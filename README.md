# NetworkChangeProject
|  [ ![Download](https://api.bintray.com/packages/zhongrui/mylibrary/NetworkChange/images/download.svg) ](https://11bintray.com/11zhongrui/mylibrary/NetworkC1hange/_latestVersion)  | 最新版本号|
|--------|----|
```gradle
implementation 'com.github:NetworkChange:版本号'
```
### 使用之前注册
```java
/*使用之前注册一次*/
NetChangeManager.get().register(this);
```

### 添加网络监听
```java
/*添加网络监听*/
/*可以添加多个监听,每个监听都会收到回调*/
NetChangeManager.get().addNetChangeListener(this, new NetChangerListener() {
    @Override
    public void onConnect(int netType) {
        switch (netType){
            case NetType.GPRS:
		//数据流量
                break;
            case NetType.WIFI:
	        //wifi
                break;
        }
    }
    @Override
    public void onDisConnect() {
	      //无网络连接
    }
});


/*如果Activity或者fragment销毁移除网络监听*/
/*根据需求移除某个监听*/
NetChangeManager.get().removeNetChangeListener(this);
```

### 手动获取当前网络状态
```java
int currentNetType = NetChangeManager.get().getCurrentNetType();
/*数据流量*/
NetType.GPRS:
/*WIFI*/
NetType.WIFI:
/*无网络*/
NetType.NONE:
```
