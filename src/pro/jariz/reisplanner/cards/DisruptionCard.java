package pro.jariz.reisplanner.cards;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import pro.jariz.reisplanner.*;

import com.fima.cardsui.objects.Card;
import pro.jariz.reisplanner.api.objects.NSStoring;

public class DisruptionCard extends Card {

    public DisruptionCard(NSStoring storing){
        super();
        this.storing = storing;
    }

    NSStoring storing;

    @Override
    public View getCardContent(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_maintenance, null);

        TextView titletv = ((TextView) view.findViewById(R.id.maintenance_title));
        titletv.setText(storing.Traject);
        titletv.setTypeface(Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Roboto-Light.ttf"));

        TextView descv = ((TextView) view.findViewById(R.id.maintenance_description));
        descv.setText(Html.fromHtml(storing.Bericht));

        if(storing.Advies != null) {
            view.findViewById(R.id.maintenance_advies_box).setVisibility(View.VISIBLE);
            ((TextView)view.findViewById(R.id.maintenance_advies)).setText(storing.Advies);
        }

        if(storing.Unplanned) ((ImageView)view.findViewById(R.id.maintenance_icon)).setImageResource(R.drawable.maintenance_unplanned);
        else ((ImageView)view.findViewById(R.id.maintenance_icon)).setImageResource(R.drawable.maintenance_planned);

        return view;
    }
}
