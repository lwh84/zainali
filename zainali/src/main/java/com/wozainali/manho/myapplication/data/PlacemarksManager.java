package com.wozainali.manho.myapplication.data;

import android.location.Location;

import com.squareup.otto.Subscribe;
import com.wozainali.manho.myapplication.bus.events.ReadKmlFinishedEvent;
import com.wozainali.manho.myapplication.kml.Placemark;

import java.util.ArrayList;

/**
 * This class will hold my location and placemark results, so they can be accessed anywhere,
 * if null then sent a task to fetch
 */
public class PlacemarksManager {

    private static PlacemarksManager placemarksManager = new PlacemarksManager();

    private Location currentLocation;
    private ArrayList<String> countryList; // only used by the adapter
    private ArrayList<Placemark> placemarks;

    public static PlacemarksManager getInstance() {
        return placemarksManager;
    }

    @Subscribe
    public void onReadKmlFinished(ReadKmlFinishedEvent event) {
        placemarks = event.getPlacemarksWrapper().getPlacemarks();
    }

    public ArrayList<Placemark> getPlacemarks() {
        return placemarks;
    }

}
