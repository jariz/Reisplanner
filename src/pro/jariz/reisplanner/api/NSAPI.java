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