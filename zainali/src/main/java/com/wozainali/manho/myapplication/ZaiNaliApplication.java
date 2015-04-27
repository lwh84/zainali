package com.wozainali.manho.myapplication;

import android.app.Application;
import android.util.Log;

import com.wozainali.manho.myapplication.asynctasks.GetCurrentCountry;
import com.wozainali.manho.myapplication.asynctasks.ReadKmlTask;

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




    }
}
