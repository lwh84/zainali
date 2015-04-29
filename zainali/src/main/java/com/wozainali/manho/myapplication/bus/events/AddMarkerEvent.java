package com.wozainali.manho.myapplication.bus.events;

import com.wozainali.manho.myapplication.kml.Placemark;

public class AddMarkerEvent {

    final public String name;
    final public double latitude, longitude;


    public AddMarkerEvent(Placemark placemark) {
        this.name = placemark.getName();

        // center point
        latitude = (placemark.getMaxLat() + placemark.getMinLat())/2;
        longitude = (placemark.getMaxLong() + placemark.getMinLong())/2;
    }


}
