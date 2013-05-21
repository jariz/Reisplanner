package pro.jariz.reisplanner.fragments;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.*;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import pro.jariz.reisplanner.R;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
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
	
	View thisView;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		thisView = inflater.inflate(R.layout.fragment_planner, container, false);
		
		final SlidingFragmentActivity context = (SlidingFragmentActivity)this.getActivity();
		
		CheckBox extra = (CheckBox)thisView.findViewById(R.id.extraOptions);
		extra.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				LinearLayout time = (LinearLayout)thisView.findViewById(R.id.time);
				LinearLayout date = (LinearLayout)thisView.findViewById(R.id.date);
				if(isChecked) {
					time.setVisibility(View.VISIBLE);
					date.setVisibility(View.VISIBLE);
					Animation anim = AnimationUtils.loadAnimation(context, R.anim.push_right_in);
					time.startAnimation(anim);
					date.startAnimation(anim);
				} else {
					time.setVisibility(View.GONE);
					date.setVisibility(View.GONE);
					Animation anim = AnimationUtils.loadAnimation(context, R.anim.push_right_out);
					
					time.startAnimation(anim);
					date.startAnimation(anim);
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
