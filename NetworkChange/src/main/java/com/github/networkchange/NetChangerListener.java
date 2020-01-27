package com.github.networkchange;

/***
 *   created by android on 2019/7/9
 */
public interface NetChangerListener {
    void onConnect(int netType);
    void onDisConnect();
}
