package com.wozainali.manho.myapplication.kml;

import android.content.res.Resources;

import com.wozainali.manho.myapplication.R;
import com.wozainali.manho.myapplication.asynctasks.ReadKmlTask.ReadFilter;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
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
            System.out.println("XML Pasing Excpetion = " + e);
        }



//        catch (ParserConfigurationException e) {
//        } catch (SAXException e) {
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        return placemarks;
    }

}
