package com.snake.kit.core.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.snake.kit.core.data.bean.NETSTATE;

import static android.telephony.TelephonyManager.NETWORK_TYPE_1xRTT;
import static android.telephony.TelephonyManager.NETWORK_TYPE_CDMA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EDGE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EHRPD;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_0;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_A;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_B;
import static android.telephony.TelephonyManager.NETWORK_TYPE_GPRS;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSDPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPAP;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSUPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_IDEN;
import static android.telephony.TelephonyManager.NETWORK_TYPE_LTE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_UMTS;

/**
 * Created by Yuan on 2016/12/24.
 * Detail 网络状态变化监听
 */


public class NetWorkStateReceiver extends BroadcastReceiver{

    private NETSTATE curNetState;
    private boolean isAvailable;

    OnBindNetWorkStateListener onBindNetWorkStateListener;

    public void setOnBindNetWorkStateListener(OnBindNetWorkStateListener onBindNetWorkStateListener) {
        this.onBindNetWorkStateListener = onBindNetWorkStateListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo =  manager.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isConnectedOrConnecting()) {
            displayState(mNetworkInfo);
        }



    }

    private void displayState(NetworkInfo info){
        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI:
                curNetState = NETSTATE.STRONG;
                break;
            case ConnectivityManager.TYPE_MOBILE:
                switch (info.getSubtype()) {
                    case NETWORK_TYPE_GPRS: //联通2g
                    case NETWORK_TYPE_CDMA: //电信2g
                    case NETWORK_TYPE_EDGE: //移动2g
                    case NETWORK_TYPE_1xRTT:
                    case NETWORK_TYPE_IDEN:
                        curNetState = NETSTATE.WEAK;
                        break;
                    case NETWORK_TYPE_EVDO_A: //电信3g
                    case NETWORK_TYPE_UMTS:
                    case NETWORK_TYPE_EVDO_0:
                    case NETWORK_TYPE_HSDPA:
                    case NETWORK_TYPE_HSUPA:
                    case NETWORK_TYPE_HSPA:
                    case NETWORK_TYPE_EVDO_B:
                    case NETWORK_TYPE_EHRPD:
                    case NETWORK_TYPE_HSPAP:
                        curNetState = NETSTATE.NORMAL;
                        break;
                    case NETWORK_TYPE_LTE:
                        curNetState = NETSTATE.NORMAL;
                        break;
                    default:
                        curNetState = NETSTATE.NORMAL;
                }
                break;
            default:
        }
        if (this.onBindNetWorkStateListener != null){
            this.onBindNetWorkStateListener.getNetWorkState(curNetState);
        }
    }

    public interface OnBindNetWorkStateListener{
        public void getNetWorkState(NETSTATE netstate);
    }
}
