package com.github.networkchange;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/***
 *   created by android on 2019/7/9
 */
public class NetChangeManager {
    private static NetChangeManager netChangeManager;
    private Handler handler;

    public static NetChangeManager get() {
        if (netChangeManager == null) {
            synchronized (NetChangeManager.class) {
                if (netChangeManager == null) {
                    netChangeManager = new NetChangeManager();
                }
            }
        }
        return netChangeManager;
    }

    private Context context;
    private NetStateReceiver netStateReceiver;
    private Map<Object, NetChangerListener> concurrentMap;
    private IntentFilter intentFilter;
    private AtomicInteger netTypeInteger;
    private long delayTime=0;

    private NetChangeManager() {
        netTypeInteger = new AtomicInteger(-1);
        initMap();
        netStateReceiver = new NetStateReceiver();
    }

    private void initMap() {
        if (concurrentMap == null) {
            this.concurrentMap = new ConcurrentHashMap<>();
        }
    }

    public void setDelayTime(long delayTimeMillis) {
        if(delayTimeMillis<0){
            delayTimeMillis=0;
        }
        this.delayTime = delayTimeMillis;
    }

    protected Context getContext() {
        return context;
    }

    public void register(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("init(context) context can not null");
        }
        this.context = context;

        setBroadcast();

    }



    private void setBroadcast() {
        if (intentFilter == null) {
            intentFilter = new IntentFilter(NetStateReceiver.ANDROID_NET_CHANGE_ACTION);
            context.registerReceiver(netStateReceiver, intentFilter);
        }
    }

    public void unRegister() {
        unRegisterBroadcast();
    }
    private void unRegisterBroadcast() {
        if (context != null && netStateReceiver != null) {
            context.unregisterReceiver(netStateReceiver);
        }
    }

    protected void onReceive() {
        int netType = NetworkUtils.getNetType(getContext());
        if(delayTime>0&&netType==NetType.NONE){
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int netType = NetworkUtils.getNetType(getContext());
                    notifyNetChange(netType);
                }
            },delayTime);
        }else{
            notifyNetChange(netType);
        }
    }
    private Handler getHandler(){
        if(handler==null){
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }
    public void addNetChangeListener(Object object, NetChangerListener listener) {
        initMap();
        this.concurrentMap.put(object, listener);
    }

    public void removeNetChangeListener(Object object) {
        if (concurrentMap == null) {
            return;
        }
        concurrentMap.remove(object);
    }

    protected void notifyNetChange(int netType) {
        if(netTypeInteger.get()==netType){
            return;
        }
        netTypeInteger.set(netType);
        if (concurrentMap == null) {
            return;
        }
        Collection<NetChangerListener> values = concurrentMap.values();
        for (NetChangerListener listener : values) {
            if (listener == null) {
                continue;
            }
            if (netType != NetType.NONE) {
                listener.onConnect(netType);
            } else {
                listener.onDisConnect();
            }
        }
    }

    public int getCurrentNetType() {
        return netTypeInteger.get();
    }
}
