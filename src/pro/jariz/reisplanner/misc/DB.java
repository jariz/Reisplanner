package pro.jariz.reisplanner.misc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import pro.jariz.reisplanner.api.objects.NSStation;

import java.util.ArrayList;

/**
 * Created by JariZ on 18-5-13.
 */
public class DB {

    public static String TAG = "Reisplanner.DB";

    static SQLiteDatabase db = null;
    static DatabaseHelper dbhelper = null;

    public static void Init(Context context) {
        if(db == null && dbhelper == null) {
            dbhelper = new DatabaseHelper(context);
            db = dbhelper.getWritableDatabase();
        } else Log.w(TAG, "Reisplanner.DB was asked to initialize while already being initialized!");
    }

    public static void Close() {
        if(db != null && dbhelper != null) dbhelper.close();
        else Log.w(TAG, "Trying to close a non-initialized Reisplanner.DB!");
    }

    /*
     * QUERIES
     */
    public static NSStation[] fetchAllStations(boolean onlynames) {
        String[] columns;
        if(onlynames) columns = new String[] { "id", "code", "type", "land", "naam_kort", "naam_middel", "naam_lang" };
        else columns = new String[] { "*" };

        ArrayList<NSStation> stations = new ArrayList<NSStation>();
        Cursor results = db.query("stations", columns, null, null, null, null, null);
        if(results != null) {
            results.moveToFirst();
            while(!results.isAfterLast()) {
                NSStation station = new NSStation();

                station.Code = results.getString(1);
                station.Type = results.getString(2);
                station.Land = results.getString(3);
                station.Namen.Kort = results.getString(4);
                station.Namen.Middel = results.getString(5);
                station.Namen.Lang = results.getString(6);

                if(!onlynames) {
                    station.UICCode = results.getInt(7);
                    station.Lat = results.getDouble(8);
                    station.Lon = results.getDouble(9);
                    station.Synoniemen = results.getString(10).split(";");
                }
                stations.add(station);
                results.moveToNext();
            }
        }

        Log.i(TAG, "fetchAllStations() returned "+stations.size()+" stations!");
        NSStation[] stationsarr = new NSStation[stations.size()];
        return stations.toArray(stationsarr);
    }

    public static void insertStations(NSStation[] stations) {
        for(int i=0;i < stations.length; i++) {
            ContentValues row = new ContentValues();
            NSStation station = stations[i];
            row.put("code", station.Code);
            row.put("type", station.Type);
            row.put("land", station.Land);
            row.put("naam_kort", station.Namen.Kort);
            row.put("naam_middel", station.Namen.Middel);
            row.put("naam_lang", station.Namen.Lang);
            row.put("uiccode", station.UICCode);
            row.put("lat", station.Lat);
            row.put("long", station.Lon);
            String synoniemen = "";
            for(int x=0; x < station.Synoniemen.length; x++) {
                synoniemen += ","+station.Synoniemen[x];
            }
            if(synoniemen.length() > 0)
            row.put("synoniemen", synoniemen.substring(1));

            db.insert("stations", null, row);
        }
        Log.i(TAG, "Inserted "+ stations.length + " stations into DB!");
    }

    public static void clearStations() {
        //DELETE EVERYTHING.jpg
        db.delete("stations", null, null);
    }

    public static Integer getLastCacheTime(String name) {
        Cursor result = db.query("cache", new String[] { "value" }, "name = ?", new String[] { name }, null, null, null);
        if(result.getPosition() == -1) return 0;
        else {
            result.moveToFirst();
            return result.getInt(0);
        }
    }

    public static void setLastCacheTime(String name, Integer value) {
        db.delete("cache", "name = ?", new String[] { name });
        ContentValues row = new ContentValues();
        row.put("value", value);
        db.insert("cache", null, row);
    }

    public static Integer getStationCount() {
        Cursor count = db.query("stations", new String[] { "COUNT(*)" }, null, null, null, null, null);
        count.moveToFirst();
        return count.getInt(0);
    }



    /**
     * Thanks to http://androidpartaker.wordpress.com/2011/07/03/introduction-to-android-sqlite-database/
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, "main", null, 1);
        }
        /**
         * onCreate method is called for the 1st time when database doesn't exists.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "Reisplanner.DB is creating a new database and filling tables...");

            //stations list
            db.execSQL("CREATE TABLE \"main\".\"stations\" (\n" +
                    "\"id\"  INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\"code\"  TEXT,\n" +
                    "\"type\"  TEXT,\n" +
                    "\"land\"  TEXT,\n" +
                    "\"naam_kort\"  TEXT,\n" +
                    "\"naam_middel\"  TEXT,\n" +
                    "\"naam_lang\"  TEXT,\n" +
                    "\"uiccode\"  INTEGER,\n" +
                    "\"lat\"  REAL,\n" +
                    "\"long\"  REAL,\n" +
                    "\"synoniemen\"  TEXT\n" +
                    ")\n" +
                    ";\n" +
                    "\n");

            //cache times
            db.execSQL("CREATE TABLE \"main\".\"cache\" (\n" +
                    "\"name\"  TEXT,\n" +
                    "\"value\"  INTEGER\n" +
                    ")\n" +
                    ";\n" +
                    "\n");
        }
        /**
         * onUpgrade method is called when database version changes.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion);
        }
    }
}
