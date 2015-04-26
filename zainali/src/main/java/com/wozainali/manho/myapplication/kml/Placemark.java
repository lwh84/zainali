package com.wozainali.manho.myapplication.kml;

import java.util.ArrayList;

public class Placemark {

    String name, stringCoords;
    ArrayList<String> coordinates;
    ArrayList<Coordinate> coordinatesList;
    double minLong, maxLong, minLat, maxLat;

    public void addCoordinates(String coordinates) {
        if (this.coordinates == null) this.coordinates = new ArrayList<String>();
        this.coordinates.add(coordinates);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<String> coordinates) {
        this.coordinates = coordinates;
    }

    public ArrayList<Coordinate> getCoordinatesList() {
        return coordinatesList;
    }

    public void setCoordinatesList(ArrayList<Coordinate> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }

    public double getMinLong() {
        return minLong;
    }

    public void setMinLong(double minLong) {
        this.minLong = minLong;
    }

    public double getMaxLong() {
        return maxLong;
    }

    public void setMaxLong(double maxLong) {
        this.maxLong = maxLong;
    }

    public double getMinLat() {
        return minLat;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
    }

    public String getStringCoords() {
        return stringCoords;
    }

    public void setStringCoords(String stringCoords) {
        this.stringCoords = stringCoords;
    }
}
