package com.bluewhaledt.saylove.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Created by rade.chan on 2016/12/17.
 */

public class WifiStatusController extends BroadcastReceiver {

    private Context mContext;
    public static final int NETWORK_WIFI_CONNECTION = 1;
    public static final int NETWORK_MOBILE_CONNECTION = 2;
    public static final int NETWORK_DISCONNECTION = 3;


    public WifiStatusController() {

    }

    public void register(Context context) {
        this.mContext = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(this, filter);
    }

    public void unRegister() {
        if (mContext != null) {
            mContext.unregisterReceiver(this);
        }
    }

    public static int getNetWork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return NETWORK_WIFI_CONNECTION;
                }
                return NETWORK_MOBILE_CONNECTION;
            }
        }
        return NETWORK_DISCONNECTION;
    }

    public static boolean wifiIsEnable(Context context) {
        return getNetWork(context) == NETWORK_WIFI_CONNECTION;
    }

    public static boolean hasNetWork(Context context) {
        return getNetWork(context) != NETWORK_DISCONNECTION;
    }

    public static boolean netWorkIsDisConnection(Context context) {
        return getNetWork(context) == NETWORK_DISCONNECTION;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {      //wifi连接上与否
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {           //wifi 网络连接断开

            } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {       //连接到wifi

            }

        } else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {//wifi打开与否
            int wifiStatus = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
            if (wifiStatus == WifiManager.WIFI_STATE_DISABLED) {      //系统关闭wifi

            } else if (wifiStatus == WifiManager.WIFI_STATE_ENABLED) {  //系统开启wifi

            }
        }
    }
}
