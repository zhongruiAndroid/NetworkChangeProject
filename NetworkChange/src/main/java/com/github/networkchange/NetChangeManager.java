package com.github.networkchange;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/***
 *   created by android on 2019/7/9
 */
public class NetChangeManager {
    private static NetChangeManager netChangeManager;

    private Context context;
    private NetStateReceiver netStateReceiver;
    private Map<Object, NetChangerListener> concurrentMap;
    private IntentFilter intentFilter;
    private ConnectivityManager systemService;
    private NetworkCallbackImp imp;
    private AtomicInteger netTypeInteger;

    private NetChangeManager() {
        netTypeInteger = new AtomicInteger();
        initMap();
        netStateReceiver = new NetStateReceiver();
    }

    private void initMap() {
        if (concurrentMap == null) {
            this.concurrentMap = new ConcurrentHashMap<>();
        }
    }

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

    protected Context getContext() {
        return context;
    }

    public void register(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("init(context) context can not null");
        }
        this.context = context;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setInterface(context);
        } else {
            setBroadcast();
        }

    }

    private void setInterface(Context context) {
        if(systemService!=null){
            //防止重复注册
            return;
        }
        systemService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (systemService != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            imp = new NetworkCallbackImp();
            NetworkRequest build = new NetworkRequest.Builder().build();
            systemService.registerNetworkCallback(build, imp);
        } else {
            setBroadcast();
        }
    }

    private void setBroadcast() {
        if (intentFilter == null) {
            intentFilter = new IntentFilter(NetStateReceiver.ANDROID_NET_CHANGE_ACTION);
            context.registerReceiver(netStateReceiver, intentFilter);
        }
    }

    public void unRegister() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            unRegisterInterface();
        } else {
            unRegisterBroadcast();
        }

    }

    private void unRegisterInterface() {
        if (systemService != null && imp != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            systemService.unregisterNetworkCallback(imp);
        } else {
            unRegisterBroadcast();
        }
    }

    private void unRegisterBroadcast() {
        if (context != null && netStateReceiver != null) {
            context.unregisterReceiver(netStateReceiver);
        }
    }

    protected void onReceive() {
        if (NetworkUtils.isNetworkAvailable(getContext())) {
            int netType = NetworkUtils.getNetType(getContext());
            notifyNetChange(netType);
        } else {
            notifyNetChange(NetType.NONE);
        }
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
