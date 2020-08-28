package com.github.networkchange;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import java.util.HashMap;
import java.util.Map;


public class NetworkUtils {


    public static boolean isNetworkAvailable(Context context) {
        int netType = getNetType(context);
        return netType != NetType.NONE;
    }

    public static int getNetType(Context context) {
        if (context == null) {
            return NetType.NONE;
        }
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr == null) {
            return NetType.NONE;
        }
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo mobileNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo != null && wifiNetworkInfo.isConnected()) {
                return NetType.WIFI;
            } else if (mobileNetworkInfo != null && mobileNetworkInfo.isConnected()) {
                return NetType.MOBILE;
            } else {
                return NetType.NONE;
            }
        } else {
            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();
            if (networks==null) {
                return NetType.NONE;
            }
            int size = networks.length;
            //通过循环将网络信息逐个取出来
            Map<Integer, Boolean> map = new HashMap<>();
            for (int i = 0; i < size; i++) {
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                if (networkInfo == null) {
                    continue;
                }
                map.put(networkInfo.getType(), networkInfo.isConnected());
            }
            Boolean wifiState = map.get(ConnectivityManager.TYPE_WIFI);
            Boolean mobileState = map.get(ConnectivityManager.TYPE_MOBILE);
            if ((wifiState != null && wifiState)) {
                return NetType.WIFI;
            } else if ((mobileState != null && mobileState)) {
                return NetType.MOBILE;
            } else {
                return NetType.NONE;
            }
        }
    }
}

