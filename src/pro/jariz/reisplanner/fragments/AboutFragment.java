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
