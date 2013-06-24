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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.devspark.progressfragment.ProgressSherlockFragment;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import pro.jariz.reisplanner.R;
import pro.jariz.reisplanner.api.NSTask;
import pro.jariz.reisplanner.api.NSTaskInvokable;
import pro.jariz.reisplanner.misc.CroutonStyles;

/**
 * JARIZ.PRO
 * Created @ 11/06/13
 * By: JariZ
 * Project: Reisplanner2
 * Package: pro.jariz.reisplanner.fragments
 *
 * SelectFragment handles the loading and displaying of possible routes
 */
public class SelectFragment extends ProgressSherlockFragment {

    private View thisView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Activity context = (Activity)this.getActivity();
        thisView = inflater.inflate(R.layout.fragment_select, container, false);

        Bundle args = this.getActivity().getIntent().getExtras();

        getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new NSTask(this, args).execute(NSTask.TYPE_REISADVIES);

        return thisView;
    }

    @Override
    public void Invoke(Object Result, Integer TaskType) {
        if(Result == null) {
            Crouton.showText(getActivity(), "Reisadvies kon niet worden opgehaald: "+NSTask.LastExceptionMessage, CroutonStyles.errorStyle);
        }
    }
}
