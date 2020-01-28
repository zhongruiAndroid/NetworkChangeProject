package com.test.netchange;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.networkchange.NetChangeManager;
import com.github.networkchange.NetChangerListener;
import com.github.networkchange.NetType;

public class MainActivity extends AppCompatActivity {

    private Button bt;
    private Button btText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.bt);
        btText = findViewById(R.id.btText);

        /*使用之前注册一次*/
        NetChangeManager.get().register(this);

        /*添加网络监听*/
        NetChangeManager.get().addNetChangeListener(this, new NetChangerListener() {
            @Override
            public void onConnect(int netType) {
                switch (netType){
                    case NetType.GPRS:
                        btText.setText("监听当前网络:GPRS");
                        Log.i("=====","====MainActivity=onConnect==GPRS"+netType);

                        break;
                    case NetType.WIFI:
                        btText.setText("监听当前网络:WIFI");
                        Log.i("=====","====MainActivity=onConnect==WIFI"+netType);
                        break;
                }
            }
            @Override
            public void onDisConnect() {
                btText.setText("监听当前网络:NONE");
                Log.i("=====","====MainActivity=onConnect====NONE");
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentNetType = NetChangeManager.get().getCurrentNetType();
                switch (currentNetType){
                    case NetType.GPRS:
                        Log.i("=====","====MainActivity=getCurrentNetType====GPRS");
                        bt.setText("点击手动获取当前网络(GPRS)");
                    break;
                    case NetType.WIFI:
                        Log.i("=====","====MainActivity=getCurrentNetType====WIFI");
                        bt.setText("点击手动获取当前网络(WIFI)");
                    break;
                    case NetType.NONE:
                        Log.i("=====","====MainActivity=getCurrentNetType====NONE");
                        bt.setText("点击手动获取当前网络(NONE)");
                    break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*根据需求移除当前监听*/
        NetChangeManager.get().removeNetChangeListener(this);
    }
}
