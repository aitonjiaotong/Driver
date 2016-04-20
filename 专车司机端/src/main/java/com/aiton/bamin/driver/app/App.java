package com.aiton.bamin.driver.app;

import android.app.Application;

import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.mapapi.SDKInitializer;

/**
 * Created by zjb on 2016/4/12.
 */
public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        ApiStoreSDK.init(this, "843f581ec8846f4c643e526f202238ac");
    }
}
