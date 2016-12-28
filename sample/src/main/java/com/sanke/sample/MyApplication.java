package com.sanke.sample;

import android.app.Application;

import com.snake.kit.SnakeKit;

/**
 * Created by Yuan on 2016/12/27.
 * Detail
 */

public class MyApplication extends Application{

    private String url = "yyssqe.oicp.net";
    private int port = 25196;

//    private String url = "192.168.1.101";
//    private int port = 5222;

    @Override
    public void onCreate() {
        super.onCreate();
//        // 初始化snake
        SnakeKit.getKit().init(this, url, port);
    }
}
