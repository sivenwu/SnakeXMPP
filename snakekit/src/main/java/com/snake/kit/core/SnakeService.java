package com.snake.kit.core;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.snake.api.apptools.LogTool;
import com.snake.api.apptools.SnakePref;
import com.snake.api.data.SnakeConstants;
import com.snake.kit.SnakeKit;
import com.snake.kit.core.managers.ManagerUtil;
import com.snake.kit.core.managers.PingPongManager;
import com.snake.kit.core.managers.SmackMucManager;
import com.snake.kit.core.managers.SmackOnselfManager;
import com.snake.kit.core.managers.SmackRosterManager;
import com.snake.kit.core.managers.SmackSessionManager;
import com.snake.kit.interfaces.ChatMessageListener;
import com.snake.kit.interfaces.XmppLoginListener;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;
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

    private ManagerUtil mManagerUtil;
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
    private boolean isInitManager = false;// 是否初始化了manager

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
        // TODO: 2016/12/20   重启？ 
        SnakeKit.getKit().restartSnakeService();
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

    // 注册用户
    public void registerUser(String account, String password){
        onselfManager.registerUser(account,password);
    }

    // 修改密码
    public void modifyPassword(){
        onselfManager.modifyPassword();
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

    /**
     * 会话管理调用方法
     *****************************************************************************/

    // 创建一个聊天会话
    public Chat createChat(String userJid){
        return sessionManager.createChat(userJid);
    }

    public Chat createChat(String userJid,ChatMessageListener listener){
        return sessionManager.createChat(userJid,listener);
    }

    // 发送消息
    public void sendMessage(String userJid,String message){
        sessionManager.sendMessage(userJid,message);
    }

    public void sendMessage(String userJid,Message message){
        sessionManager.sendMessage(userJid,message);
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

    private void connect(){
        connect(this.server,this.port);
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

                    if (mManagerUtil != null)
                    mManagerUtil.notifyChangeData(mConnection);

                    if (!isExcuLogin){// 如果没有执行登录，则进行登录操作
                        try {
                            onselfManager.login();
                        } catch (Exception e) {
                            LogTool.e(e.getMessage().toString());
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

            if (!mConnection.isConnected())
            mConnection.connect();
            if (!isInitManager)
            initManager();

        } catch (SmackException | IOException | XMPPException e) {
            LogTool.e(e.getMessage().toString());
            e.printStackTrace();
        }
    }

    private void initManager() {
        mucManager = new SmackMucManager(this, mConnection);
        rosterManager = new SmackRosterManager(this, mConnection);
        pingPongManager = new PingPongManager(this, mConnection);
        sessionManager = new SmackSessionManager(this, mConnection);
        onselfManager = new SmackOnselfManager(this, mConnection);

        mManagerUtil = new ManagerUtil();
        mManagerUtil.register(mucManager);
        mManagerUtil.register(rosterManager);
        mManagerUtil.register(pingPongManager);
        mManagerUtil.register(sessionManager);
        mManagerUtil.register(onselfManager);

        isInitManager = true;
    }


    private void registerManagerService() {
        pingPongManager.registerPongServer();
        pingPongManager.registerCallBack(new PingPongManager.PingPongCallBack() {
            @Override
            public void pingTimeOut() {
                isExcuLogin = false;
            }

            @Override
            public void pingNoneAuthenticated() {
                // 尝试登录
                logout();
                connect();
            }
        });
        //...
    }

}
