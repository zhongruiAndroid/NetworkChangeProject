package com.test.netchange;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.networkchange.NetChangeManager;
import com.github.networkchange.NetChangerListener;
import com.github.networkchange.NetType;
import com.github.networkchange.NetworkCallbackImp;

public class MainActivity extends AppCompatActivity {

    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.bt);
//        init();

        NetChangeManager.get().register(this);

        NetChangeManager.get().addNetChangeListener(this, new NetChangerListener() {
            @Override
            public void onConnect(int netType) {
                Log.i("=====","====MainActivity=onConnect"+netType);
                switch (netType){
                    case NetType.NONE:
                        bt.setText("NONE");
                        break;
                    case NetType.GPRS:
                        bt.setText("GPRS");
                        break;
                    case NetType.WIFI:
                        bt.setText("WIFI");
                        break;
                }
            }
            @Override
            public void onDisConnect() {
                bt.setText("NONE");
                Log.i("=====","====MainActivity=onConnect====NONE");
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TestActivity.class));
            }
        });
    }

    private void init() {
        ConnectivityManager systemService = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            NetworkCallbackImp imp = new NetworkCallbackImp();
            NetworkRequest build = new NetworkRequest.Builder().build();
            systemService.registerNetworkCallback(build,imp);
        }


        /*val cmgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cmgr?.registerDefaultNetworkCallback(networkCallback)
        } else {
            cmgr?.registerNetworkCallback(request, networkCallback)
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetChangeManager.get().unRegister();
    }
}
