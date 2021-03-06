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

package pro.jariz.reisplanner;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import pro.jariz.reisplanner.adapters.*;
import pro.jariz.reisplanner.api.NSTaskInvokable;
import pro.jariz.reisplanner.fragments.*;
import pro.jariz.reisplanner.misc.DB;

public class BasePlannerActivity extends SlidingFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//custom animation
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

        //load database
        DB.Init(this);

		//set default activity layout
		setContentView(R.layout.activity_base);
		
		//switch to our default fragment
		switchTo(new PlannerFragment());
		
		//and away we go...
		initSM();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		//custom animation
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

    @Override
    public void onResume() {
        super.onResume();

        //custom animation
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
	
	void switchTo(NSTaskInvokable fragment) {
		try
		{
			findViewById(R.id.fragment_frame).startAnimation(AnimationUtils.loadAnimation(this, R.anim.fadeout));
			
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_frame, fragment)
			.commit();			
			
			findViewById(R.id.fragment_frame).startAnimation(AnimationUtils.loadAnimation(this, R.anim.fadein));
		}
		catch (Exception z)
		{
			Log.e("BASEPLANNER", "Could not start fragment transaction", z);
		}
	}
	
	void initSM() {
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setSlidingActionBarEnabled(true);
		
		//set the actual slide menu
		setBehindContentView(R.layout.slidemenu);
		
		//fill the slide menu
		final ListView stations = (ListView)findViewById(R.id.stations);
		stations.setAdapter(new SlideMenuAdapter(this, new String[] { "Reisplanner", "Vertrektijden" }));
		
		final ListView nieuws = (ListView)findViewById(R.id.nieuws);
		nieuws.setAdapter(new SlideMenuAdapter(this, new String[] { "Storingen", "Werkzaamheden" }));
		
		final ListView applicatie = (ListView)findViewById(R.id.applicatie);
		applicatie.setAdapter(new SlideMenuAdapter(this, new String[] { "Instellingen", "Over" }));
		
		//set up event handlers

        SlidingMenu.OnOpenListener openListener = new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                //hide keyboard if slidemenu is showing (looks ugly if it does)
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null && inputMethodManager.isAcceptingText()) {
                    inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        };
		
		OnItemClickListener clickevent = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> list, View parent, int index,
					long arg3) {
				String name = (String)list.getAdapter().getItem(index);

				toggle();
				
				if(name.equals("Reisplanner")) {
					switchTo(new PlannerFragment());
				} else if(name.equals("Over")) {
					switchTo(new AboutFragment());
				} else if(name.equals("Storingen") || name.equals("Werkzaamheden")) {
					MaintenanceFragment maintenance = new MaintenanceFragment();
					Bundle args = new Bundle();
					if(name.equals("Storingen"))
						args.putBoolean("storingen", true);
					
					maintenance.setArguments(args);
					switchTo(maintenance);
				} else {
					Log.w("BASEPLANNER", "Unrecognized list item selected: "+name);
				}
				
			} 
		};

        getSlidingMenu().setOnOpenListener(openListener);

		applicatie.setOnItemClickListener(clickevent);
		nieuws.setOnItemClickListener(clickevent);
		stations.setOnItemClickListener(clickevent);
	}

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
            case android.R.id.home:
                //home button clicked? toggle slidemenu
                toggle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
		}
	}

}
