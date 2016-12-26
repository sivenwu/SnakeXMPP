package com.snake.kit.core.managers;

import android.content.Intent;
import android.os.Bundle;

import com.snake.api.apptools.LogTool;
import com.snake.api.apptools.SnakeUtilKit;
import com.snake.api.data.MessageModel;
import com.snake.kit.apptools.MessageDealUtil;
import com.snake.kit.core.mngservices.IMessageManager;
import com.snake.kit.core.receivers.MessageReceiver;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import java.util.List;

/**
 * Created by Yuan on 2016/11/7.
 * Detail 聊天信息处理管理器，包括推送消息
 */

public class SmackMessageManager implements IMessageManager,ChatManagerListener,ChatMessageListener {

    private com.snake.kit.interfaces.ChatMessageListener listener;
    private ChatManager mChatManager;
    private OfflineMessageManager mOfflineMessageManager;


    public SmackMessageManager(ChatManager mChatManager, OfflineMessageManager mOfflineMessageManager) {
        this.mChatManager = mChatManager;
        this.mOfflineMessageManager = mOfflineMessageManager;

        getOfflineMessage();
    }

    public void setListener(com.snake.kit.interfaces.ChatMessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void sendMessage(Chat chat, String message) {
        try {
            chat.sendMessage(message);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(Chat chat, Message message) {
        try {
            chat.sendMessage(message);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveMessage() {

    }

    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {
        chat.addMessageListener(this);
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        MessageModel msg = MessageDealUtil.dealMessage(message);

        if (msg.getBody() != null) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable("message", msg);

            intent.putExtras(bundle);
            intent.setAction(MessageReceiver.SNAKE_MESSAGE_ACTION);
            SnakeUtilKit.getSnakeApp().sendBroadcast(intent);

            if (listener != null){
                listener.onRecevie(msg,message.getBody());
            }

        }
    }

    public void registerListenter(){
        if (mChatManager != null) {

            if (mChatManager.getChatListeners().size() > 0){
                // 注册前 移除之前的监听，避免重复监听
                mChatManager.removeChatListener(this);
            }

            mChatManager.addChatListener(this);
        }
    }

    public void getOfflineMessage(){

        LogTool.d("获取离线消息..");

        if (mOfflineMessageManager != null){

            try {
                if (mOfflineMessageManager.getMessageCount() >0){

                  List<Message> messages = mOfflineMessageManager.getMessages();

                    for (Message message : messages){
                        processMessage(null,message);
                    }

                }

//                mOfflineMessageManager.deleteMessages();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}
