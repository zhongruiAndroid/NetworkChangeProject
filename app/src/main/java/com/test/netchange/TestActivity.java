package com.test.netchange;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.networkchange.NetChangeManager;
import com.github.networkchange.NetChangerListener;
import com.github.networkchange.NetType;

public class TestActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tv = findViewById(R.id.tv);

        tv.setText("");

        NetChangeManager.get().addNetChangeListener(this, new NetChangerListener() {
            @Override
            public void onConnect(int netType) {
                Log.i("=====","====TestActivity=onConnect"+netType);
                switch (netType){
                    case NetType.NONE:
                        tv.setText("NONE");
                        break;
                    case NetType.GPRS:
                        tv.setText("GPRS");
                        break;
                    case NetType.WIFI:
                        tv.setText("WIFI");
                        break;
                }
            }
            @Override
            public void onDisConnect() {
                tv.setText("NONE");
                Log.i("=====","====TestActivity=onConnect====NONE");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        NetChangeManager.get().removeNetChangeListener(this);
    }
}
