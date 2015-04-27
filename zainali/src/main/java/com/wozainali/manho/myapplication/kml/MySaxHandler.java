package com.wozainali.manho.myapplication.kml;

import android.util.Log;

import com.wozainali.manho.myapplication.asynctasks.ReadKmlTask;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Scanner;

public class MySaxHandler extends DefaultHandler {

    Placemarks placeMarks;
    private boolean inPlacemark;
    private boolean inName;
    private boolean inCoordinates;

    private ReadKmlTask.ReadFilter readFilter;

    StringBuffer stringBuffer;
    String tempString;
    double tempMinLatitude;
    double tempMaxLatitude;
    double tempMinLongitude;
    double tempMaxLongitude;

    public Placemarks getPlacemarksFromParsedData() {
        return placeMarks;
    }

    @Override
    public void startDocument() throws SAXException {
        this.placeMarks = new Placemarks();
    }

    @Override
    public void endDocument() throws SAXException {
        // for now nothing is happening here
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (localName.equals("Placemark")) {
            this.inPlacemark = true;
            placeMarks.setCurrentPlacemark(new Placemark());
        } else if (localName.equals("name")) {
            this.inName = true;
        } else if (localName.equals("coordinates")) {
            stringBuffer = new StringBuffer();
//            tempString = "";
            this.inCoordinates = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("Placemark")) {
            this.inPlacemark = false;
            placeMarks.addCurrentPlacemark();

            Placemark currentPlacemark = placeMarks.getCurrentPlacemark();
            currentPlacemark.setMinLat(tempMinLatitude);
            currentPlacemark.setMaxLat(tempMaxLatitude);
            currentPlacemark.setMinLong(tempMinLongitude);
            currentPlacemark.setMaxLong(tempMaxLongitude);
            placeMarks.setCurrentPlacemark(null);
        } else if (localName.equals("name")) {
            this.inName = false;
        } else if (localName.equals("coordinates")) {
            if (getReadFilter() == ReadKmlTask.ReadFilter.ALL) {
                String foundCoordinates = stringBuffer.toString().trim();
                placeMarks.getCurrentPlacemark().addPolygon(getPlacemarkPolygon(foundCoordinates));
//            placeMarks.getCurrentPlacemark().addCoordinates(stringBuffer.toString().trim());
            }

            this.inCoordinates = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (placeMarks.getCurrentPlacemark() != null) {
            Placemark currentPlacemark = placeMarks.getCurrentPlacemark();
            if (this.inName) {
                currentPlacemark.setName(new String(ch,start,length));
                Log.i("coordinate", "name of country = " + currentPlacemark.getName());
            } else if (this.inCoordinates) {
                stringBuffer.append(ch, start, length);
            }
        }

    }

    private PlaceMarkPolygon getPlacemarkPolygon(String coordinates) {
        // first make objects of all the coordinates and put them in a list
        Scanner scanner = new Scanner(coordinates);
        scanner.useDelimiter(" ");
        ArrayList<Coordinate> listOfCoordinates = new ArrayList<>();
        while (scanner.hasNext()) {
            Coordinate coordinate = new Coordinate();

            setCoordinate(coordinate, scanner.next());

            listOfCoordinates.add(coordinate);
//
//            Log.i("coordinate", "coordinate = " + scanner.next());
        }

        PlaceMarkPolygon placeMarkPolygon = new PlaceMarkPolygon();
        placeMarkPolygon.setCoordinates(listOfCoordinates);

        return placeMarkPolygon;
    }

    private enum CoordinateType {
        LONGITUDE, LATITUDE, ALTITUDE;
    }

    private void setCoordinate(Coordinate coordinate, String coordinatesPair) {
        Scanner scanner = new Scanner(coordinatesPair);
        scanner.useDelimiter(",");
        CoordinateType coordinatetype = CoordinateType.LONGITUDE;

        while (scanner.hasNext()) {
            String currentValue = scanner.next();
            switch (coordinatetype) {
                case LONGITUDE:
                    setMinMaxLongitude(currentValue);
                    coordinate.setLongitude(currentValue);
                    coordinatetype = CoordinateType.LATITUDE;
                    break;
                case LATITUDE:
                    setMinMaxLatitude(currentValue);
                    coordinate.setLatitude(currentValue);
                    coordinatetype = CoordinateType.ALTITUDE;
                    break;
                case ALTITUDE:
                    coordinatetype = CoordinateType.LONGITUDE;
                    break;
            }
        }
    }

    public ReadKmlTask.ReadFilter getReadFilter() {
        return readFilter;
    }

    public void setReadFilter(ReadKmlTask.ReadFilter readFilter) {
        this.readFilter = readFilter;
    }

    public void setMinMaxLatitude(String latitude) {
        double currentValue = 0;
        try {
            currentValue  = Double.valueOf(latitude);
        } catch (Exception e) {
            Log.i("MySaxHandler", "parsing double failed");
        }

        if (currentValue < tempMinLatitude || tempMinLatitude == 0) {
            tempMinLatitude = currentValue;
        }

        if (currentValue > tempMaxLatitude || tempMaxLatitude == 0 ) {
            tempMaxLatitude = currentValue;
        }

    }

    public void setMinMaxLongitude(String longitude) {
        double currentValue = 0;
        try {
            currentValue  = Double.valueOf(longitude);
        } catch (Exception e) {
            Log.i("MySaxHandler", "parsing double failed");
        }
        if (currentValue < tempMinLongitude || tempMinLongitude == 0) {
            tempMinLongitude = currentValue;
        }

        if (currentValue > tempMaxLongitude || tempMaxLongitude == 0 ) {
            tempMaxLongitude = currentValue;
        }
    }
}
