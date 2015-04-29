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


            placeMarks.setCurrentPlacemark(null);
        } else if (localName.equals("name")) {
            this.inName = false;
        } else if (localName.equals("coordinates")) {
            String foundCoordinates = stringBuffer.toString().trim();
//            placeMarks.getCurrentPlacemark().addPolygon(getPlacemarkPolygon(foundCoordinates));
            placeMarks.getCurrentPlacemark().addCoordinates(stringBuffer.toString().trim());


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



    public ReadKmlTask.ReadFilter getReadFilter() {
        return readFilter;
    }

    public void setReadFilter(ReadKmlTask.ReadFilter readFilter) {
        this.readFilter = readFilter;
    }


}
