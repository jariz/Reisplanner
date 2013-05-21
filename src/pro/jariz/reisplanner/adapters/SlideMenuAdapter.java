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

package pro.jariz.reisplanner.adapters;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import pro.jariz.reisplanner.R;

public class SlideMenuAdapter extends ArrayAdapter<String> implements ListAdapter {
	private final Activity context;
	  private final String[] names;

	  static class ViewHolder {
	    public TextView text;
	    public ImageView image;
	  }

	  public SlideMenuAdapter(Activity context, String[] names) {
	    super(context, R.layout.slidemenu, names);
	    this.context = context;
	    this.names = names;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View rowView = convertView;
	    if (rowView == null) {
	      LayoutInflater inflater = context.getLayoutInflater();
	      rowView = inflater.inflate(R.layout.slidemenuitem, null);
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.text = (TextView) rowView.findViewById(R.id.text);
	      viewHolder.image = (ImageView) rowView
	          .findViewById(R.id.stationimage);
	      rowView.setTag(viewHolder);
	    }

	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    String s = names[position];
	    holder.text.setText(s);
	    if (s.startsWith("Windows7") || s.startsWith("iPhone")
	        || s.startsWith("Solaris")) {
	      //holder.image.setImageResource(R.drawable.no);
	    } else {
	      //holder.image.setImageResource(R.drawable.ok);
	    }
	    
	    
	    
	    return rowView;
	  }
}
