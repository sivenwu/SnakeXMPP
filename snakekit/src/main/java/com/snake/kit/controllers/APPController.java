package com.snake.kit.controllers;

import com.snake.kit.apptools.LogTool;
import com.snake.kit.core.SnakeService;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Yuan on 2016/11/10.
 * Detail
 */

public class APPController implements Observer{

    private final static String MSG = "SnakeService was killed,try to prepare to restart";
    private static SnakeService mSnakeService;

    protected static SnakeService getmSnakeService() {
        return mSnakeService;
    }

    protected static boolean isRunning(){
        if (mSnakeService == null){
            // TODO: 2016/11/11  这里以后控制重新启动服务..
            LogTool.d(MSG);
            return false;
        }
        return true;
    }

    @Override
    public void update(Observable observable, Object data) {

        if (data == null){
            mSnakeService = null;
            return ;
        }

        if (data instanceof SnakeService)
        mSnakeService = (SnakeService) data;
    }


}
