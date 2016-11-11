package com.snake.kit.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.snake.kit.apptools.SnakeUtilKit;
import com.snake.kit.core.SnakeService;

/**
 * Created by Yuan on 2016/11/7.
 * Detail start SnackService for activity
 */

public abstract class SnackActivity extends AppCompatActivity{

    private boolean isBinder = false;
    public SnakeService mSnackService = null;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mSnackService = ((SnakeService.SnackBinder) service).getService();
            isBinder = true;
//            mSnackService.connect((String) SnakePref.getObject(SnakeContacts.SMACK_USER_ACCOUNT,"")
//                    ,(String)SnakePref.getObject(SnakeContacts.SMACK_USER_PASSWORD,"")
//                    ,(String)SnakePref.getObject(SnakeContacts.SMACK_SERVER,"")
//                    ,(int)SnakePref.getObject(SnakeContacts.SMACK_SERVER_PORT,5222));
            bindByServiceConnect(mSnackService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBinder = false;
            bindByServiceDisconnect();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SnakeUtilKit.init(getApplication(),getServer(),getServerPort());
        startSnackService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBinder){
            mSnackService.disConnect();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isBinder){
            bindService( new Intent(this, SnakeService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isBinder) return ;
        unbindService(mServiceConnection);
        isBinder = false;
    }

    private void startSnackService(){
        startService(new Intent(this, SnakeService.class));
        bindService( new Intent(this, SnakeService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    //---------- public method for child------------------------------------------------------------
    public abstract String getServer();
    public abstract int getServerPort();
    public abstract void bindByServiceConnect(SnakeService mSnakeService);
    public abstract void bindByServiceDisconnect();
}
