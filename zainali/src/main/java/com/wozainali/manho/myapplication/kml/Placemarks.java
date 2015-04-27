package com.wozainali.manho.myapplication.kml;

import java.util.ArrayList;
import java.util.Iterator;

public class Placemarks {

    private ArrayList<Placemark> placemarks = new ArrayList<>();
    private Placemark currentPlacemark;

    public void addCurrentPlacemark() {
        placemarks.add(currentPlacemark);
    }

    public ArrayList<Placemark> getPlacemarks() {
        return placemarks;
    }

    public void setPlacemarks(ArrayList<Placemark> placemarks) {
        this.placemarks = placemarks;
    }

    public Placemark getCurrentPlacemark() {
        return currentPlacemark;
    }

    public void setCurrentPlacemark(Placemark currentPlacemark) {
        this.currentPlacemark = currentPlacemark;
    }
}
