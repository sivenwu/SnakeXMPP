package com.snake.kit;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.snake.api.apptools.LogTool;
import com.snake.api.apptools.SnakeUtilKit;
import com.snake.kit.controllers.MessageController;
import com.snake.kit.controllers.MucController;
import com.snake.kit.controllers.PublicController;
import com.snake.kit.controllers.RosterController;
import com.snake.kit.controllers.SessionController;
import com.snake.kit.core.SnakeService;

import java.util.Observable;


/**
 * Created by Yuan on 2016/11/4.
 * Detail 全局配置入口
 */

public class SnakeKit extends Observable {

    private static SnakeKit kit = new SnakeKit();

    private SnakeService mSnackService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogTool.d("SnakeService start to connect..");
            mSnackService = ((SnakeService.SnackBinder) service).getService();
            // 每次重新连接通知控制器更新service
            notifyChangeData(mSnackService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            notifyChangeData(null);
        }
    };

    public static SnakeKit getKit() {
        if (kit == null) {
            kit = new SnakeKit();
        }
        return kit;
    }

    public void init(Application application, String server, int port) {

        // 工具初始化
        SnakeUtilKit.init(application, server, port);

        // 注册控制器
        registerAppControllers();

        // 服务开始启动...
        startSnakeService(application);
    }

    //----------------------------------------------------------------------------------------------

    private void notifyChangeData(Object obj) {
        setChanged();
        notifyObservers(obj);
    }

    private void startSnakeService(Application application) {
        application.startService(new Intent(application, SnakeService.class));
        application.bindService(new Intent(application, SnakeService.class),
                mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void registerAppControllers() {
        addObserver(new PublicController());
        addObserver(new MessageController());
        addObserver(new MucController());
        addObserver(new RosterController());
        addObserver(new SessionController());
    }
}
