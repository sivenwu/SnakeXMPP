package com.snake.kit.core.handlers;

import android.os.Handler;
import android.os.Message;

import com.snake.kit.core.SnakeService;

import java.lang.ref.WeakReference;

/**
 * Created by Yuan on 2016/12/24.
 * Detail 维护一个主线程handler，负责代理转发子线程接口 for SnakeService
 */


public class SnakeServiceManager {


    private SnakeServiceHandler handler;

    public SnakeServiceManager(SnakeService snakeService) {
        this.handler = new SnakeServiceHandler(snakeService);
    }

    public static void delegate(SnakeService snakeService){
        new SnakeServiceHandler(snakeService);
    }

    private static class SnakeServiceHandler extends Handler {
        private final WeakReference<SnakeService> mSnakeService;

        public SnakeServiceHandler(SnakeService snakeService) {
            mSnakeService = new WeakReference<SnakeService>(snakeService);
        }

        @Override
        public void handleMessage(Message msg) {
        }
    }

}
