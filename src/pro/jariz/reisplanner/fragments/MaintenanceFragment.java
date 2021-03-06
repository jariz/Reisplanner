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

import com.devspark.progressfragment.ProgressSherlockFragment;
import com.fima.cardsui.views.*;
import com.fima.cardsui.objects.*;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import pro.jariz.reisplanner.R;
import pro.jariz.reisplanner.api.*;
import pro.jariz.reisplanner.api.objects.NSStation;
import pro.jariz.reisplanner.api.objects.NSStoring;
import pro.jariz.reisplanner.cards.DisruptionCard;
import pro.jariz.reisplanner.misc.CroutonStyles;

import java.util.Arrays;

public class MaintenanceFragment extends ProgressSherlockFragment {
	
	Boolean storingen;
	View thisView;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		SlidingFragmentActivity context = (SlidingFragmentActivity)this.getActivity();
		thisView = inflater.inflate(R.layout.fragment_maintenance, container, false);
		storingen = this.getArguments().getBoolean("storingen", false);
		
		CardUI mCardView = (CardUI) thisView.findViewById(R.id.content_container);
		CardStack title = new CardStack();
		if(storingen) {
			context.getSupportActionBar().setTitle("Storingen");
			title.setTitle("Storingen");
		} else {
			context.getSupportActionBar().setTitle("Werkzaamheden");
			title.setTitle("Werkzaamheden");
		}

        new NSTask(this).execute(NSTask.TYPE_STORINGEN);

		title.setTitleTypeFace(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-CondensedItalic.ttf"));
		mCardView.addStack(title);
		mCardView.refresh();

		return thisView;
	}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //are we still showing a crouton? kill it with fire.
        if(crouton != null)
            Crouton.hide(crouton);
    }

    private Crouton crouton = null;

    @Override
    public void Invoke(Object Result, Integer TaskType) {
        if(Result == null) {
            //remove loading screen
            setContentShown(true);
            //remove cards
            thisView.findViewById(R.id.content_container).setVisibility(View.GONE);
            //show feedback
            crouton = Crouton.makeText(this.getActivity(), "Er is een fout opgetreden tijdens het ophalen van de storingen: " + NSTask.LastExceptionMessage, CroutonStyles.errorStyle);
            crouton.setConfiguration(new Configuration.Builder().setDuration(Configuration.DURATION_INFINITE).build());
            crouton.show();

            // === a blank screen with a notification showing at top
            return;
        }
        Object[] objects = (Object[])Result;
        NSStoring[] storings = Arrays.copyOf(objects, objects.length, NSStoring[].class);
        CardUI mCardView = (CardUI) thisView.findViewById(R.id.content_container);
        int added = 0;
        for(int i=0;i < storings.length; i++) {
            if(storings[i].Unplanned != storingen) continue;
            added++;
            DisruptionCard card = new DisruptionCard(storings[i]);
            mCardView.addCard(card);
        }
        mCardView.refresh();

        if(added > 0) setContentShown(true);
        //else setContentEmpty(true);
        //screw progressfragment's helpers, we'll do it our way considering it's too much work to get it working 'their' way

        else {
            thisView.findViewById(R.id.empty).setVisibility(View.VISIBLE);
            mCardView.refresh();
            setContentShown(true);
            mCardView.setVisibility(View.GONE);
        }
    }
}
