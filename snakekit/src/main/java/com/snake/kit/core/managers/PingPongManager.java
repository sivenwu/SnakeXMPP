package com.snake.kit.core.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.text.format.Time;

import com.snake.api.apptools.LogTool;
import com.snake.kit.core.SnakeService;
import com.snake.kit.core.data.bean.NETSTATE;
import com.snake.kit.interfaces.SnakeServiceLetterListener;

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
 *
 * normal and strong state: 4G PING(30) PONG (30 + 10)
 * weak state : 2G PING(40) PONG(40+20)
 *
 */

public class PingPongManager extends BaseManager{

    // info
    private String mPingID;
    private long mPingTimestamp;//ping时间戳
    public static int PING_INTERVAL = 30 * 1000; // 心跳时间
    public static int PACKET_TIMEOUT = 10 * 1000;// 超时时间

    private NETSTATE curNetState = NETSTATE.NORMAL;//心跳一般模式

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

    // alarm
    private AlarmManager pingAlarmManager;
    private AlarmManager pongAlarmManager;

    // callBack
    private PingPongCallBack callBack;

    public PingPongManager(Context context, SnakeServiceLetterListener mLetterListener, AbstractXMPPConnection mConnection) {
        super(context, mLetterListener, mConnection);
    }

    // 更新网络状态改变心跳时间
    public void updateNetWorkState(NETSTATE state){

        if (curNetState.equals(state)) return ;

        // 首先重新连接
//        ((SnakeService)context).logout();
//        ((SnakeService)context).login();

        //首先取消闹钟服务
        cacelPingAlarmService();
        cacelPongAlarmService();

        curNetState = state;
        if (curNetState.equals(NETSTATE.NORMAL) || curNetState.equals(NETSTATE.STRONG)){
            LogTool.d("NORMAL 网络状态切换，心跳机制改变..");
            PING_INTERVAL = 30 * 1000;
            PACKET_TIMEOUT = 10 * 1000;
        }else {
            LogTool.d("WEAK 网络状态切换，心跳机制改变..");
            PING_INTERVAL = 40 * 1000;
            PACKET_TIMEOUT = 20 * 1000;
        }

        // 重新启动Ping服务
        getPingAlarmService();
    }

    public void registerCallBack(PingPongCallBack callBack) {
        this.callBack = callBack;
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

            LogTool.d(getTime());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getPingAlarmService();
            }

            if (mConnection.isAuthenticated()) {
                // 收到ping服务器的闹钟，即ping一下服务器
                LogTool.d("PingAlarmReceiver,ready to send Ping...");
                sendPingPoingPackaget();
            } else {
                LogTool.d("Ping: alarm received, but not auth to server.");
                if (callBack!= null){
                    LogTool.d("try to login now...");
                    callBack.pingNoneAuthenticated();
                }
            }
        }
    }

    /**
     * pong超时的广播接收器
     */
    private class PongTimeoutAlarmReceiver extends BroadcastReceiver {
        public void onReceive(Context ctx, Intent i) {
            LogTool.d("Ping: timeout for " + mPingID);
            // 超时就注销登录
            dealTimeOut();
            ((SnakeService) context).logout();

            if (callBack!= null){
                callBack.pingTimeOut();
            }
        }
    }

    // 释放资源
    private void dealTimeOut(){
//        cacelPingAlarmService();
        cacelPongAlarmService();

//        context.unregisterReceiver(this.mPongTimeoutAlarmReceiver);
//        context.unregisterReceiver(this.mPingAlarmReceiver);
//
//        if (mPongListener != null){
//            mConnection.removePacketInterceptor(mPongListener);
//        }
    }

    //----------------------------------------------------------------------------------------------

    private void getPingAlarmService(){

        pingAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // 此时需要启动超时判断的闹钟了，时间间隔为PACKET_TIMEOUT秒
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {// api 19以上后 闹钟服务将不准确

            pingAlarmManager .setInexactRepeating(AlarmManager.RTC_WAKEUP,
                            System.currentTimeMillis() + PING_INTERVAL,
                            PING_INTERVAL, mPingAlarmPendIntent);

        }else{
            LogTool.d("getPingAlarmService setExact..");
            pingAlarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+PING_INTERVAL,mPingAlarmPendIntent);
        }
    }

    private void cacelPingAlarmService(){
        if (pingAlarmManager == null){
            pingAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        pingAlarmManager
                .cancel(mPingAlarmPendIntent);// 取消超时闹钟
    }

    private void getPongAlarmService(){
        // 此时需要启动超时判断的闹钟了，时间间隔为PACKET_TIMEOUT秒
        pongAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        pongAlarmManager.set(
                AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                        +PACKET_TIMEOUT + PING_INTERVAL, mPongTimeoutAlarmPendIntent);
    }

    private void cacelPongAlarmService(){
        if (pongAlarmManager == null){
            pongAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        pongAlarmManager
                .cancel(mPongTimeoutAlarmPendIntent);// 取消超时闹钟
    }

    // ---------- 回调 ----------------------------------------------------------------------------

    public interface PingPongCallBack{

        // 超时回调
        public void pingTimeOut();

        // ping 闹钟后没有登录响应
        public void pingNoneAuthenticated();
    }

    /**
     * 测试时间 方法
     * @return
     */
    private String getTime(){
        Time time = new Time("GMT+8");
        time.setToNow();
        int year = time.year;
        int month = time.month;
        int day = time.monthDay;
        int minute = time.minute;
        int hour = time.hour;
        int sec = time.second;
        return "当前时间为：" + year +
                "年 " + month +
                "月 " + day +
                "日 " + hour +
                "时 " + minute +
                "分 " + sec +
                "秒";
    }

}
