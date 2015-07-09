package com.jwang.android.gymmate.util;

import android.content.Context;
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
    public static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 5; // in Meters
    public static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds

    public static final String KEY_LOCATION_LONG = "long_location_key";
    public static final String KEY_LOCATION_LAT = "lat_location_key";

    public static Location getCurrentLocation(final Context context)
    {
        // Get the location manager
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null)
        {
            Log.w(TAG, "getCurrentLocation: " + location.getLatitude() + " " + location.getLongitude());
        }
        else
        {
            Log.e(TAG, "getCurrentLocation returns null!");
        }
        return location;
    }
}
