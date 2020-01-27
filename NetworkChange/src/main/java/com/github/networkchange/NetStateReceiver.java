package com.github.networkchange;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;


/***
 *   created by android on 2019/7/9
 */
public class NetStateReceiver extends BroadcastReceiver {
    public static final String ANDROID_NET_CHANGE_ACTION = ConnectivityManager.CONNECTIVITY_ACTION;

    public NetStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null || intent.getAction() == null) {
            return;
        }
        if (ANDROID_NET_CHANGE_ACTION.equalsIgnoreCase(intent.getAction())) {
            Log.i("=====","=====onReceive");
            NetChangeManager.get().onReceive();
            return;
        }

    }


}
