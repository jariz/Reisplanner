/*
 * Copyright 2013 Jari Zwarts
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package pro.jariz.reisplanner.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import pro.jariz.reisplanner.misc.DB;

/*
 * NSTask is a asynchronous download class that consists of several different task types for different situations
 */


public class NSTask extends AsyncTask<Integer, Integer, Object> {

    public static String LastExceptionMessage = "";

	public NSTask(NSTaskInvokable context) {
		super();
		this.Context = context.getActivity();
		this.Invokeable = context;
	}
	
	//settings
	private static String username = "jarizw@gmail.com";
	private static String password = "neVxL0uUQhrPayMbUBVVVe3dRLN0ZuErSGqvhcoDkj6b0WqSRLbl0A";
    private static String realm = "http://webservices.ns.nl/";
	
	//vars
	private static Context Context;
	private static NSTaskInvokable Invokeable;
	private static int TaskType;
	
	//enums
	public static int TYPE_STATIONS = 1;
    public static int TYPE_STATIONS_DB = 2;
	public static int TYPE_AVT = 3;
    public static int TYPE_STORINGEN = 4;
	
    @Override
    protected Object doInBackground(Integer... TaskTypeArr) {
    	if(TaskTypeArr.length > 1) {
    		Log.e("NSAPI", "NSTask only accepts 1 argument");
            NSTask.LastExceptionMessage = "NSTask only accepts 1 argument";
    		return null;
    	}
    	
    	TaskType = TaskTypeArr[0];
    	
    	String uri = "";
    	switch(TaskType) {
    		case 1: //TYPE_STATIONS
    			uri = "ns-api-stations-v2";
            case 4: //TYPE_STORINGEN
                uri = "ns-api-storingen?actual=true";
    		break;
    	}
        
    	//execute request
        String req = "";
        if(!uri.equals(""))
    	     req = doReq(realm+uri);

        if(req == null)
            //LastExceptionMessage is already set by doReq so we don't need to worry and just return null
            return null;

    	try
    	{
    	    //return depending on what task type
            switch(TaskType) {
                case 1: //TYPE_STATIONS
                    return NSParser.ParseStations(req);
                case 2: //TYPE_STATIONS_DB
                    return DB.fetchAllStations(true);
                case 4: //TYPE_STORINGEN
                    return NSParser.ParseDisruptions(req);
            }
    	}
    	catch(Exception z) {
    		NSAPI.Error(z);
            NSTask.LastExceptionMessage = z.getMessage();
    	}
    	return null;
    }
    
    
    protected String doReq(String... uri) {
    	HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
        	HttpGet get = new HttpGet(uri[0]);
        	get.setHeader("Authorization", "Basic "+Base64.encodeToString((username+":"+password).getBytes(), Base64.NO_WRAP));
            response = httpclient.execute(get);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            NSTask.LastExceptionMessage = e.getMessage();
        	NSAPI.Error(e);
            return null;
        } catch (IOException e) {
            NSTask.LastExceptionMessage = e.getMessage();
        	NSAPI.Error(e);
        	return null;
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        Log.i("NSAPI", "NSTask is finished, invoking " + Invokeable.getClass().getCanonicalName() + " to process the results");
        Invokeable.Invoke(result, TaskType);
    }
}