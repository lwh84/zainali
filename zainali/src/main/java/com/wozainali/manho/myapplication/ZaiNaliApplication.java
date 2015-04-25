package com.wozainali.manho.myapplication;

import android.app.Application;
import android.util.Log;

public class ZaiNaliApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("ZaiNaliApplication", "onCreate");

    }
}
