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

package pro.jariz.reisplanner.fragments;

import com.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import pro.jariz.reisplanner.R;
import pro.jariz.reisplanner.api.NSTaskInvokable;

public class AboutFragment extends NSTaskInvokable {
	
	View thisView;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		thisView = inflater.inflate(R.layout.fragment_about, container, false);
		SlidingFragmentActivity context = (SlidingFragmentActivity)this.getActivity();
		context.getSupportActionBar().setTitle("Over");
		
		ListView software = (ListView)thisView.findViewById(R.id.software);
		software.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, new String[] { "SlidingMenu", "Crouton", "CardsUI", "ActionbarSherlock", "RobotoTextView", "Android Action Bar Style Generator" }));
		
		return thisView;
	}

}
