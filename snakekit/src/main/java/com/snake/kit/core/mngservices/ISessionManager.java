package com.snake.kit.core.mngservices;

import com.snake.kit.interfaces.ChatMessageListener;

import org.jivesoftware.smack.chat.Chat;

/**
 * Created by Yuan on 2016/11/30.
 * Detail interface for SmackSessionManager
 */

public interface ISessionManager {

    // 创建一个聊天会话
    public Chat createChat(String userJid);

    public Chat createChat(String userJid,ChatMessageListener listener);

    // 发送消息
    public void sendMessage(String userJid,String message);

    public void sendMessage(String userJid,com.snake.kit.core.data.Message message);

    // 接收消息
    public void receiveMessage();

}
