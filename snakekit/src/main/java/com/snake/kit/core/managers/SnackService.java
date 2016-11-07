package com.snake.kit.core.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.snake.kit.apptools.LogTool;
import com.snake.kit.interfaces.XmppCononectListener;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.ping.packet.Ping;

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

    // User
    private String login;
    private String password;
    private String server;
    private int port;

    // listener
    private XmppCononectListener xmppCononectListener;

    // info
    public static final int PING_INTERVAL = 10 * 1000;
    public static final int PACKET_TIMEOUT = 10 * 1000;

    /** ping服务器闹钟BroadcastReceiver的Action */
    public static final String PING_ALARM = ".PING_ALARM";//
    /** 判断连接超时的闹钟BroadcastReceiver的Action */
    public static final String PONG_TIMEOUT_ALARM = ".PONG_TIMEOUT_ALARM";

    private String mPingID;
    private long mPingTimestamp;//ping时间戳
    private PendingIntent mPongTimeoutAlarmPendIntent;
    private PendingIntent mPingAlarmPendIntent;
    private Intent mPingAlarmIntent = new Intent(PING_ALARM);
    private Intent mPongTimeoutAlarmIntent = new Intent(PONG_TIMEOUT_ALARM);


    private BroadcastReceiver mPongTimeoutAlarmReceiver = new PongTimeoutAlarmReceiver();
    private BroadcastReceiver mPingAlarmReceiver = new PingAlarmReceiver();

    // PackListener
    private StanzaListener mPongListener;//

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

    // 连接
    public void connect(String login, String password, String server,int port,XmppCononectListener xmppCononectListener){

        this.xmppCononectListener = xmppCononectListener;
        connect(login,password,server,port);
    }


    public void connect(String login, String password, String server,int port){

        if (mConnection != null && mConnection.isAuthenticated()){
            LogTool.d("Authentication is valid now !");
            return ;
        }

        this.login = login;
        this.password = password;
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
                    .setUsernameAndPassword(this.login,this.password)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)//越过证书
                    .build();
            mConnection = new XMPPTCPConnection(connectionConfiguration);
            mConnection.addConnectionListener(new ConnectionListener() {
                @Override
                public void connected(XMPPConnection connection) {
                    LogTool.d("connected");
                    if (xmppCononectListener != null){
                        xmppCononectListener.connected();
                    }
                    try {
                        mConnection = (AbstractXMPPConnection) connection;
                        mConnection.login();
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    } catch (SmackException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void authenticated(XMPPConnection connection, boolean resumed) {
                    LogTool.d("authenticated");
                    if (xmppCononectListener != null){
                        xmppCononectListener.authenticated();
                    }
                }

                @Override
                public void connectionClosed() {
                    LogTool.d("connectionClosed");
                }

                @Override
                public void connectionClosedOnError(Exception e) {
                    LogTool.d("connectionClosedOnError " + e.getMessage().toString());
                    if (xmppCononectListener != null){
                        xmppCononectListener.onError(e,e.getMessage().toString());
                    }
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
                    if (xmppCononectListener != null){
                        xmppCononectListener.onError(e,e.getMessage().toString());
                    }
                }
            });

            mConnection.connect();
            initManager();

            registerPongListener();
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

    //----------------------------------------------------------------------------------------------
    //--------- SnackService private method---------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    private void initManager(){
        messageManager = new SmackMessageManager(this,mConnection);
        mucManager = new SmackMucManager(this,mConnection);
        rosterManager = new SmackRosterManager(this,mConnection);
    }



    /**
     * 注册处理ping服务器消息
     */
    private void registerPongListener() {
        // reset ping expectation on new connection
        mPingID = null;// 初始化ping的id

        if (mPongListener != null)
            mConnection.removePacketInterceptor(mPongListener);// 先移除之前监听对象

        mPongListener = new StanzaListener() {

            @Override
            public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                if (packet == null)
                    return;

                // 如果服务器返回的消息为ping服务器时的消息，说明没有掉线
                if (packet.getPacketID().equals(mPingID)) {
                    Log.i(TAG,
                            String.format(
                                    "Ping: server latency %1.3fs",
                                    (System.currentTimeMillis() - mPingTimestamp) / 1000.));
                    mPingID = null;
                    ((AlarmManager)
                            getSystemService(Context.ALARM_SERVICE))
                            .cancel(mPongTimeoutAlarmPendIntent);// 取消超时闹钟
                }
            }

        };

        // 正式开始监听
        mConnection.addPacketInterceptor(mPongListener, new PacketTypeFilter(
                IQ.class));
        // 定时ping服务器，以此来确定是否掉线
        mPingAlarmPendIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 0, mPingAlarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // 超时闹钟
        mPongTimeoutAlarmPendIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 0, mPongTimeoutAlarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // 注册定时ping服务器广播接收者
        registerReceiver(mPingAlarmReceiver, new IntentFilter(
                 PING_ALARM));
        // 注册pong超时的广播接收器
        registerReceiver(mPongTimeoutAlarmReceiver, new IntentFilter(
                PONG_TIMEOUT_ALARM));

        // 15分钟ping一次服务器
        ((AlarmManager)getSystemService(Context.ALARM_SERVICE))
                .setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + PING_INTERVAL,
                        PING_INTERVAL, mPingAlarmPendIntent);
    }

    /**
     * 发送ping请求的广播接收器
     */
    private class PingAlarmReceiver extends BroadcastReceiver {
        public void onReceive(Context ctx, Intent i) {
            if (mConnection.isAuthenticated()) {
                // 收到ping服务器的闹钟，即ping一下服务器
                sendServerPing();
            } else {
                Log.d(TAG, "Ping: alarm received, but not auth to server.");
            }
        }
    }

    /**
     * pong超时的广播接收器
     */
    private class PongTimeoutAlarmReceiver extends BroadcastReceiver {
        public void onReceive(Context ctx, Intent i) {
            Log.d(TAG, "Ping: timeout for " + mPingID);
            // 超时就注销登录
//            logout();
        }
    }

    /**
     * 发送ping心跳
     */
    public void sendServerPing() {

        // 此时说明上一次ping服务器还未回应，直接返回，直到连接超时
        if (mPingID != null) {
            Log.d(TAG, "上一次ping服务器还未回应，上次的pingID： " + mPingID);
            return;
        }

        Ping ping = new Ping();
        ping.setType(IQ.Type.get);
        ping.setTo(mConnection.getServiceName());

        // 此id其实是随机生成，但是唯一的
        mPingID = ping.getPacketID();
        mPingTimestamp = System.currentTimeMillis();
        Log.d(TAG, "发送ping心跳，ID:" + mPingID);
        // 发送ping消息
        try {
            mConnection.sendPacket(ping);
            // 此时需要启动超时判断的闹钟了，时间间隔为60+3秒
            ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).set(
                    AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                            + PACKET_TIMEOUT + 3000, mPongTimeoutAlarmPendIntent);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }

    }

}
