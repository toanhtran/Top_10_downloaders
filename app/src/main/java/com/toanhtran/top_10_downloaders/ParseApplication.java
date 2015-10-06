package com.toanhtran.top_10_downloaders;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by toanhtran on 10/6/15.
 */
public class ParseApplication {
    private static ArrayList<Application> applications;
    private String xmlData;
    private ArrayList<Application> applications;//store entries in an array

    public ParseApplication(String xmlData) {
        this.xmlData = this.xmlData;
        applications = new ArrayList<>();//instance of ArrayList
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }

    public static boolean process() {
        boolean status = true;
        Application currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(this.xmlData));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch(eventType){
                    case XmlPullParser.START_TAG:
                        if(tagName.equalsIgnoreCase("entry")){
                            inEntry = true;
                            currentRecord = new Application();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                    case XmlPullParser.END_TAG:
                        if(inEntry){
                            if(tagName.equalsIgnoreCase("entry")){
                                applications.add(currentRecord);
                                inEntry = false;
                            } else if(tagName.equalsIgnoreCase("name")){
                                currentRecord.setName(textValue);
                            } else if (tagName.equalsIgnoreCase("artist")){
                                currentRecord.setArtist(textValue);
                            } else if (tagName.equalsIgnoreCase("releaseDate")){
                                currentRecord.setReleaseDate(textValue);
                            }
                        }
                        break;
                    default:
                        //nothing else to do

                }
                eventType = xpp.next();
            }



        } catch (Exception e){
            status = false;
            e.printStackTrace();//if there is problem catch and log error

        }for(Application app: applications) {
            Log.d("ParseApplications", "***********");
            Log.d("ParseApplications", "Name: " + app.getName());
            Log.d("ParseApplications", "Name: " + app.getArtist());
            Log.d("ParseApplications", "Name: " + app.getReleaseDate());
        }
        return true;
    }
}
