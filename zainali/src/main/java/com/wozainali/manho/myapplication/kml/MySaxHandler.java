package com.wozainali.manho.myapplication.kml;

import android.util.Log;

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

    StringBuffer stringBuffer;

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
            this.inCoordinates = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("Placemark")) {
            this.inPlacemark = false;
            placeMarks.addCurrentPlacemark();
            placeMarks.setCurrentPlacemark(null);
        } else if (localName.equals("name")) {
            this.inName = false;
        } else if (localName.equals("coordinates")) {
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
                String coordinates = stringBuffer.toString().trim();

//                // get list of coordinate objects
//                ArrayList<Coordinate> b = getCoordinatesList(coordinates);
//
//                // put list inside of placemark object

                currentPlacemark.setCoordinates(coordinates);
            }
        }

    }

    private ArrayList<Coordinate> getCoordinatesList(String a) {
        Scanner scanner = new Scanner(a);
        scanner.useDelimiter(" ");
        ArrayList<Coordinate> listOfCoordinates = new ArrayList<>();

        while (scanner.hasNext()) {
            Coordinate coordinate = new Coordinate();

            setCoordinate(coordinate, scanner.next());

            listOfCoordinates.add(coordinate);

//            Log.i("coordinate", "coordinate = " + scanner.next());
        }

        return listOfCoordinates;
    }

    private void setCoordinate(Coordinate coordinate, String coordinatesPair) {
        Scanner scanner = new Scanner(coordinatesPair);
        scanner.useDelimiter(",");
        boolean setLatitude = false; // set longitude first

        while (scanner.hasNext()) {
            if (setLatitude) {
                coordinate.setLatitude(scanner.next());
                setLatitude = false;
            } else {
                coordinate.setLongitude(scanner.next());
                setLatitude = true;
            }
        }


    }
}
