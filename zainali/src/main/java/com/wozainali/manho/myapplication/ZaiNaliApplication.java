package com.wozainali.manho.myapplication;

import android.app.Application;

import com.squareup.otto.Bus;
import com.wozainali.manho.myapplication.bus.ZaiNaliBus;
import com.wozainali.manho.myapplication.data.PlacemarksManager;

public class ZaiNaliApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Bus bus = ZaiNaliBus.getBus();
        bus.register(PlacemarksManager.getInstance());
    }
}
