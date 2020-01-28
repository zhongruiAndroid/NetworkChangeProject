package com.github.networkchange;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.support.annotation.RequiresApi;

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
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                //wifi
                NetChangeManager.get().notifyNetChange(NetType.WIFI);
            } else if(atomicInteger.get()==1) {
                //mobile
                //如果wifi没有在数据之前开启，则提示
                NetChangeManager.get().notifyNetChange(NetType.GPRS);
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
        int value = atomicInteger.decrementAndGet();
        /*如果数据流量和wifi都断开*/
        if (value == 0) {
            NetChangeManager.get().notifyNetChange(NetType.NONE);
        } else if (value == 1 && NetworkUtils.getNetType(NetChangeManager.get().getContext()) != NetType.WIFI) {
            /*检查wifi是否连接，如果没有，则断开的wifi*/
            NetChangeManager.get().notifyNetChange(NetType.GPRS);
        } else {
//            NetChangeManager.get().notifyNetChange(NetType.WIFI);
        }
    }

    @Override
    public void onUnavailable() {
    }
}
