package com.snake.kit.core.managers;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.snake.kit.apptools.LogTool;
import com.snake.kit.apptools.SnakePref;
import com.snake.kit.core.data.SnakeConstants;
import com.snake.kit.interfaces.XmppLoginListener;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

/**
 * Created by Yuan on 2016/11/7.
 * Detail Xmpp service
 */

public class SnackService extends Service{

    private final String TAG = "SnackService";

    // service
    private SnackBinder binder = new SnackBinder();

    //xmpp
    private volatile AbstractXMPPConnection mConnection;
    private SmackMessageManager messageManager;
    private SmackMucManager mucManager;
    private SmackRosterManager rosterManager;
    private PingPongManager pingPongManager;

    // User
    private String login;
    private String password;
    private String server;
    private int port;

    // listener
    private XmppLoginListener xmppLoginListener;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 这里启动连接服务
        LogTool.d("smack start to connect..");
        connect(SnakePref.getString(SnakeConstants.SMACK_SERVER,""),SnakePref.getInt(SnakeConstants.SMACK_SERVER_PORT,5222));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // binder for snackService
    public class SnackBinder extends Binder{
        public SnackService getService(){return SnackService.this;}
    }

    //----------------------------------------------------------------------------------------------
    //--------- SnackService public method----------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    // 登录
    public void login(String userName,String password,XmppLoginListener xmppLoginListener){
        this.xmppLoginListener = xmppLoginListener;
        if (mConnection != null){
            try {
                mConnection.login(userName,password);
            } catch (Exception e) {
                this.xmppLoginListener.onError(e,e.getMessage().toLowerCase());
                e.printStackTrace();
            }
        }
    }

    // 注销登录
    public void logout(){

    }

    //----------------------------------------------------------------------------------------------
    //--------- SnackService private method---------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    private void connect(String server,int port){

        if (mConnection != null && mConnection.isAuthenticated()){
            LogTool.d("Authentication is valid now !");
            return ;
        }

        this.server = server;
        this.port = port;

        new Thread(new Runnable() {
            @Override
            public void run() {
                justConnect();
            }
        }).start();
    }

    private void justConnect(){
        try {
            XMPPTCPConnectionConfiguration connectionConfiguration =  XMPPTCPConnectionConfiguration.builder()
                    .setHost(this.server)
                    .setPort(this.port)
                    .setServiceName(this.server)
                    .setSendPresence(true)// support presence
//                    .setUsernameAndPassword(this.login,this.password)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)//越过证书
                    .build();
            mConnection = new XMPPTCPConnection(connectionConfiguration);
            mConnection.addConnectionListener(new ConnectionListener() {
                @Override
                public void connected(XMPPConnection connection) {
                    LogTool.d("connected");
                    mConnection = (AbstractXMPPConnection) connection;
                }

                @Override
                public void authenticated(XMPPConnection connection, boolean resumed) {
                    LogTool.d("authenticated");
                    if (xmppLoginListener != null){
                        xmppLoginListener.authenticated();

                        // 开始注册服务
                        registerManagerService();
                    }
                }

                @Override
                public void connectionClosed() {
                    LogTool.d("connectionClosed");
                }

                @Override
                public void connectionClosedOnError(Exception e) {
                    LogTool.d("connectionClosedOnError " + e.getMessage().toString());
                }

                @Override
                public void reconnectionSuccessful() {
                    LogTool.d("reconnectionSuccessful");
                }

                @Override
                public void reconnectingIn(int seconds) {
                    LogTool.d("reconnectingIn");
                }

                @Override
                public void reconnectionFailed(Exception e) {
                    LogTool.d("reconnectionFailed "+ e.getMessage().toString());
                }
            });

            mConnection.connect();
            initManager();

        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    public void disConnect(){
        if (mConnection != null){
            mConnection.disconnect();
        }
    }


    private void initManager(){
        messageManager = new SmackMessageManager(this,mConnection);
        mucManager = new SmackMucManager(this,mConnection);
        rosterManager = new SmackRosterManager(this,mConnection);
        pingPongManager = new PingPongManager(this,mConnection);
    }

    private void registerManagerService(){
        pingPongManager.registerPongServer();
        //...
    }

}
