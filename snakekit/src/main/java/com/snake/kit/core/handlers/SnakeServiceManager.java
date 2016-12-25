package com.snake.kit.core.handlers;

import android.os.Handler;
import android.os.Message;

import com.snake.kit.core.SnakeService;
import com.snake.kit.interfaces.XmppLoginListener;

import java.lang.ref.WeakReference;

/**
 * Created by Yuan on 2016/12/24.
 * Detail 维护一个主线程handler，负责代理转发子线程接口 for SnakeService
 */


public class SnakeServiceManager {

    private SnakeServiceHandler handler;
    private Object curHandlerObject;

    // Handler CODE
    public static final int HANDLER_CODE_LOGIN_SUCCESS = 0X01;
    public static final int HANDLER_CODE_LOGIN_FAILED = 0X02;


    // Listenter
    private XmppLoginListener mXmppLoginListener;
    public void setXmppLoginListener(XmppLoginListener mXmppLoginListener) {
        this.mXmppLoginListener = mXmppLoginListener;
    }


    public SnakeServiceManager(SnakeService snakeService) {
        this.handler = new SnakeServiceHandler(snakeService){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case HANDLER_CODE_LOGIN_SUCCESS:
                        justLoginSuccess();
                        break;
                    case HANDLER_CODE_LOGIN_FAILED:
                        loginFailed();
                        break;
                }
            }
        };
    }

    public static SnakeServiceManager delegate(SnakeService snakeService){
        return new SnakeServiceManager(snakeService);
    }

    public void handler(int code){

        handler(code,null);

    }

    public void handler(int code,Object obj){

        curHandlerObject = obj;

        handler.sendEmptyMessage(code);

    }

    //---------------------------------------------------------------------------------------------

    private void justLoginSuccess(){
        if (this.mXmppLoginListener !=null){
            this.mXmppLoginListener.authenticated();
        }
    }

    private void loginFailed(){
        if (this.mXmppLoginListener !=null){
            if (curHandlerObject != null) {
                Exception mException = (Exception) curHandlerObject;
                if (mException != null) {
                    this.mXmppLoginListener.onError(mException, mException.getMessage().toLowerCase());
                    mException.printStackTrace();
                }
            }else{
                this.mXmppLoginListener.onError(null, "");
            }
        }
    }


    //---------------------------------------------------------------------------------------------

    private static class SnakeServiceHandler extends Handler {
        private final WeakReference<SnakeService> mSnakeService;

        public SnakeServiceHandler(SnakeService snakeService) {
            mSnakeService = new WeakReference<SnakeService>(snakeService);
        }

    }

}
