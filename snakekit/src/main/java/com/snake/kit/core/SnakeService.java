package com.snake.kit.core;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.snake.api.apptools.LogTool;
import com.snake.api.apptools.SnakePref;
import com.snake.api.data.SnakeConstants;
import com.snake.kit.SnakeKit;
import com.snake.kit.core.data.bean.NETSTATE;
import com.snake.kit.core.handlers.SnakeServiceManager;
import com.snake.kit.core.managers.ManagerUtil;
import com.snake.kit.core.managers.PingPongManager;
import com.snake.kit.core.managers.SmackMucManager;
import com.snake.kit.core.managers.SmackOnselfManager;
import com.snake.kit.core.managers.SmackRosterManager;
import com.snake.kit.core.managers.SmackSessionManager;
import com.snake.kit.core.receivers.NetWorkStateReceiver;
import com.snake.kit.interfaces.ChatMessageListener;
import com.snake.kit.interfaces.ISnakeRosterListener;
import com.snake.kit.interfaces.SnakeServiceLetterListener;
import com.snake.kit.interfaces.XmppLoginListener;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

/**
 * Created by Yuan on 2016/11/7.
 * Detail Xmpp service
 */

public class SnakeService extends Service implements SnakeServiceLetterListener {

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
    private SnakeServiceManager handlerManager;

    // User
    private String login;
    private String password;
    private String server;
    private int port;

    // flag
    private boolean isExcuLogin = true;//是否已经执行登录（非是否登录成功)
    private boolean isInitsConnection = false;// 是否初始化了manager

    // Receiver
    private NetWorkStateReceiver mNetWorkStateReceiver;

    // listener
    private XmppLoginListener mXmppLoginListener;
    private ISnakeRosterListener mISnakeRosterListener;

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

    @Override
    public void sendHandlerLetter(int code) {
        if (handlerManager !=null){
            handlerManager.handler(code);
        }
    }

    @Override
    public void sendHandlerLetter(int code, Object object) {
        if (handlerManager !=null){
            handlerManager.handler(code,object);
        }
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

        this.mXmppLoginListener = xmppLoginListener;

        this.login = userName;
        this.password = password;

        if (mConnection!=null && mConnection.isConnected()) {
                handlerManager.setXmppLoginListener(xmppLoginListener);

                isExcuLogin = true;
                onselfManager.login(userName, password);
        }else{
            LogTool.i("登录失败，尝试连接服务器中..");
            isExcuLogin = false;
            connect();
        }
    }

    public void login(){

        handlerManager.setXmppLoginListener(this.mXmppLoginListener);

        if (mConnection.isConnected()) {
                isExcuLogin = true;
                onselfManager.login(login, password);
        }else{
            LogTool.i("登录失败，尝试连接服务器中..");
            isExcuLogin = false;
            connect();
        }
    }

