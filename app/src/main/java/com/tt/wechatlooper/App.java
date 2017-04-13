package com.tt.wechatlooper;

import android.app.Application;

import com.tt.wechatlooper.util.CrashHandler;

/**
 * Author: Beta-Tan
 * CreateTime: 2017/4/13
 * Description:
 */
public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public WechatVersion version;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        version = WechatVersion.v6_5_7;
        CrashHandler.getInstance().init(this);
    }
}

