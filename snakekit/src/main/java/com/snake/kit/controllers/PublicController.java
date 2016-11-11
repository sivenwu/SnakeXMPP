package com.snake.kit.controllers;

import com.snake.kit.interfaces.XmppLoginListener;

/**
 * Created by Yuan on 2016/11/11.
 * Detail 公共控制器 登录 登出 个人信息等等
 */

public class PublicController extends APPController {

    public static void login(String uesrName, String password, XmppLoginListener xmppLoginListener){
        if (!isRunning()){
            return ;
        }
        getmSnakeService().login(uesrName,password,xmppLoginListener);
    }
}
