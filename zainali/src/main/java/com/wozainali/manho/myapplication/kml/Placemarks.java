package com.wozainali.manho.myapplication.kml;

import java.util.ArrayList;
import java.util.Iterator;

public class Placemarks {

    private ArrayList<Placemark> placemarks = new ArrayList<>();
    private Placemark currentPlacemark;

    // I dont think i need this...
    public String toString() {
        String string= "";
        for (Iterator<Placemark> iterator = placemarks.iterator();iterator.hasNext();) {
            Placemark placemark = iterator.next();
            string += placemark.getName() + "\n" + placemark.getCoordinates() + "\n\n";
        }
        return string;
    }

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
