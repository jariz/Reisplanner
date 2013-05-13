package pro.jariz.reisplanner.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;

public class NSHardcoded {
	public static String HARDCODEDSTATIONS(Context context) {
		try {
			
			
			//goddamnit why is this so fucking hard java/android !??!? just reading a asset ffs
			
			byte[] buffer = new byte[196608];
			context.getAssets().open("ns-api-stations-v2.xml").read(buffer);
			return new String(buffer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
