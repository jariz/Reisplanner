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

package pro.jariz.reisplanner.cards;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import pro.jariz.reisplanner.*;

import com.fima.cardsui.objects.Card;

public class StationCard extends Card {

	public StationCard(String title){
		super(title);
	}

	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_station, null);
		
		TextView titletv = ((TextView) view.findViewById(R.id.title));
		titletv.setText(title);
	    titletv.setTypeface(Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Roboto-Light.ttf"));
		//((ImageView) view.findViewById(R.id.stationimage)).setImageResource(image);
		
		return view;
	}
}
