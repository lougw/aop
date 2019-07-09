package com.lougw.app;

import android.app.Application;

import com.lougw.aop.AOPUtil;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AOPUtil.getInstance().init(this);
    }
}
