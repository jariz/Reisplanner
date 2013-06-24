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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.widget.*;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.*;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import pro.jariz.reisplanner.R;
import pro.jariz.reisplanner.SelectActivity;
import pro.jariz.reisplanner.api.*;
import pro.jariz.reisplanner.api.objects.*;
import pro.jariz.reisplanner.cards.StationCard;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton.OnCheckedChangeListener;
import de.keyboardsurfer.android.widget.crouton.*;
import pro.jariz.reisplanner.misc.CroutonStyles;
import pro.jariz.reisplanner.misc.DB;

public class PlannerFragment extends NSTaskInvokable {
	
	@Override
	public void Invoke(Object Result, Integer TaskType) {

        if(Result == null) {
            //Result has returned null, which means somethings wrong.
            //Show feedback to user in form of a Crouton, using NSTask.LastExceptionMessage
            Crouton.showText(this.getActivity(), "Unable to update station list: "+NSTask.LastExceptionMessage, CroutonStyles.errorStyle);
            return;
        }

        Object[] stationso = (Object[])Result;
        NSStation[] stations = Arrays.copyOf(stationso, stationso.length, NSStation[].class);

        if(TaskType == NSTask.TYPE_STATIONS) {
            //update cache time & clear & insert into db
            Time now = new Time(); now.setToNow();
            DB.setLastCacheTime("stations", new BigDecimal(now.toMillis(true)).intValue());
            DB.clearStations();
            DB.insertStations(stations);

            Crouton.showText(this.getActivity(), getResources().getString(R.string.updating_station_list_done), CroutonStyles.successStyle);
        }

        Render(stations, thisView, this.getActivity());
	}

    public boolean isSafe() { //is it safe to build a bundle out of the views?
        AutoCompleteTextView auto = (AutoCompleteTextView)thisView.findViewById(R.id.AutoCompleteTextView1);
        AutoCompleteTextView auto3 = (AutoCompleteTextView)thisView.findViewById(R.id.AutoCompleteTextView3);

        if(auto.getText().length() > 0 && auto3.getText().length() > 0) return true; else return false;
    }

    public Bundle BuildBundle() { //create bundle from views to pass to select activity
        Bundle bundle = new Bundle();
        AutoCompleteTextView auto = (AutoCompleteTextView)thisView.findViewById(R.id.AutoCompleteTextView1);
        AutoCompleteTextView auto2 = (AutoCompleteTextView)thisView.findViewById(R.id.AutoCompleteTextView2);
        AutoCompleteTextView auto3 = (AutoCompleteTextView)thisView.findViewById(R.id.AutoCompleteTextView3);
        CheckBox check = (CheckBox)thisView.findViewById(R.id.extraOptions);
        DatePicker picker = (DatePicker)thisView.findViewById(R.id.datePicker1);
        TimePicker time = (TimePicker)thisView.findViewById(R.id.timePicker1);

        bundle.putString("from", auto.getText().toString());
        bundle.putString("via", auto2.getText().toString());
        bundle.putString("to", auto3.getText().toString());

        if(check.isChecked()) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            bundle.putString("date", format.format(new Date(picker.getYear(), picker.getMonth(), picker.getDayOfMonth(), time.getCurrentHour(), time.getCurrentMinute(), 0)));
        }

        return bundle;
    }

    @Override
    public void onSlideMenu() {

    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        switch (item.getItemId()) {/*
            case android.R.id.home:
                //home button clicked? toggle slidemenu
                ((SlidingFragmentActivity)this.getActivity()).toggle();
                return true;*/
            case R.id.plan:
                Intent select = new Intent(this.getActivity(), SelectActivity.class);
                if(isSafe()) {
                    select.putExtras(BuildBundle());
                    startActivity(select);
                    return true;
                } else {
                    Crouton.showText(getActivity(), getResources().getString(R.string.enter_both_fields), CroutonStyles.errorStyle);
                    return false;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.planner, menu);
    }
	
	View thisView;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		thisView = inflater.inflate(R.layout.fragment_planner, container, false);
		
		final SlidingFragmentActivity context = (SlidingFragmentActivity)this.getActivity();

        //make sure we've got some actionbar buttons
        setHasOptionsMenu(true);

        //context.getSupportMenuInflater().
		
		CheckBox extra = (CheckBox)thisView.findViewById(R.id.extraOptions);
		extra.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				LinearLayout time = (LinearLayout)thisView.findViewById(R.id.time);
				LinearLayout date = (LinearLayout)thisView.findViewById(R.id.date);
				if(isChecked) {
					time.setVisibility(View.VISIBLE);
					date.setVisibility(View.VISIBLE);
					Animation anim = AnimationUtils.loadAnimation(context, R.anim.fadein);
					time.startAnimation(anim);
					date.startAnimation(anim);
				} else {
					time.setVisibility(View.GONE);
					date.setVisibility(View.GONE);
					//Animation anim = AnimationUtils.loadAnimation(context, R.anim.fadeout);
					
					//time.startAnimation(anim);
					//date.startAnimation(anim);
				}
			} });
	
		context.getSupportActionBar().setTitle("Planner");

		Time time = new Time();
		time.setToNow();

        Integer lastCacheTime = DB.getLastCacheTime("stations");
		
		Time difference = new Time();
		difference.set(time.toMillis(true) - lastCacheTime);
		
		//no stations present or cache is outdated? refresh.
		if(DB.getStationCount() == 0  || (lastCacheTime != 0 && difference.monthDay >= 10)) {
			Crouton.showText(context, getResources().getString(R.string.updating_station_list), CroutonStyles.infoStyle);
			
			new NSTask(this).execute(NSTask.TYPE_STATIONS);
		}
		else {
			//cache time!
            new NSTask(this).execute(NSTask.TYPE_STATIONS_DB);
		}
		
		return thisView;
	}
	
	void Render(NSStation[] nsStations, final View x, final Context context) {

		AutoCompleteTextView auto = (AutoCompleteTextView)x.findViewById(R.id.AutoCompleteTextView1);
		AutoCompleteTextView auto2 = (AutoCompleteTextView)x.findViewById(R.id.AutoCompleteTextView2);
		AutoCompleteTextView auto3 = (AutoCompleteTextView)x.findViewById(R.id.AutoCompleteTextView3);

		ArrayList<String> fullnames = new ArrayList<String>();
		CardUI mCardView = (CardUI) x.findViewById(R.id.cardsview);
		mCardView.setSwipeable(true);
		CardStack stack = new CardStack();
		stack.setTitle("Recentelijke stations");
		
		for(Integer i = 0; i < nsStations.length; i++) {
			fullnames.add((nsStations[i]).Namen.Lang);
			if(i < 5)
				stack.add(new StationCard((nsStations[i]).Namen.Lang));
		}
		mCardView.addStack(stack);
		
		//draw time
		auto.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, fullnames));
		auto2.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, fullnames));
		auto3.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, fullnames));
		mCardView.refresh();
		
		//stacktitle, do you even #HOLO?
		stack.setTitleTypeFace(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-ThinItalic.ttf"));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
	}
}
