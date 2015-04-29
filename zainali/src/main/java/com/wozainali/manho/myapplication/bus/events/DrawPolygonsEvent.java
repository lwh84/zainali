package com.wozainali.manho.myapplication.bus.events;

import com.google.android.gms.location.places.Place;
import com.wozainali.manho.myapplication.kml.PlaceMarkPolygon;
import com.wozainali.manho.myapplication.kml.Placemark;

import java.util.ArrayList;

public class DrawPolygonsEvent {

    final public ArrayList<PlaceMarkPolygon> polygons;

    public DrawPolygonsEvent(ArrayList<PlaceMarkPolygon> polygons) {
        this.polygons = polygons;
    }
}
