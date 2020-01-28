package com.github.networkchange;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

/***
 *   created by zhongrui on 2020/1/28
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImp extends ConnectivityManager.NetworkCallback {
    private AtomicInteger atomicInteger = new AtomicInteger();

    public NetworkCallbackImp() {
        this.atomicInteger = new AtomicInteger();
    }

    @Override
    public void onAvailable(Network network) {
        atomicInteger.incrementAndGet();
        NetChangeManager.get().onReceive();
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                //wifi
                Log.i("=====", "NetworkCallbackImp222=====wifi");
            } else {
                //mobile
                Log.i("=====", "NetworkCallbackImp222=====gprs");
            }
        }
    }

    @Override
    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
    }

    @Override
    public void onLosing(Network network, int maxMsToLive) {
    }

    @Override
    public void onLost(Network network) {
        NetChangeManager.get().onReceive();
        int value = atomicInteger.decrementAndGet();
        if (value == 0) {
//            NetChangeManager.get().onReceive();
        } else {

        }
    }

    @Override
    public void onUnavailable() {
    }
}
