package com.snake.kit.core.mngservices;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by Yuan on 2016/12/21.
 * Detail interface for SmackMessageManager
 */

public interface IMessageManager {

    // 发送消息
    public void sendMessage(Chat chat, String message);

    public void sendMessage(Chat chat,Message message);

    // 接收消息
    public void receiveMessage();

}
