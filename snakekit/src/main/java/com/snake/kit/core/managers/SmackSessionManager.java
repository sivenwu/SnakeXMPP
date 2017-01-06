package com.snake.kit.core.managers;

import android.content.Context;
import android.util.ArrayMap;

import com.snake.kit.core.mngservices.ISessionManager;
import com.snake.kit.interfaces.SnakeServiceLetterListener;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import java.util.Map;

/**
 * Created by Yuan on 2016/11/11.
 * Detail 会话管理
 */

public class SmackSessionManager extends BaseManager implements ISessionManager{

    private SmackMessageManager mSmackMessageManager;

    private ChatManager mChatManager;
    private OfflineMessageManager mOfflineMessageManager;
    private Map<String,Object> mCurChatMap;
//    private SparseArray mSparseArray;


    public SmackSessionManager(Context context, SnakeServiceLetterListener mLetterListener, AbstractXMPPConnection mConnection) {
        super(context, mLetterListener, mConnection);

        mChatManager = ChatManager.getInstanceFor(mConnection);
        mOfflineMessageManager = new OfflineMessageManager(mConnection);

        mCurChatMap = new ArrayMap<>();
//        mSparseArray = new SparseArray();
        mSmackMessageManager = new SmackMessageManager(mChatManager,mOfflineMessageManager);

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

        mSmackMessageManager.sendMessage(curChat,userJid,message);
    }

    @Override
    public void sendMessage(String userJid, com.snake.kit.core.data.Message message) {
        Chat curChat = (Chat) mCurChatMap.get(userJid);

        if (curChat == null){
            curChat = createChat(userJid);
        }

        mSmackMessageManager.sendMessage(curChat,userJid,message);
    }


    @Override
    public void receiveMessage() {

    }

    public void getOfflineMessage(){

        mSmackMessageManager.getOfflineMessage();

    }
}
