package com.wozainali.manho.myapplication.bus.events;

import com.wozainali.manho.myapplication.kml.Placemarks;

public class ReadKmlFinishedEvent {

    private Placemarks placemarks;

    public ReadKmlFinishedEvent(Placemarks placemarks) {
        this.placemarks = placemarks;
    }

    public Placemarks getPlacemarks() {
        return this.placemarks;
    }

}
