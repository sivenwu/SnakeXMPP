package com.snake.kit.interfaces;


import com.snake.api.data.MessageModel;

/**
 * Created by Yuan on 2016/11/30.
 * Detail 聊天消息监听器
 */

public interface ChatMessageListener {

    void onRecevie(MessageModel model, String message);

}
