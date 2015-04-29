package com.wozainali.manho.myapplication.bus.events;

import com.wozainali.manho.myapplication.kml.Placemarks;

public class ReadKmlFinishedEvent {

    final public Placemarks placemarksWrapper;

    public ReadKmlFinishedEvent(Placemarks placemarksWrapper) {
        this.placemarksWrapper = placemarksWrapper;
    }

}
