package com.snake.kit.core.handlers;

import android.os.Handler;
import android.os.Message;

import com.snake.kit.core.SnakeService;
import com.snake.kit.interfaces.ISnakeRosterListener;
import com.snake.kit.interfaces.XmppLoginListener;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.snake.dbkit.bean.ContactModel;

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
    public static final int HANDLLER_CODE_GET_ROSETER = 0X03;


    // Listenter
    private WeakReference<XmppLoginListener> mWfXmppLoginListener;
    public void setXmppLoginListener(XmppLoginListener mXmppLoginListener) {
        this.mWfXmppLoginListener = new WeakReference<XmppLoginListener>(mXmppLoginListener);
    }

    private WeakReference<ISnakeRosterListener> mWfISnakeRosterListener;
    public void setISnakeRosterListener(ISnakeRosterListener mISnakeRosterListener) {
        this.mWfISnakeRosterListener = new WeakReference<ISnakeRosterListener>(mISnakeRosterListener);
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
                    case HANDLLER_CODE_GET_ROSETER:
                        getRoster();
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
        if (this.mWfXmppLoginListener.get() !=null){
            this.mWfXmppLoginListener.get().authenticated();
            this.mWfXmppLoginListener.clear();// 回收
        }
    }

    private void loginFailed(){
        if (this.mWfXmppLoginListener.get() !=null){
            if (curHandlerObject != null) {
                Exception mException = (Exception) curHandlerObject;
                if (mException != null) {
                    this.mWfXmppLoginListener.get().onError(mException, mException.getMessage().toLowerCase());
                    mException.printStackTrace();
                }
            }else{
                this.mWfXmppLoginListener.get().onError(null, "");
            }
            this.mWfXmppLoginListener.clear();// 回收
        }
    }

    private void getRoster(){
        if (this.mWfISnakeRosterListener.get() != null){
            if (curHandlerObject != null){
                List<ContactModel> contactModels = (List<ContactModel>) curHandlerObject;
                if (contactModels != null){
                    this.mWfISnakeRosterListener.get().rosterEntires(contactModels);
                }
            }

            this.mWfISnakeRosterListener.clear();
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
