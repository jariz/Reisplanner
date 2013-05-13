package pro.jariz.reisplanner.api;

import java.io.*;
import java.util.List;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import pro.jariz.reisplanner.R.string;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.util.Base64;

class NSAPI {
	public static String[] Stations = { };
	
	public static void Error(Exception e) {
		Log.e("NSAPI", e.toString());
		e.printStackTrace();
	}
}