    // 注销登录
    public void logout() {
        // 登录状态改变
        isExcuLogin = false;

        // 断开服务连接
        onselfManager.logout();

        // 释放资源
        releaseManagerService();
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
    public void getAllRosters(ISnakeRosterListener i) {
        handlerManager.setISnakeRosterListener(i);
        rosterManager.getAllRosters(i);
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

    public void sendMessage(String userJid,com.snake.kit.core.data.Message message){
        sessionManager.sendMessage(userJid,message);
    }

    //----------------------------------------------------------------------------------------------
    //--------- SnackService private method---------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    private void initConnection(){

        handlerManager = SnakeServiceManager.delegate(this);
        XMPPTCPConnectionConfiguration connectionConfiguration = XMPPTCPConnectionConfiguration.builder()
                .setHost(this.server)
                .setPort(this.port)
                .setServiceName(this.server)
                .setSendPresence(true)// support presence
                .setUsernameAndPassword("账号","密码")
                .setConnectTimeout(1000 * 10)
//                    .setUsernameAndPassword(this.login,this.password)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)//越过证书
                .build();
        mConnection = new XMPPTCPConnection(connectionConfiguration);
        mConnection.addConnectionListener(new ConnectionListener() {
            @Override
            public void connected(XMPPConnection connection) {
                LogTool.d("connected ,isExcuLogin :" + isExcuLogin);
                mConnection = (AbstractXMPPConnection) connection;

                if (mManagerUtil != null)
                    mManagerUtil.notifyChangeData(mConnection);

                if (!isExcuLogin){// 如果没有执行登录，则进行登录操作
                    login();
                }
            }

            @Override
            public void authenticated(XMPPConnection connection, boolean resumed) {
                LogTool.d("authenticated");
                sendHandlerLetter(SnakeServiceManager.HANDLER_CODE_LOGIN_SUCCESS);
                // 开始注册服务
                registerManagerService();
            }

            @Override
            public void connectionClosed() {
                LogTool.d("connectionClosed");
            }

            @Override
            public void connectionClosedOnError(Exception e) {
                LogTool.d("connectionClosedOnError " + e.getMessage().toString() + "connect is null ? " + (mConnection == null));
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

        initManager();
    }

    private void connect(){
        connect(this.server,this.port);
    }

    private void connect(String server, int port) {

        if (mConnection != null && mConnection.isAuthenticated()) {
            LogTool.d("Authentication is valid now !");
            return;
        }

        this.server = server;
        this.port = port;

        if (mConnection == null)
            initConnection();

        new Thread(new Runnable() {
            @Override
            public void run() {
                justConnect();
            }
        }).start();
    }

    private void justConnect() {
        try {
            if (!mConnection.isConnected() && !mConnection.isAuthenticated())
                mConnection.connect();
        } catch (SmackException | IOException | XMPPException e) {
            LogTool.e(e.getMessage().toString());
            e.printStackTrace();
        }
    }

    // 注册监听网络状态广播
    private void registerNetWorkStateService(){
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mNetWorkStateReceiver =new NetWorkStateReceiver();

        mNetWorkStateReceiver.setOnBindNetWorkStateListener(new NetWorkStateReceiver.OnBindNetWorkStateListener() {
            @Override
            public void getNetWorkState(NETSTATE netstate) {
                pingPongManager.updateNetWorkState(netstate);
            }
        });

        this.registerReceiver(mNetWorkStateReceiver, filter);
    }

    private void initManager() {
        mucManager = new SmackMucManager(this,this, mConnection);
        rosterManager = new SmackRosterManager(this,this, mConnection);
        pingPongManager = new PingPongManager(this,this, mConnection);
        sessionManager = new SmackSessionManager(this, this,mConnection);
        onselfManager = new SmackOnselfManager(this,this, mConnection);

        mManagerUtil = new ManagerUtil();
        mManagerUtil.register(mucManager);
        mManagerUtil.register(rosterManager);
        mManagerUtil.register(pingPongManager);
        mManagerUtil.register(sessionManager);
        mManagerUtil.register(onselfManager);
    }

    private void releaseManager(){

        mManagerUtil.releaseManagers();
        mManagerUtil = null;

        mucManager = null;
        rosterManager = null;
        pingPongManager  = null;
        sessionManager = null;;
        onselfManager = null;

    }

    // 释放服务
    private void releaseManagerService(){

        // 释放心跳服务
        pingPongManager.releaseManager();
        // 释放网络状态监听服务
        this.unregisterReceiver(mNetWorkStateReceiver);
        // 释放manager 资源
        mConnection = null;
        releaseManager();

    }

    // 注册服务
    private void registerManagerService() {
        pingPongManager.registerPongServer();
        pingPongManager.registerCallBack(new PingPongManager.PingPongCallBack() {
            @Override
            public void pingTimeOut() {
                //.. 暂时不做什么，内部已经实现
            }

            @Override
            public void pingNoneAuthenticated() {
                connect();
            }
        });

        registerNetWorkStateService();

        sessionManager.getOfflineMessage();

        //...
    }

}
