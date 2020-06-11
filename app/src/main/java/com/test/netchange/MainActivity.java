package com.test.netchange;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.networkchange.NetChangeManager;
import com.github.networkchange.NetChangerListener;
import com.github.networkchange.NetType;

public class MainActivity extends AppCompatActivity {

    private Button bt;
    private Button btText;
    private Button btClearText;
    private TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.bt);
        btText = findViewById(R.id.btText);
        tvText = findViewById(R.id.tvText);
        btClearText = findViewById(R.id.btClearText);
        btClearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvText.setText("");
            }
        });

        /*使用之前注册一次,可以写在application中*/
        NetChangeManager.get().register(this);

        /*添加网络监听*/
        NetChangeManager.get().addNetChangeListener(this, new NetChangerListener() {
            @Override
            public void onConnect(int netType) {
                switch (netType) {
                    case NetType.MOBILE:
                        btText.setText("监听当前网络:GPRS");
                        bt.setText("点击手动获取当前网络");
                        Log.i("=====", "====MainActivity=onConnect==GPRS" + netType);
                        tvText.setText(tvText.getText() + "\nGPRS");
                        break;
                    case NetType.WIFI:
                        btText.setText("监听当前网络:WIFI");
                        bt.setText("点击手动获取当前网络");
                        Log.i("=====", "====MainActivity=onConnect==WIFI" + netType);
                        tvText.setText(tvText.getText() + "\nWIFI");
                        break;
                }
            }

            @Override
            public void onDisConnect() {
                btText.setText("监听当前网络:NONE");
                bt.setText("点击手动获取当前网络");
                Log.i("=====", "====MainActivity=onConnect====NONE");
                tvText.setText(tvText.getText() + "\nNONE");
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentNetType = NetChangeManager.get().getCurrentNetType();
                switch (currentNetType) {
                    case NetType.MOBILE:
                        Log.i("=====", "====MainActivity=getCurrentNetType====GPRS");
                        bt.setText("点击手动获取当前网络(GPRS)");
                        tvText.setText(tvText.getText() + "\nGPRS_click");
                        break;
                    case NetType.WIFI:
                        Log.i("=====", "====MainActivity=getCurrentNetType====WIFI");
                        bt.setText("点击手动获取当前网络(WIFI)");
                        tvText.setText(tvText.getText() + "\nWIFI_click");
                        break;
                    case NetType.NONE:
                        Log.i("=====", "====MainActivity=getCurrentNetType====NONE");
                        bt.setText("点击手动获取当前网络(NONE)");
                        tvText.setText(tvText.getText() + "\nNONE_click");
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
