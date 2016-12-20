package com.snake.kit.core;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.snake.api.apptools.LogTool;
import com.snake.api.apptools.SnakePref;
import com.snake.api.data.SnakeConstants;
import com.snake.kit.core.managers.PingPongManager;
import com.snake.kit.core.managers.SmackMessageManager;
import com.snake.kit.core.managers.SmackMucManager;
import com.snake.kit.core.managers.SmackOnselfManager;
import com.snake.kit.core.managers.SmackRosterManager;
import com.snake.kit.core.managers.SmackSessionManager;
import com.snake.kit.interfaces.XmppLoginListener;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

/**
 * Created by Yuan on 2016/11/7.
 * Detail Xmpp service
 */

public class SnakeService extends Service {

    private final String TAG = "SnackService";

    // service
    private SnackBinder binder = new SnackBinder();

    //xmpp
    private volatile AbstractXMPPConnection mConnection;
    private SmackMessageManager messageManager;
    private SmackMucManager mucManager;
    private SmackRosterManager rosterManager;
    private PingPongManager pingPongManager;
    private SmackSessionManager sessionManager;
    private SmackOnselfManager onselfManager;

    // User
    private String login;
    private String password;
    private String server;
    private int port;

    // flag
    private boolean isExcuLogin = false;//是否已经执行登录（非是否登录成功） 为服务是否连接判定

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
        connect(SnakePref.getString(SnakeConstants.SMACK_SERVER, ""), SnakePref.getInt(SnakeConstants.SMACK_SERVER_PORT, 5222));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // binder for snackService
    public class SnackBinder extends Binder {
        public SnakeService getService() {
            return SnakeService.this;
        }
    }

    //----------------------------------------------------------------------------------------------
    //--------- SnackService public method----------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    /**
     * 公共调用方法
     *********************************************************************************/

    // 登录
    public void login(String userName, String password, XmppLoginListener xmppLoginListener) {
        this.xmppLoginListener = xmppLoginListener;
        if (mConnection != null) {
            if (mConnection.isConnected()) {
                try {
                    isExcuLogin = true;
                    onselfManager.login(userName, password);
                } catch (Exception e) {
                    this.xmppLoginListener.onError(e, e.getMessage().toLowerCase());
                    e.printStackTrace();
                }
            }else{
                try {
                    isExcuLogin = false;
                    mConnection.connect();
                } catch (Exception e) {
                    this.xmppLoginListener.onError(e, e.getMessage().toLowerCase());
                    e.printStackTrace();
                }
            }
        }else{
            // 如果服务没有启动则启动连接
//            justConnect();
        }
    }

    // 注销登录
    public void logout() {
        isExcuLogin = false;
        onselfManager.logout();
    }

    // 断开连接
    public void disConnect() {
        isExcuLogin = false;
        if (mConnection != null) {
            mConnection.disconnect();
        }
    }


    /**
     * 好友管理调用方法
     *****************************************************************************/
    public void getAllRosters() {
        rosterManager.getAllRosters();
        ;
    }

    public void addRoster(String user, String name, String groupName) {
        rosterManager.addRoster(user, name, groupName);
    }

    public void setSubscriptionMode(Roster.SubscriptionMode mode) {
        rosterManager.setSubscriptionMode(mode);
    }

    public void deleteRoster(String user) {
        rosterManager.deleteRoster(user);
    }

    public void updateRosterInfo() {
        rosterManager.updateRosterInfo();
    }

    public void updateRosterByGroup(String user, String curGroup, String mvGroup) {
        rosterManager.updateRosterByGroup(user, curGroup, mvGroup);
    }

    public void setGroupName(String groupName, String modifyName) {
        rosterManager.setGroupName(groupName, modifyName);
    }

    public void addGroup(String groupName) {
        rosterManager.addGroup(groupName);
    }

    public void deleteGroup(String groupName) {
        rosterManager.deleteGroup(groupName);
    }

    //----------------------------------------------------------------------------------------------
    //--------- SnackService private method---------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    private void connect(String server, int port) {

        if (mConnection != null && mConnection.isAuthenticated()) {
            LogTool.d("Authentication is valid now !");
            return;
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

    private void justConnect() {
        try {
            XMPPTCPConnectionConfiguration connectionConfiguration = XMPPTCPConnectionConfiguration.builder()
                    .setHost(this.server)
                    .setPort(this.port)
                    .setServiceName(this.server)
                    .setSendPresence(true)// support presence
                    .setConnectTimeout(1000 * 10)
//                    .setUsernameAndPassword(this.login,this.password)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)//越过证书
                    .build();
            mConnection = new XMPPTCPConnection(connectionConfiguration);
            mConnection.addConnectionListener(new ConnectionListener() {
                @Override
                public void connected(XMPPConnection connection) {
                    LogTool.d("connected");
                    mConnection = (AbstractXMPPConnection) connection;

                    if (!isExcuLogin){// 如果没有执行登录，则进行登录操作
                        try {
                            onselfManager.login();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void authenticated(XMPPConnection connection, boolean resumed) {
                    LogTool.d("authenticated");
                    if (xmppLoginListener != null) {
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
                    LogTool.d("reconnectionFailed " + e.getMessage().toString());
                }
            });

            mConnection.connect();
            initManager();

        } catch (SmackException | IOException | XMPPException e) {
            e.printStackTrace();
        }
    }

    private void initManager() {
        messageManager = new SmackMessageManager(this, mConnection);
        mucManager = new SmackMucManager(this, mConnection);
        rosterManager = new SmackRosterManager(this, mConnection);
        pingPongManager = new PingPongManager(this, mConnection);
        sessionManager = new SmackSessionManager(this, mConnection);
        onselfManager = new SmackOnselfManager(this, mConnection);
    }

    private void registerManagerService() {
        pingPongManager.registerPongServer();
        //...
    }

}
