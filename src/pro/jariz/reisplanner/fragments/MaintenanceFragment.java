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
import pro.jariz.reisplanner.api.objects.NSStation;
import pro.jariz.reisplanner.api.objects.NSStoring;
import pro.jariz.reisplanner.cards.DisruptionCard;

import java.util.Arrays;

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

            new NSTask(this).execute(NSTask.TYPE_STORINGEN);
		}
		title.setTitleTypeFace(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-CondensedItalic.ttf"));
		mCardView.addStack(title);
		mCardView.refresh();
		
		return thisView;
	}

    @Override
    public void Invoke(Object Result, Integer TaskType) {
        Object[] objects = (Object[])Result;
        NSStoring[] storings = Arrays.copyOf(objects, objects.length, NSStoring[].class);
        CardUI mCardView = (CardUI) thisView.findViewById(R.id.maintenance);
        for(int i=0;i < storings.length; i++) {
            DisruptionCard card = new DisruptionCard(storings[i]);
            mCardView.addCard(card);
        }
        mCardView.refresh();
    }
}
