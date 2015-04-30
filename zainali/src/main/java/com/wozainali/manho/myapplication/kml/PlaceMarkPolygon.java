package com.wozainali.manho.myapplication.kml;

import java.util.ArrayList;

public class PlaceMarkPolygon {

    private ArrayList<Double> longitudes;
    private ArrayList<Double> latitudes;

    public ArrayList<Double> getLongitudes() {
        return longitudes;
    }

    public void setLongitudes(ArrayList<Double> longitudes) {
        this.longitudes = longitudes;
    }

    public ArrayList<Double> getLatitudes() {
        return latitudes;
    }

    public void setLatitudes(ArrayList<Double> latitudes) {
        this.latitudes = latitudes;
    }

    public void addLongitude(Double longitude) {
        if (longitudes == null) this.longitudes = new ArrayList<>();
        this.longitudes.add(longitude);
    }

    public void addLatitude(Double latitude) {
        if (latitudes == null) this.latitudes = new ArrayList<>();
        this.latitudes.add(latitude);
    }

}
