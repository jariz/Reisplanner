package pro.jariz.reisplanner.api;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import pro.jariz.reisplanner.api.objects.*;

import android.util.Log;
import android.util.Xml;


public class NSParser {
	
	//public parser type functions
	
	public static NSStation[] ParseStations(String input) {
		Log.i("NSAPI", "NSParser has started parsing a station list...");
		
		//set up parser
    	XmlPullParser parser = Xml.newPullParser();
    	StringReader reader = new StringReader(input);
    	try {
			parser.setInput(reader);
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			//parser.nextTag();
		} catch (Exception e) {
			NSAPI.Error(e);
		}
		
    	//start parsin'
    	try
    	{
    		ArrayList<NSStation> stationlist = new ArrayList<NSStation>();
    		parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, "Stations");
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, "Station");
            String tname = parser.getName();
			while (parser.next() != XmlPullParser.END_TAG && (tname != null && !tname.equals("Stations"))) {
				NSStation station = new NSStation();
				while (parser.next() != XmlPullParser.END_TAG) {
			        if (parser.getEventType() != XmlPullParser.START_TAG) {
			            continue;
			        }
			        String name = parser.getName();
			        
			        if (name.equals("Code")) {
			            station.Code = readTagText(parser, name);
			        } else if(name.equals("Type")) {
			        	station.Type = readTagText(parser, name);
			        } else if(name.equals("Namen")) {
			        	
			        	//namen start tag
			        	parser.nextTag();
			        	
			        	station.Namen.Kort = readTagText(parser, "Kort");
			        	parser.nextTag();
			        	
			        	station.Namen.Middel = readTagText(parser, "Middel");
			        	parser.nextTag();
			        	station.Namen.Lang = readTagText(parser, "Lang");
			        	parser.nextTag();
			        	
			        	//end on namen end tag (We're gonna next() anyway)
			        } else if(name.equals("Land")) {
			        	station.Land = readTagText(parser, name);
			        } else if(name.equals("UICCode")) {
			        	station.UICCode = Integer.parseInt(readTagText(parser, name));
			        } else if(name.equals("Lat")) {
			        	station.Lat = Double.parseDouble(readTagText(parser,name));
			        } else if(name.equals("Lon")) {
			        	station.Lon = Double.parseDouble(readTagText(parser,name));
			        } else if(name.equals("Synoniemen")) {
			        	ArrayList<String> synoniemen = new ArrayList<String>();
			        	while(parser.nextTag() != parser.END_TAG && !parser.getName().equals("Synoniemen")) {
			        		synoniemen.add(readTagText(parser, "Synoniem"));
			        	}
			        	
			        	station.Synoniemen = new String[synoniemen.size()];
			        	synoniemen.toArray(station.Synoniemen);
			        } else {
			        	Log.w("NSAPI", "Skipping unknown tag '"+name+"'... This shouldn't happen...");
			            skip(parser);
			        }
			    }
				stationlist.add(station);
				parser.nextTag();
				try
				{
					parser.require(XmlPullParser.START_TAG, null, "Station");
				}
				catch(XmlPullParserException z)
				{
					//dirty method but it's the only way I know how.... :(
					break;
				}
			}
			Log.i("NSAPI", "NSParser has successfully parsed "+stationlist.size()+" stations!");
            NSStation[] stationz = new NSStation[stationlist.size()];
			return stationlist.toArray(stationz);
    	}
    	catch(Exception z) {
    		NSAPI.Error(z);
    		return null;
    	}
	}

    public static Object[] ParseDisruptions(String input) {
        Log.i("NSAPI", "NSParser has started parsing a disruptions list...");

        //set up parser
        XmlPullParser parser = Xml.newPullParser();
        StringReader reader = new StringReader(input);
        try {
            parser.setInput(reader);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        } catch (Exception e) {
            NSAPI.Error(e);
        }

        try {
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, "Storingen");

            ArrayList<NSStoring> storings = new ArrayList<NSStoring>();
            storings.addAll(disruptions("Ongepland", parser));
            storings.addAll(disruptions("Gepland", parser));
            NSStoring[] xxx = new NSStoring[storings.size()];
            Log.i("NSAPI", "NSParser has successfully parsed "+storings.size()+" disruptions!");
            return storings.toArray(xxx);


        } catch(Exception z) {
            NSAPI.Error(z);
            return null;
        }
    }


    //You are entering a zone of code that's written while being drunk. Be aware.

    static ArrayList<NSStoring> disruptions(String tag, XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, null, tag);

        ArrayList<NSStoring> storings = new ArrayList<NSStoring>();
        while(parser.nextTag() != XmlPullParser.END_TAG && !parser.getName().equals(tag)) {
            NSStoring storing = new NSStoring();
            parser.require(XmlPullParser.START_TAG, null, "Storing");
            //parser.nextTag();
            while(parser.nextTag() != XmlPullParser.END_TAG && !parser.getName().equals(("Storing"))) {
                String name = parser.getName();
                if(name.equals("id")) storing.id = readTagText(parser,name);
                else if(name.equals("Traject")) storing.Traject = readTagText(parser,name);
                else if(name.equals("Periode")) storing.Periode = readTagText(parser,name);
                else if(name.equals("Reden")) storing.Reden = readTagText(parser,name);
                else if(name.equals("Advies")) storing.Advies = readTagText(parser,name);
                else if(name.equals("Bericht")) {
                    //search for cdata
                    parser.require(parser.START_TAG, null, "Bericht");
                    parser.nextToken();
                    storing.Bericht = parser.getText();
                    parser.next();
                    parser.require(parser.END_TAG, null, "Bericht");
                    //parser.require(parser.END_TAG, null, "Bericht");
                }
                else if(name.equals("Datum")) storing.Datum = readTagText(parser,name);
                else {
                    Log.w("NSAPI", "Skipping unknown tag '"+name+"'... This shouldn't happen...");
                    skip(parser);
                }
            }
            if(tag.equals("Ongepland")) storing.Unplanned = true;
            else storing.Unplanned = false;
            parser.require(XmlPullParser.END_TAG, null, "Storing");
            storings.add(storing);
        }

        return storings;
    }
	
	
	//private helper functions
	
	private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                depth--;
                break;
            case XmlPullParser.START_TAG:
                depth++;
                break;
            }
        }
     }
    
    static String readTagText(XmlPullParser parser, String name) {
    	String result = "";
    	try
    	{
	    	parser.require(XmlPullParser.START_TAG, null, name);
	        if (parser.next() == XmlPullParser.TEXT) {
	            result = parser.getText();
	            parser.nextTag();
	        }
	        parser.require(XmlPullParser.END_TAG, null, name);
    	} catch(Exception z) {
    		NSAPI.Error(z);
    	}
        return result;
    }
	
}