package com.github.networkchange;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

/***
 *   created by zhongrui on 2020/1/28
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImp extends ConnectivityManager.NetworkCallback {
    @Override
    public void onAvailable(Network network) {
        Log.i("=====","111=====onAvailable");
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        Log.i("=====","222=====onCapabilitiesChanged");
        if(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)){
            if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                //wifi
            }else {
                //mobile
            }
        }

      /*  val request = NetworkRequest.Builder().build()
        val cmgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cmgr?.registerDefaultNetworkCallback(networkCallback)
        } else {
            cmgr?.registerNetworkCallback(request, networkCallback)
        }*/
    }

    @Override
    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
        Log.i("=====","333=====onLinkPropertiesChanged");
    }

    @Override
    public void onLosing(Network network, int maxMsToLive) {
        Log.i("=====","444=====onLosing");
    }

    @Override
    public void onLost(Network network) {
        Log.i("=====","555=====onLost");
    }

    @Override
    public void onUnavailable() {
        Log.i("=====","666=====onUnavailable");
    }
}
