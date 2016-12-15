package com.snake.kit.core.managers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;

import com.snake.api.apptools.SnakeUtilKit;
import com.snake.api.data.MessageModel;
import com.snake.kit.apptools.MessageDealUtil;
import com.snake.kit.core.mngservices.ISessionManager;
import com.snake.kit.core.receivers.MessageReceiver;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by Yuan on 2016/11/11.
 * Detail 会话管理
 */

public class SmackSessionManager extends BaseManager implements ISessionManager,ChatManagerListener,ChatMessageListener{

    private ChatManager mChatManager;
    private SparseArray mSparseArray;

    public SmackSessionManager(Context context, AbstractXMPPConnection mConnection) {
        super(context, mConnection);

        mChatManager = ChatManager.getInstanceFor(mConnection);
        mSparseArray = new SparseArray();

        registerListenter();
    }

    private void registerListenter(){
        if (mChatManager != null)
            mChatManager.addChatListener(this);
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
        }
    }

    @Override
    public Chat createChat(String userJid) {

        Chat curChat = mChatManager.createChat(userJid);
        // 存储数据库
//        mSparseArray.put(userJid,curChat);

        return mChatManager.createChat(userJid);
    }

    @Override
    public Chat createChat(String userJid, final com.snake.kit.interfaces.ChatMessageListener listener) {
        return mChatManager.createChat(userJid, new ChatMessageListener() {
            @Override
            public void processMessage(Chat chat, Message message) {
                MessageModel msg = MessageDealUtil.dealMessage(message);

                if (msg.getBody() != null) {
                    listener.onRecevie(msg,message.getBody());
                }
            }
        });
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
}
