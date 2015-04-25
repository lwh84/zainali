package com.wozainali.manho.myapplication.kml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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
            } else if (this.inCoordinates) {
                stringBuffer.append(ch,start,length);
                currentPlacemark.setCoordinates(stringBuffer.toString());
            }
        }

    }
}
