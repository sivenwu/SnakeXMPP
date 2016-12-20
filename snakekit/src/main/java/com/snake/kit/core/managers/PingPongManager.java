package com.snake.kit.core.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.snake.api.apptools.LogTool;
import com.snake.kit.core.SnakeService;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.ping.packet.Ping;

/**
 * Created by Yuan on 2016/11/8.
 * Detail 心跳管理机制
 */

public class PingPongManager extends BaseManager{

    // info
    private String mPingID;
    private long mPingTimestamp;//ping时间戳
    public static final int PING_INTERVAL = 30 * 1000; // 心跳时间
    public static final int PACKET_TIMEOUT = 30 * 1000;// 超时时间

    // action
    public static final String ACTION_HEADER = "SNACK";
    public static final String PING_ALARM = ".PING_ALARM";   //  ping服务器闹钟BroadcastReceiver的Action
    public static final String PONG_TIMEOUT_ALARM = ".PONG_TIMEOUT_ALARM";    //  判断连接超时的闹钟BroadcastReceiver的Action

    // PackListener
    private StanzaListener mPongListener;

    // intent
    private PendingIntent mPongTimeoutAlarmPendIntent;
    private PendingIntent mPingAlarmPendIntent;
    private Intent mPingAlarmIntent = new Intent(ACTION_HEADER+PING_ALARM);
    private Intent mPongTimeoutAlarmIntent = new Intent(ACTION_HEADER+PONG_TIMEOUT_ALARM);

    // BroadcastReceiver
    private BroadcastReceiver mPongTimeoutAlarmReceiver = new PongTimeoutAlarmReceiver();
    private BroadcastReceiver mPingAlarmReceiver = new PingAlarmReceiver();

    public PingPongManager(Context context, AbstractXMPPConnection mConnection) {
        super(context, mConnection);
    }

    // 注册心跳服务
    public void registerPongServer(){

        mPingID = null;// 初始化ping的id

        if (mPongListener != null)
            mConnection.removePacketInterceptor(mPongListener);// 先移除之前监听对象

        mPongListener = new StanzaListener() {

            @Override
            public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                if (packet == null)
                    return;

                // 如果服务器返回的消息为ping服务器时的消息，说明没有掉线,然后取消超时闹钟等待下一次启动超时闹钟
                if (packet.getStanzaId().equals(mPingID)) {
                    LogTool.d("alive now ! cacel timoutAlarm!!");
                    mPingID = null;
                    cacelPongAlarmService();
                }
            }
        };

        // 正式开始监听
        mConnection.addPacketInterceptor(mPongListener, new StanzaTypeFilter(
                IQ.class));

        // 闹钟服务intent
        mPingAlarmPendIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), 0, mPingAlarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT); // 确定是否掉线 ping
        mPongTimeoutAlarmPendIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), 0, mPongTimeoutAlarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);// 确定是否超时，跟Ping时间一样

        // 广播服务 intent
        context.registerReceiver(mPingAlarmReceiver, new IntentFilter(
                ACTION_HEADER + PING_ALARM));//ping服务器广播接收者
        context.registerReceiver(mPongTimeoutAlarmReceiver, new IntentFilter(
                ACTION_HEADER + PONG_TIMEOUT_ALARM)); // 注册pong超时的广播接收器

        getPingAlarmService();
        LogTool.d("start timoutAlarm and pingAlarm!!");
    }

    // 发送心跳包
    private void sendPingPoingPackaget(){
        // 此时说明上一次ping服务器还未回应，直接返回，直到连接超时
        if (mPingID != null) {
            LogTool.d("上一次ping服务器还未回应，上次的pingID： " + mPingID);
            return;
        }

        Ping ping = new Ping();
        ping.setType(IQ.Type.get);
        ping.setTo(mConnection.getServiceName());

        // 此id其实是随机生成，但是唯一的
        mPingID = ping.getStanzaId();
        mPingTimestamp = System.currentTimeMillis();
        LogTool.d( "发送ping心跳，ID:" + mPingID);
        // 发送ping消息
        try {
            mConnection.sendStanza(ping);
            getPongAlarmService();//这里启动监听pong超时服务,闹钟时间设置比ping长3秒
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送ping请求的广播接收器
     */
    private class PingAlarmReceiver extends BroadcastReceiver {
        public void onReceive(Context ctx, Intent i) {
            if (mConnection.isAuthenticated()) {
                // 收到ping服务器的闹钟，即ping一下服务器
                LogTool.d("PingAlarmReceiver,ready to send Ping...");
                sendPingPoingPackaget();
            } else {
                LogTool.d("Ping: alarm received, but not auth to server.");
            }
        }
    }

    /**
     * pong超时的广播接收器
     */
    private class PongTimeoutAlarmReceiver extends BroadcastReceiver {
        public void onReceive(Context ctx, Intent i) {
            LogTool.d( "Ping: timeout for " + mPingID);
            // 超时就注销登录
            dealTimeOut();
            ((SnakeService)context).logout();
        }
    }

    // 释放资源
    private void dealTimeOut(){
        cacelPingAlarmService();
        cacelPongAlarmService();

        context.unregisterReceiver(this.mPongTimeoutAlarmReceiver);
        context.unregisterReceiver(this.mPingAlarmReceiver);

        if (mPongListener != null){
            mConnection.removePacketInterceptor(mPongListener);
        }
    }

    //----------------------------------------------------------------------------------------------

    private void getPingAlarmService(){
        // 此时需要启动超时判断的闹钟了，时间间隔为PACKET_TIMEOUT秒
        ((AlarmManager)context.getSystemService(Context.ALARM_SERVICE))
                .setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + PING_INTERVAL,
                        PING_INTERVAL, mPingAlarmPendIntent);
    }

    private void cacelPingAlarmService(){
        ((AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE))
                .cancel(mPongTimeoutAlarmPendIntent);// 取消超时闹钟
    }

    private void getPongAlarmService(){
        // 此时需要启动超时判断的闹钟了，时间间隔为PACKET_TIMEOUT秒
        ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).set(
                AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                        +PACKET_TIMEOUT + 3000, mPongTimeoutAlarmPendIntent);
    }

    private void cacelPongAlarmService(){
        ((AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE))
                .cancel(mPongTimeoutAlarmPendIntent);// 取消超时闹钟
    }
}
