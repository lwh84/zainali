package com.wozainali.manho.myapplication;

import android.app.Application;
import android.util.Log;

import com.squareup.otto.Bus;
import com.wozainali.manho.myapplication.asynctasks.GetCurrentCountry;
import com.wozainali.manho.myapplication.asynctasks.ReadKmlTask;
import com.wozainali.manho.myapplication.bus.ZaiNaliBus;
import com.wozainali.manho.myapplication.data.PlacemarksManager;

public class ZaiNaliApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("ZaiNaliApplication", "onCreate");

        // start tasks
//        final int worldData = R.raw.world;
//        ReadKmlTask readKmlTask = new ReadKmlTask(worldData, getResources());
//        readKmlTask.execute();

        GetCurrentCountry getCurrentCountry = new GetCurrentCountry(this);
        getCurrentCountry.execute();
        Bus bus = ZaiNaliBus.getBus();

        bus.register(PlacemarksManager.getInstance());






    }
}
