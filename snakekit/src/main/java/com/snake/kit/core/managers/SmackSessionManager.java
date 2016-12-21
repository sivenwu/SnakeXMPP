package com.snake.kit.core.managers;

import android.content.Context;
import android.util.ArrayMap;

import com.snake.kit.core.mngservices.ISessionManager;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;

import java.util.Map;

/**
 * Created by Yuan on 2016/11/11.
 * Detail 会话管理
 */

public class SmackSessionManager extends BaseManager implements ISessionManager{

    private SmackMessageManager mSmackMessageManager;

    private ChatManager mChatManager;
    private Map<String,Object> mCurChatMap;
//    private SparseArray mSparseArray;

    public SmackSessionManager(Context context, AbstractXMPPConnection mConnection) {
        super(context, mConnection);

        mChatManager = ChatManager.getInstanceFor(mConnection);
        mCurChatMap = new ArrayMap<>();
//        mSparseArray = new SparseArray();
        mSmackMessageManager = new SmackMessageManager(mChatManager);

        mSmackMessageManager.registerListenter();
    }

    @Override
    public Chat createChat(String userJid) {

        Chat curChat = null;
        if (mCurChatMap.containsKey(userJid)){
            curChat = (Chat) mCurChatMap.get(userJid);

            if (curChat != null){ return curChat; }
        }

        curChat = mChatManager.createChat(userJid);
        mCurChatMap.put(userJid,curChat);

        return curChat;
    }

    @Override
    public Chat createChat(String userJid, final com.snake.kit.interfaces.ChatMessageListener listener) {

        mSmackMessageManager.setListener(listener);

        Chat curChat = null;
        if (mCurChatMap.containsKey(userJid)){
            curChat = (Chat) mCurChatMap.get(userJid);

            if (curChat != null){
                return curChat;
            }
        }

        curChat = mChatManager.createChat(userJid);
        mCurChatMap.put(userJid,curChat);

        return curChat;
    }

    @Override
    public void sendMessage(String userJid, String message) {
        Chat curChat = (Chat) mCurChatMap.get(userJid);

        if (curChat == null){
            curChat = createChat(userJid);
        }

        mSmackMessageManager.sendMessage(curChat,message);
    }

    @Override
    public void sendMessage(String userJid, Message message) {
        Chat curChat = (Chat) mCurChatMap.get(userJid);

        if (curChat == null){
            curChat = createChat(userJid);
        }

        mSmackMessageManager.sendMessage(curChat,message);
    }


    @Override
    public void receiveMessage() {

    }
}
