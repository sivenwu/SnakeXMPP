package com.snake.kit.core.managers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.snake.kit.apptools.MessageDealUtil;
import com.snake.kit.apptools.SnakeUtilKit;
import com.snake.kit.core.data.MessageModel;
import com.snake.kit.core.receivers.MessageReceiver;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by Yuan on 2016/11/11.
 * Detail 会话管理
 */

public class SmackSessionManager extends BaseManager implements ChatManagerListener,ChatMessageListener{

    private ChatManager mChatManager;

    public SmackSessionManager(Context context, AbstractXMPPConnection mConnection) {
        super(context, mConnection);

        mChatManager = ChatManager.getInstanceFor(mConnection);
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
}
