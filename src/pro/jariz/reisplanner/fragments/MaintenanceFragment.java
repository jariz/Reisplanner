package pro.jariz.reisplanner.fragments;

import com.fima.cardsui.views.*;
import com.fima.cardsui.objects.*;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pro.jariz.reisplanner.R;
import pro.jariz.reisplanner.api.*;

public class MaintenanceFragment extends NSTaskInvokable {
	
	Boolean storingen;
	View thisView;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		SlidingFragmentActivity context = (SlidingFragmentActivity)this.getActivity();
		thisView = inflater.inflate(R.layout.fragment_maintenance, container, false);
		storingen = this.getArguments().getBoolean("storingen", false);
		
		CardUI mCardView = (CardUI) thisView.findViewById(R.id.maintenance);
		CardStack title = new CardStack();
		if(storingen) {
			context.getSupportActionBar().setTitle("Storingen");
			title.setTitle("Storingen");
		} else {
			context.getSupportActionBar().setTitle("Werkzaamheden");
			title.setTitle("Werkzaamheden");
		}
		title.setTitleTypeFace(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-CondensedItalic.ttf"));
		mCardView.addStack(title);
		mCardView.refresh();
		
		return thisView;
	}
}
