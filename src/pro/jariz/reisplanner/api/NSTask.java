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

/*
 * NSTask is a asynchronous download class that consists of several different task types for different situations
 */


public class NSTask extends AsyncTask<Integer, Integer, String> {
	
	public NSTask(NSTaskInvokable context) {
		super();
		this.Context = context.getActivity();
		this.Invokeable = context;
	}
	
	//settings
	private static String username = "jarizw@gmail.com";
	private static String password = "neVxL0uUQhrPayMbUBVVVe3dRLN0ZuErSGqvhcoDkj6b0WqSRLbl0A";
	private static String realm = "https://webservices.ns.nl/";
	
	//vars
	private static Context Context;
	private static NSTaskInvokable Invokeable;
	private static int TaskType;
	
	//enums
	public static int TYPE_STATIONS = 1;
	public static int TYPE_AVT = 2;
	
    @Override
    protected String doInBackground(Integer... TaskTypeArr) {
    	if(TaskTypeArr.length > 1) {
    		Log.e("NSAPI", "NSTask only accepts 1 argument");
    		return null;
    	}
    	
    	TaskType = TaskTypeArr[0];
    	
    	String uri = "";
    	switch(TaskType) {
    		case 1: //TYPE_STATIONS
    			uri = "ns-api-stations-v2";
    		break;
    	}
        
    	//execute request
    	//String req = doReq(realm+uri);
    	String req = "TODOREMOVEME";
    	
    	try
    	{
    	//return depending on what task type
    	if(req != null) 
    		switch(TaskType) {
    			case 1: //TYPE_STATIONS
    				String vv = NSHardcoded.HARDCODEDSTATIONS(Context);;
    			return vv;
    		}
    	}
    	catch(Exception z) {
    		NSAPI.Error(z);
    	}
    	return null;
    }
    
    
    protected String doReq(String... uri) {
    	HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
        	HttpGet get = new HttpGet(uri[0]);
        	get.setHeader("Authorization", "Basic "+Base64.encodeToString((username+":"+password).getBytes(), Base64.DEFAULT));
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
        	NSAPI.Error(e);
            return null;
        } catch (IOException e) {
        	NSAPI.Error(e);
        	return null;
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        
        Invokeable.Invoke(result, TaskType);
    }
}