package com.test.netchange;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.bt);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetChangeManager.get().unRegister();
    }
}
