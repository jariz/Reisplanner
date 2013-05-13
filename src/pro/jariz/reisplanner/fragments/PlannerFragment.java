package pro.jariz.reisplanner.fragments;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;

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
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.util.Log;
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
public class PlannerFragment extends NSTaskInvokable {
	
	Style infoStyle = new Style.Builder()
	.setBackgroundColor(R.color.crouton_info)
	.setTextColor(android.R.color.white)
	.build();
	
	Style successStyle = new Style.Builder()
	.setBackgroundColor(R.color.crouton_success)
	.setTextColor(android.R.color.white)
	.build();
	
	Style errorStyle = new Style.Builder()
	.setBackgroundColor(R.color.crouton_success)
	.setTextColor(android.R.color.white)
	.build();
	
	@Override
	public void Invoke(String Result, Integer TaskType) {
		
		File stationfile = new File(this.getActivity().getCacheDir()+"/Stations.xml");
		
		FileWriter fw;
		try {
			if(stationfile.exists())
				stationfile.delete();
			
			fw = new FileWriter(stationfile);
			fw.write(Result.toCharArray());
			fw.close();
		} catch (IOException e) {
			Crouton.showText(this.getActivity(), "Unable to write to station cache file, "+e.getMessage(), errorStyle);
			return; 
		}
		
		parseAndRender(Result, thisView, this.getActivity());
		Crouton.showText(this.getActivity(), getResources().getString(R.string.updating_station_list_done), successStyle);
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
		
		File cache = context.getCacheDir();
		
		File stationfile = new File(cache.getAbsoluteFile()+"/Stations.xml");
		
		Time time = new Time();
		time.setToNow();
		
		Time difference = new Time();
		difference.set(time.toMillis(true) - stationfile.lastModified());
		
		//file doesn't exist or file is 10 days old? refresh.
		if(!stationfile.exists() || (stationfile.lastModified() != 0 && difference.monthDay >= 10)) {
			Crouton.showText(context, getResources().getString(R.string.updating_station_list), infoStyle);
			
			final NSTask task = new NSTask(this);
			task.execute(NSTask.TYPE_STATIONS);			
		}
		else {
			
			//cache time!
			char[] buf = new char[(int) stationfile.length()];
			try {
				FileReader reader = new FileReader(stationfile);
				reader.read(buf);
				reader.close();
			} catch (Exception e) {
				Log.wtf("Planner", "Unable to read station xml file from cache AFTER checking for it's existance..?");
				e.printStackTrace();
				return thisView;
			}
			
			parseAndRender(new String(buf), thisView, context);
		}
		
		return thisView;
	}
	
	void parseAndRender(String xml, final View x, final Context context) {
		Object[] Stations = NSParser.ParseStations(xml);
		AutoCompleteTextView auto = (AutoCompleteTextView)x.findViewById(R.id.AutoCompleteTextView1);
		AutoCompleteTextView auto2 = (AutoCompleteTextView)x.findViewById(R.id.AutoCompleteTextView2);
		AutoCompleteTextView auto3 = (AutoCompleteTextView)x.findViewById(R.id.AutoCompleteTextView3);
		ArrayList<String> fullnames = new ArrayList<String>();
		CardUI mCardView = (CardUI) x.findViewById(R.id.cardsview);
		mCardView.setSwipeable(true);
		CardStack stack = new CardStack();
		stack.setTitle("Recentelijke stations");
		
		for(Integer i = 0; i < Stations.length; i++) {
			fullnames.add(((NSStation)Stations[i]).Namen.Lang);
			if(i < 5)
				stack.add(new StationCard(((NSStation)Stations[i]).Namen.Lang));
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
