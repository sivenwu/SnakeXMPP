package com.snake.kit.core.managers;

import android.content.Intent;
import android.os.Bundle;

import com.snake.api.apptools.LogTool;
import com.snake.api.apptools.SnakeGsonUtil;
import com.snake.api.apptools.SnakeUtilKit;
import com.snake.api.data.MessageModel;
import com.snake.kit.apptools.MessageDealUtil;
import com.snake.kit.core.data.SnakeRouter;
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

import cn.snake.dbkit.bean.ChatInfoModel;
import cn.snake.dbkit.manager.DBOperationManager;

/**
 * Created by Yuan on 2016/11/7.
 * Detail 聊天信息处理管理器，包括推送消息
 */

public class SmackMessageManager implements IMessageManager,ChatManagerListener,ChatMessageListener {

    private DBOperationManager dbOperationManager;// 数据库操作对象

    private com.snake.kit.interfaces.ChatMessageListener listener;
    private ChatManager mChatManager;
    private OfflineMessageManager mOfflineMessageManager;

    public SmackMessageManager(ChatManager mChatManager, OfflineMessageManager mOfflineMessageManager) {
        this.mChatManager = mChatManager;
        this.mOfflineMessageManager = mOfflineMessageManager;

        getOfflineMessage();
        initDbOperationManager();
    }

    public void setListener(com.snake.kit.interfaces.ChatMessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void sendMessage(Chat chat,String jid, String m) {
        sendMessage(chat,jid,getMessage("1",m));
    }

    @Override
    public void sendMessage(Chat chat, String jid,com.snake.kit.core.data.Message message) {

        ChatInfoModel dbMessage = null;

        Object object = getSendMessage(jid,message);

        if (object !=null){
            dbMessage = (ChatInfoModel) object;
        }

        try {
            chat.sendMessage(SnakeGsonUtil.bean2json(message));
            // 没有异常则发送成功状态
            if (dbMessage != null){
                dbMessage.setMessageStatus("3");
            }

        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            if (dbMessage != null){
                dbMessage.setMessageStatus("1");
            }
        }

        if (isUseDblibrary() && dbMessage!= null){
            saveMessage(dbMessage);
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

            if (isUseDblibrary()){
                Object model = getRecvMeesage(chat,msg);
                if (model!=null)
                saveMessage((ChatInfoModel) model);
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

                mOfflineMessageManager.deleteMessages();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Converts the message to json format
     * @param type type: 1 normal message ,2 image message,3 Voice message 4,Video message
     * @param message
     */
    private  com.snake.kit.core.data.Message getMessage(String type,String message){
        com.snake.kit.core.data.Message m = new com.snake.kit.core.data.Message(type,message);
        return m;
    }

    /**
     * 获取存储数据库消息model,消息状态由外部决定
     * @param jid
     * @param m
     * @return
     */
    private Object getSendMessage(String jid,com.snake.kit.core.data.Message m){
        Object obj = null;

        obj = SnakeRouter.instance().dbObject("ChatInfoModel");

        if (obj != null){
            ChatInfoModel message = (ChatInfoModel) obj;
            message.setType(m.getType());
            message.setMessage(m.getMessage());
            message.setIsRead(1);
            message.setGroupId(-1);
            message.setIsFrom("false");
            message.setJid(jid);
        }

        return obj;
    }

    /**
     * 获取存储数据库消息model
     * @param chat
     * @param m
     */
    public Object getRecvMeesage(Chat chat, MessageModel m){

        Object obj = SnakeRouter.instance().dbObject("ChatInfoModel");

        if (obj != null){

            com.snake.kit.core.data.Message message
                    = SnakeGsonUtil.json2bean(m.getBody(),com.snake.kit.core.data.Message.class);

            ChatInfoModel model = (ChatInfoModel) obj;
            model.setType(message.getType());
            model.setMessage(message.getMessage());
            model.setIsRead(2);
            model.setGroupId(-1);
            model.setIsFrom("true");
            model.setJid(m.getFrom());
            model.setMessageStatus("3");
        }

        return obj;
    }

    private void saveMessage(ChatInfoModel message){
        dbOperationManager.insert(message);
    }

    private void initDbOperationManager(){
        if (SnakeRouter.instance().isUseDbLibrary())
        dbOperationManager = SnakeRouter.instance().getDbLibarayKit();
    }

    private boolean isUseDblibrary(){
        return SnakeRouter.instance().isUseDbLibrary();
    }

}
