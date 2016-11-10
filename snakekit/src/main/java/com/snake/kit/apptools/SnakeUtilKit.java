package com.snake.kit.apptools;

import android.app.Application;

import com.snake.kit.core.data.SnakeConstants;

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
    private static String SERVER_URL;
    private static int SERVER_PORY;

    public SnakeUtilKit(Application application,String serverUrl,int port) {
        init(application,serverUrl,port);
    }

    public static void init(Application application,String serverUrl,int port){

        snakeApp = application;

        // init tools..
        SnakePref.init(snakeApp);

        // config
        SERVER_URL = serverUrl;
        SERVER_PORY = port;
        saveConfigration();

    }

    private static void saveConfigration(){
        SnakePref.putObject(SnakeConstants.SMACK_SERVER,SERVER_URL);
        SnakePref.putObject(SnakeConstants.SMACK_SERVER_PORT,SERVER_PORY);
    }

    public static Application getSnakeApp() {
        return snakeApp;
    }

    public static boolean isDebug() {
        return isDebug;
    }
}
