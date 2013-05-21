package pro.jariz.reisplanner.misc;

import de.keyboardsurfer.android.widget.crouton.Style;
import pro.jariz.reisplanner.R;

/**
 * Created by JariZ on 19-5-13.
 */
public class CroutonStyles {
    public static Style infoStyle = new Style.Builder()
            .setBackgroundColor(R.color.crouton_info)
            .setTextColor(android.R.color.white)
            .build();

    public static Style successStyle = new Style.Builder()
            .setBackgroundColor(R.color.crouton_success)
            .setTextColor(android.R.color.white)
            .build();

    public static Style errorStyle = new Style.Builder()
            .setBackgroundColor(R.color.crouton_error)
            .setTextColor(android.R.color.white)
            .build();
}
