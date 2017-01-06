package com.snake.kit.core.mngservices;

import com.snake.kit.core.data.Message;

import org.jivesoftware.smack.chat.Chat;

/**
 * Created by Yuan on 2016/12/21.
 * Detail interface for SmackMessageManager
 */

public interface IMessageManager {

    // 发送消息
    public void sendMessage(Chat chat,String jid,String message);

    public void sendMessage(Chat chat,String jid,Message message);

    // 接收消息
    public void receiveMessage();

}
