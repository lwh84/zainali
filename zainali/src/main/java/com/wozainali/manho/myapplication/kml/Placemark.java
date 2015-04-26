package com.wozainali.manho.myapplication.kml;

import java.util.ArrayList;

public class Placemark {

    String name, coordinates;
    ArrayList<Coordinate> coordinatesList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public ArrayList<Coordinate> getCoordinatesList() {
        return coordinatesList;
    }

    public void setCoordinatesList(ArrayList<Coordinate> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }
}
