package com.wozainali.manho.myapplication.data;

import android.location.Location;

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




}
