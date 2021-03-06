package com.wozainali.manho.myapplication.kml;

import android.content.res.Resources;

import com.wozainali.manho.myapplication.asynctasks.ReadKmlTask.ReadFilter;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MyKmlReader {

    public Placemarks getPlacemarks(int id, Resources resources, ReadFilter readFilter) {
        Placemarks placemarks = null;

        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();

            XMLReader xmlReader = saxParser.getXMLReader();

            MySaxHandler mySaxHandler = new MySaxHandler();
            mySaxHandler.setReadFilter(readFilter);
            xmlReader.setContentHandler(mySaxHandler);

            xmlReader.parse(new InputSource(resources.openRawResource(id)));

            placemarks = mySaxHandler.getPlacemarksFromParsedData();

        } catch (Exception e) {
            System.out.println("XML Parsing Exception = " + e);
        }

        return placemarks;
    }

}
