package com.wozainali.manho.myapplication.asynctasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.wozainali.manho.myapplication.bus.ZaiNaliBus;
import com.wozainali.manho.myapplication.bus.events.ReadKmlFinishedEvent;
import com.wozainali.manho.myapplication.kml.MyKmlReader;
import com.wozainali.manho.myapplication.kml.Placemarks;
import com.wozainali.manho.myapplication.asynctasks.ReadKmlTask.ReadFilter;

public class ReadKmlTask extends AsyncTask<Void, Void, Placemarks> {

    MyKmlReader myKmlReader;
    int kmlId;
    Resources resources;
    public final ReadFilter readFilter = ReadFilter.ALL;

    // TODO: make this filter more useful
    public enum ReadFilter {
        TITLE_ONLY, ALL;
    }

    public ReadKmlTask (int kmlId, Resources resources) {
        this.kmlId = kmlId;
        this.resources = resources;
    }

    @Override
    protected Placemarks doInBackground(Void... params) {
        myKmlReader = new MyKmlReader();
        Placemarks placemarks = myKmlReader.getPlacemarks(kmlId, resources, readFilter);
        return placemarks;
    }

    @Override
    protected void onPostExecute(Placemarks placemarks) {
        ZaiNaliBus.getBus().post(new ReadKmlFinishedEvent(placemarks));
    }
}
