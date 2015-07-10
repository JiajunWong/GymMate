package com.jwang.android.gymmate.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * @author Jiajun Wang on 6/30/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class LocationUtil
{
    private static final String TAG = LocationUtil.class.getSimpleName();
    public static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1000; // in Meters
    public static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds

    public static final String KEY_LOCATION_LONG = "long_location_key";
    public static final String KEY_LOCATION_LAT = "lat_location_key";

    public static String[] getCurrentLocation(final Context context)
    {
        // 0 -> longitude, 1 -> latitude
        String[] locations = new String[2];
        // Get the location manager
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use
        // default
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        if (location == null)
        {
            SharedPreferences sharedPreferences = context.getSharedPreferences(AppConfig.LOCATION, Context.MODE_PRIVATE);
            String lng = sharedPreferences.getString(LocationUtil.KEY_LOCATION_LONG, "0");
            String lat = sharedPreferences.getString(LocationUtil.KEY_LOCATION_LAT, "0");
            locations[0] = lng;
            locations[1] = lat;
        }
        else
        {
            locations[0] = Double.toString(location.getLongitude());
            locations[1] = Double.toString(location.getLatitude());
        }

        if (locations != null && locations.length == 2)
        {
            Log.w(TAG, "getCurrentLocation: " + locations[0] + " " + locations[1]);
        }
        else
        {
            Log.e(TAG, "getCurrentLocation returns null!");
        }
        return locations;
    }
}
