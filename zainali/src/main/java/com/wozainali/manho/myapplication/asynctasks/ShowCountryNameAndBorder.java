package com.wozainali.manho.myapplication.asynctasks;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.squareup.otto.Bus;
import com.wozainali.manho.myapplication.bus.ZaiNaliBus;
import com.wozainali.manho.myapplication.bus.events.AddMarkerEvent;
import com.wozainali.manho.myapplication.bus.events.DrawPolygonsEvent;
import com.wozainali.manho.myapplication.bus.events.ShowCurrentNumberEvent;
import com.wozainali.manho.myapplication.bus.events.ZoomToPointEvent;
import com.wozainali.manho.myapplication.data.PlacemarksManager;
import com.wozainali.manho.myapplication.data.ZaiNaliLatLng;
import com.wozainali.manho.myapplication.data.ZaiNaliPolygon;
import com.wozainali.manho.myapplication.kml.PlaceMarkPolygon;
import com.wozainali.manho.myapplication.kml.Placemark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ShowCountryNameAndBorder extends AsyncTask <Void, Void, Placemark> {

    private Placemark placemark;
    private double longitude, latitude;
    private Context context;
    private boolean alreadySearching;

    public ShowCountryNameAndBorder(Placemark placemark) {
        this.placemark = placemark;
    }

    public ShowCountryNameAndBorder(double longitude, double latitude, Context context) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.context = context;
    }

    public Placemark showCountryNameAndBorder(Placemark placemark) {
        // get polygon strings to convert
        ArrayList<String> coordinatesList = placemark.getCoordinatesList();

        // convert strings to arraylist of doubles
        convert(coordinatesList, placemark);

        // fill placemarkpolygon with info and sent to postexecute
        return placemark;
    }

    public void showCountryNameAndBorder(double longitude, double latitude) {
        if (alreadySearching) return;
        alreadySearching = true;
        // get the list of placemarks
        ArrayList<Placemark> placemarks = PlacemarksManager.getInstance().getPlacemarks();

        if (placemarks == null) return;
        boolean countryFound = false;

        // counter for displaying progress
        int currentNumber = 0;

        for (Placemark placemarkToCheck : placemarks) {
            // check min and max lat and long values, to filter out most of the placemarks
            convert(placemarkToCheck.getCoordinatesList(), placemarkToCheck);

            if (longitude > placemarkToCheck.getMinLong() &&
                    longitude < placemarkToCheck.getMaxLong() &&
                    latitude > placemarkToCheck.getMinLat() &&
                    latitude < placemarkToCheck.getMaxLat()) {
                 countryFound = checkPointInPolygon(placemarkToCheck, latitude, longitude);
                if (countryFound) {
                    ShowCountryNameAndBorder showCountryNameAndBorder = new ShowCountryNameAndBorder(placemarkToCheck);
                    showCountryNameAndBorder.execute();
                    alreadySearching = false;
                    break;

                }


            }
            ZaiNaliBus.getBus().post(new ShowCurrentNumberEvent(currentNumber++));




        }
        alreadySearching = false;
    }

    /**
     * Bad method for finding Country, I was using this for testing purposes
     */
    public void findCountryByNameOfGeoLocation(double latitude, double longitude, ArrayList<Placemark> placemarks) {
        Geocoder geocoder = new Geocoder(this.context, Locale.US);
        List<Address> addresses = null;
        String countryName = "";

        try {
            addresses = geocoder.getFromLocation(latitude,longitude,1);
        } catch (IOException e) {
        }

        if (addresses.size() > 0) {
            System.out.println(addresses.get(0).getLocality());
            countryName = addresses.get(0).getCountryName();
        }

        for (Placemark placemark : placemarks) {
            if (placemark.getName().contains(countryName)) {
                ShowCountryNameAndBorder showCountryNameAndBorder = new ShowCountryNameAndBorder(placemark);
                showCountryNameAndBorder.execute();
            }
        }
    }

    public boolean checkPointInPolygon(Placemark placemark, double latitudeToCheck, double longitudetoCheck) {
        boolean test = false;
        ArrayList<ZaiNaliLatLng> listOfLatLngs = new ArrayList<>();

        ZaiNaliPolygon zaiNaliPolygon = new ZaiNaliPolygon();

        for (PlaceMarkPolygon placeMarkPolygon : placemark.getPolygons()) {

            for (int i = 0; i < placeMarkPolygon.getLongitudes().size(); i++) {
                Log.i("polygon", "making the list");
                listOfLatLngs.add(new ZaiNaliLatLng(placeMarkPolygon.getLatitudes().get(i),
                        placeMarkPolygon.getLongitudes().get(i)));
            }

            test = zaiNaliPolygon.PointIsInRegion(latitudeToCheck,longitudetoCheck,listOfLatLngs);
            Log.i("boolean", "test = " + test);
        }

        return test;
    }

    public boolean checkWithPolygon(Placemark placemark, double latitudeToCheck, double longitudeToCheck) {
//        tempMinLatitude = 0;
//        tempMaxLatitude = 0;
//        tempMinLongitude = 0;
//        tempMaxLongitude = 0;
//        convert(placemark.getCoordinatesList(), placemark);
//
//        ArrayList<ZaiNaliLatLng> listOfLatLngs = new ArrayList<>();
//
//        ZaiNaliPolygon zaiNaliPolygon = new ZaiNaliPolygon();
//
//
//        for (PlaceMarkPolygon placeMarkPolygon : placemark.getPolygons()) {
//
//            for (int i = 0; i < placeMarkPolygon.getLongitudes().size(); i++) {
//                Log.i("polygon", "making the list");
//                listOfLatLngs.add(new ZaiNaliLatLng(placeMarkPolygon.getLatitudes().get(i),
//                        placeMarkPolygon.getLongitudes().get(i)));
//            }
//
//            boolean test = zaiNaliPolygon.PointIsInRegion(latitude,longitude,listOfLatLngs);
//            Log.i("boolean", "test = " + test);
//        }
//
//
//
//        for (PlaceMarkPolygon placeMarkPolygon : placemark.getPolygons()) {
//            Log.i("placemark", "placemarkpolygon");
//
////            PolygonOptions polygonOptions = new PolygonOptions();
//
//            ArrayList<LatLng> foundCountryPolygon = new ArrayList<>();
//
//            for (int i = 0 ; i < placeMarkPolygon.getLongitudes().size(); i++) {
//                Log.i("polygon", "making the list");
//                foundCountryPolygon.add(new LatLng(placeMarkPolygon.getLatitudes().get(i),
//                                                placeMarkPolygon.getLongitudes().get(i)));
//            }
//
//            return PolyUtil.containsLocation(new LatLng(latitudeToCheck, longitudeToCheck),foundCountryPolygon, true);
//
//        }

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
        // converts the string to arraylist of doubles
        for (String coordinates : coordinatesList) {
            placemark.addPolygon(getPlacemarkPolygon(coordinates));
        }

        setMinMaxOfPlacemark(placemark);

    }

    public void setMinMaxOfPlacemark(Placemark placemark) {
        // determine the min max
        ArrayList<Double> latitudesOfPlacemark = new ArrayList<>();
        ArrayList<Double> longitudesOfPlacemark = new ArrayList<>();

        for (PlaceMarkPolygon currentPolygonList : placemark.getPolygons()) {
            latitudesOfPlacemark.addAll(currentPolygonList.getLatitudes());
            longitudesOfPlacemark.addAll(currentPolygonList.getLongitudes());
        }

        placemark.setMinLat(Collections.min(latitudesOfPlacemark));
        placemark.setMaxLat(Collections.max(latitudesOfPlacemark));
        placemark.setMinLong(Collections.min(longitudesOfPlacemark));
        placemark.setMaxLong(Collections.max(longitudesOfPlacemark));
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
//                    setMinMaxLongitude(dCurrentValue);
                    placeMarkPolygon.addLongitude(dCurrentValue);
                    coordinatetype = CoordinateType.LATITUDE;
                    break;
                case LATITUDE:
//                    setMinMaxLatitude(dCurrentValue);
                    placeMarkPolygon.addLatitude(dCurrentValue);
                    coordinatetype = CoordinateType.ALTITUDE;
                    break;
                case ALTITUDE:
                    coordinatetype = CoordinateType.LONGITUDE;
                    break;
            }
        }
    }

//    public void setMinMaxLatitude(double currentValue) {
//        if (currentValue < tempMinLatitude || tempMinLatitude == 0) {
//            tempMinLatitude = currentValue;
//        }
//
//        if (currentValue > tempMaxLatitude || tempMaxLatitude == 0 ) {
//            tempMaxLatitude = currentValue;
//        }
//    }
//
//    public void setMinMaxLongitude(double currentValue) {
//        if (currentValue < tempMinLongitude || tempMinLongitude == 0) {
//            tempMinLongitude = currentValue;
//        }
//
//        if (currentValue > tempMaxLongitude || tempMaxLongitude == 0 ) {
//            tempMaxLongitude = currentValue;
//        }
//    }


}
