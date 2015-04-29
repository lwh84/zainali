package com.wozainali.manho.myapplication.asynctasks;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;
import com.squareup.otto.Bus;
import com.wozainali.manho.myapplication.bus.ZaiNaliBus;
import com.wozainali.manho.myapplication.bus.events.AddMarkerEvent;
import com.wozainali.manho.myapplication.bus.events.DrawPolygonsEvent;
import com.wozainali.manho.myapplication.bus.events.ZoomToPointEvent;
import com.wozainali.manho.myapplication.data.PlacemarksManager;
import com.wozainali.manho.myapplication.kml.PlaceMarkPolygon;
import com.wozainali.manho.myapplication.kml.Placemark;

import java.util.ArrayList;
import java.util.Scanner;

public class ShowCountryNameAndBorder extends AsyncTask <Void, Void, Placemark> {

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


    public Placemark showCountryNameAndBorder(Placemark placemark) {
        // get polygons Strings
        ArrayList<String> coordinatesList = placemark.getCoordinatesList();

        // convert strings to arraylist of doubles
        convert(coordinatesList, placemark);

        Log.i("showcountry", "placemark = " + placemark);

        // min max lat long

        // get center of country

        // fill placemarkpolygon with info and sent to postexecute
        return placemark;
    }

    public void showCountryNameAndBorder(double longitude, double latitude) {
        Log.i("showcountry", "from geolocation" + longitude + " " + latitude);

        ArrayList<Placemark> placemarks = PlacemarksManager.getInstance().getPlacemarks();

        if (placemarks == null) return;

        // for every placemark check min max lat long
        for (Placemark currentPlacemark : placemarks) {
            convert(currentPlacemark.getCoordinatesList(), currentPlacemark);

            if (longitude > currentPlacemark.getMinLong() &&
                    longitude < currentPlacemark.getMaxLong() &&
                    latitude > currentPlacemark.getMinLat() &&
                    latitude < currentPlacemark.getMaxLat()) {
                Log.i("placemark", "found one");

                if (checkWithPolygon(currentPlacemark, latitude, longitude)) {
                    showCountryNameAndBorder(currentPlacemark);
                    Log.i("placemark", "showcountryand borders!!!");
                } else {
                    Log.i("placemark", "no match");
                }

            }

            tempMinLatitude = 0;
            tempMaxLatitude = 0;
            tempMinLongitude = 0;
            tempMaxLongitude = 0;
        }



        // if still here
        // get polygons


        // when placemark found getname and getpolygons, get min and max lat and long

        // put all needed info in placemarkpolygon and go to postexecute and sent events

    }

    public boolean checkWithPolygon(Placemark placemark, double latitudeToCheck, double longitudeToCheck) {
        for (PlaceMarkPolygon placeMarkPolygon : placemark.getPolygons()) {
            Log.i("placemark", "placemarkpolygon");

//            PolygonOptions polygonOptions = new PolygonOptions();

            ArrayList<LatLng> foundCountryPolygon = new ArrayList<>();

            for (int i = 0 ; i < placeMarkPolygon.getLongitudes().size(); i++) {
                Log.i("polygon", "making the list");
                foundCountryPolygon.add(new LatLng(placeMarkPolygon.getLatitudes().get(i),
                                                placeMarkPolygon.getLongitudes().get(i)));
            }

            return PolyUtil.containsLocation(new LatLng(latitudeToCheck, longitudeToCheck),foundCountryPolygon, true);

        }

        return false;

    }

    @Override
    protected Placemark doInBackground(Void... params) {
        if (placemark != null) {
            showCountryNameAndBorder(placemark);
        } else {
            showCountryNameAndBorder(longitude, latitude);
        }

        return placemark;
    }

    @Override
    protected void onPostExecute(Placemark placemark) {
        if (placemark != null) {
            Bus bus = ZaiNaliBus.getBus();

            // send event to zoom in and animate to point
            bus.post(new ZoomToPointEvent(placemark));

            // send event to addmarker on that point with name
            bus.post(new AddMarkerEvent(placemark));

            // send events to draw polylines
            bus.post(new DrawPolygonsEvent(placemark.getPolygons()));
        }
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
