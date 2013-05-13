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
