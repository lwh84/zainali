package com.wozainali.manho.myapplication.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.wozainali.manho.myapplication.kml.PlaceMarkPolygon;
import com.wozainali.manho.myapplication.kml.Placemark;

import java.util.ArrayList;
import java.util.Scanner;

public class ShowCountryNameAndBorder extends AsyncTask <Void, Void, PlaceMarkPolygon> {

    private Placemark placemark;
    private double longitude, latitude;
    private double tempMinLatitude, tempMaxLatitude, tempMinLongitude, tempMaxLongitude;

    public ShowCountryNameAndBorder(Placemark placemark) {
        this.placemark = placemark;
    }

    public ShowCountryNameAndBorder(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public void showCountryNameAndBorder(Placemark placemark) {
        // get polygons Strings
        ArrayList<String> coordinatesList = placemark.getCoordinatesList();

        // convert strings to arraylist of doubles
        convert(coordinatesList, placemark);

        Log.i("showcountry", "placemark = " + placemark);

        // min max lat long

        // get center of country

        // fill placemarkpolygon with info and sent to postexecute
    }

    public void showCountryNameAndBorder(double longitude, double latitude) {

        // get the list of countries

        // for every placemark check min max lat long

        // if longitude < min long return
        // if longitude > max long return
        // if latitude < min lat return
        // if longitude > max lat return
        // if still here
        // get polygons
        // for every polygon, make polygon object Double.
        // Path2D path = new Path2D.Double();
//        path.moveTo(valoresX[0], valoresY[0]);
//        for(int i = 1; i < valoresX.length; ++i) {
//            path.lineTo(valoresX[i], valoresY[i]);
//        }
//        path.closePath();
        //
        // path.contains(points) then check point in polygon.

        // when placemark found getname and getpolygons, get min and max lat and long

        // put all needed info in placemarkpolygon and go to postexecute and sent events

    }

    @Override
    protected PlaceMarkPolygon doInBackground(Void... params) {
        if (placemark != null) {
            showCountryNameAndBorder(placemark);
        } else {
            showCountryNameAndBorder(longitude, latitude);
        }

        return null;
    }

    @Override
    protected void onPostExecute(PlaceMarkPolygon placeMarkPolygon) {
        // send event to zoom in and animate to point

        // send event to addmarker on that point with name

        // send events to draw polylines
    }

    public void convert(ArrayList<String> coordinatesList, Placemark placemark) {
        for (String coordinates : coordinatesList) {
            placemark.addPolygon(getPlacemarkPolygon(coordinates));
        }

        placemark.setMinLat(tempMinLatitude);
        placemark.setMaxLat(tempMaxLatitude);
        placemark.setMinLong(tempMinLongitude);
        placemark.setMaxLong(tempMaxLongitude);
    }

    private PlaceMarkPolygon getPlacemarkPolygon(String coordinates) {
        // first make objects of all the coordinates and put them in a list
        Scanner scanner = new Scanner(coordinates);
        scanner.useDelimiter(" ");
        PlaceMarkPolygon placeMarkPolygon = new PlaceMarkPolygon();

        while (scanner.hasNext()) {
            setCoordinate(placeMarkPolygon, scanner.next());
        }

        return placeMarkPolygon;
    }

    private enum CoordinateType {
        LONGITUDE, LATITUDE, ALTITUDE;
    }

    private void setCoordinate(PlaceMarkPolygon placeMarkPolygon, String coordinatesPair) {
        Scanner scanner = new Scanner(coordinatesPair);
        scanner.useDelimiter(",");
        CoordinateType coordinatetype = CoordinateType.LONGITUDE;

        while (scanner.hasNext()) {

            String sCurrentValue = scanner.next();
            double dCurrentValue = 0;
            try {
                dCurrentValue  = Double.valueOf(sCurrentValue);
            } catch (Exception e) {
                Log.i("MySaxHandler", "parsing double failed");
            }

            switch (coordinatetype) {
                case LONGITUDE:
                    setMinMaxLongitude(dCurrentValue);
                    placeMarkPolygon.addLongitude(dCurrentValue);
                    coordinatetype = CoordinateType.LATITUDE;
                    break;
                case LATITUDE:
                    setMinMaxLatitude(dCurrentValue);
                    placeMarkPolygon.addLatitude(dCurrentValue);
                    coordinatetype = CoordinateType.ALTITUDE;
                    break;
                case ALTITUDE:
                    coordinatetype = CoordinateType.LONGITUDE;
                    break;
            }
        }
    }

    public void setMinMaxLatitude(double currentValue) {
        if (currentValue < tempMinLatitude || tempMinLatitude == 0) {
            tempMinLatitude = currentValue;
        }

        if (currentValue > tempMaxLatitude || tempMaxLatitude == 0 ) {
            tempMaxLatitude = currentValue;
        }
    }

    public void setMinMaxLongitude(double currentValue) {
        if (currentValue < tempMinLongitude || tempMinLongitude == 0) {
            tempMinLongitude = currentValue;
        }

        if (currentValue > tempMaxLongitude || tempMaxLongitude == 0 ) {
            tempMaxLongitude = currentValue;
        }
    }


}
