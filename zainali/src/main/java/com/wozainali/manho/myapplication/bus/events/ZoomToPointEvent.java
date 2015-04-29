package com.wozainali.manho.myapplication.bus.events;

import com.wozainali.manho.myapplication.kml.Placemark;

public class ZoomToPointEvent {

    double minLat, maxLat, minLong, maxLong;

    public ZoomToPointEvent(Placemark placemark) {
        this.minLat = placemark.getMinLat();
        this.maxLat = placemark.getMaxLat();
        this.minLong = placemark.getMinLong();
        this.maxLong = placemark.getMaxLong();
    }

    public double getMinLat() {
        return minLat;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public double getMinLong() {
        return minLong;
    }

    public double getMaxLong() {
        return maxLong;
    }
}
