package com.snake.kit.apptools;

import android.app.Application;

/**
 * Created by Yuan on 2016/11/4.
 * Detail 统一
 *        存储公共全局参数
 *        初始化所有工具类
 *
 *        方便管理
 */

public class SnakeUtilKit {

    public static SnakeUtilKit kit;

    //app
    private static Application snakeApp;

    //build control
    public static boolean isDebug = true;

    public SnakeUtilKit(Application application) {
        init(application);
    }

    public static  void init(Application application){
        snakeApp = application;

        // init tools..
        SnakePref.init(snakeApp);
    }

    public static Application getSnakeApp() {
        return snakeApp;
    }

    public static boolean isDebug() {
        return isDebug;
    }
}
