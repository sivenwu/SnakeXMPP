package com.snake.kit.controllers;

import com.snake.kit.interfaces.ChatMessageListener;

/**
 * Created by Yuan on 2016/11/10.
 * Detail 会话控制器
 */

public final class SessionController extends APPController{

    // 创建一个聊天会话
    public static boolean createChat(String userJid){
        if (!isRunning()){
            return false;
        }
        return (getmSnakeService().createChat(userJid) != null);
    }

    public static boolean createChat(String userJid,ChatMessageListener listener){
        if (!isRunning()){
            return false;
        }
        return (getmSnakeService().createChat(userJid,listener) != null);
    }

    // 发送消息
    public static void sendMessage(String userJid,String message){
        if (!isRunning()){
            return ;
        }
        getmSnakeService().sendMessage(userJid,message);
    }

    public static void sendMessage(String userJid,com.snake.kit.core.data.Message message){
        if (!isRunning()){
            return ;
        }
        getmSnakeService().sendMessage(userJid,message);
    }
}
