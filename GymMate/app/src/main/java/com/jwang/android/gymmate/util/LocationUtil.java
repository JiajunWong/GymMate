package com.jwang.android.gymmate.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.firebase.client.Firebase;
import com.jwang.android.gymmate.model.ModelUserLocation;

/**
 * @author Jiajun Wang on 6/30/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class LocationUtil
{
    public static final String KEY_LOCATION_LONG = "long_location_key";
    public static final String KEY_LOCATION_LAT = "lat_location_key";

    public static String[] getCurrentLocation(Context context)
    {
        // 0 -> longitude, 1 -> latitude
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String[] locations = new String[2];
        locations[0] = sharedPreferences.getString(KEY_LOCATION_LONG, null);
        locations[1] = sharedPreferences.getString(KEY_LOCATION_LAT, null);
        return locations;
    }

    public static boolean updateLocation(Context context, Location location)
    {
        if (location == null)
        {
            return false;
        }

        saveLocationData(location);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String lng = sharedPreferences.getString(KEY_LOCATION_LONG, null);
        String lat = sharedPreferences.getString(KEY_LOCATION_LAT, null);

        if (!TextUtils.isEmpty(lng) && !TextUtils.isEmpty(lat))
        {
            GeoLocationUtil geoLocationUtil = GeoLocationUtil.fromDegrees(location.getLatitude(), location.getLongitude());
            GeoLocationUtil oldGeoLocationUtil1 = GeoLocationUtil.fromDegrees(Double.valueOf(lat), Double.valueOf(lng));

            double distance = geoLocationUtil.distanceTo(oldGeoLocationUtil1, AppConfig.RADIUS_SPHERE);
            if (distance < 5)
            {
                return false;
            }
        }
        sharedPreferences.edit().putString(KEY_LOCATION_LAT, Double.toString(location.getLatitude())).apply();
        sharedPreferences.edit().putString(KEY_LOCATION_LONG, Double.toString(location.getLongitude())).apply();

        return true;
    }

    private static void saveLocationData(Location location)
    {
        Firebase myFirebaseRef = new Firebase(AppConfig.FIREBASE_ENDPOINT);
        String userid = InstagramOauth.getsInstance().getSession().getUser().id;
        String username = InstagramOauth.getsInstance().getSession().getUser().username;
        String userRealName = InstagramOauth.getsInstance().getSession().getUser().fullName;
        String userProfileImage = InstagramOauth.getsInstance().getSession().getUser().profilPicture;

        Firebase alanRef = myFirebaseRef.child("user_location").child(userid);
        ModelUserLocation modelUserLocation = new ModelUserLocation();
        modelUserLocation.setFullName(userRealName);
        modelUserLocation.setUserName(username);
        modelUserLocation.setProfilePicture(userProfileImage);
        modelUserLocation.setInstagramId(userid);
        modelUserLocation.setLatitude(Double.toString(location.getLatitude()));
        modelUserLocation.setLongitude(Double.toString(location.getLongitude()));

        alanRef.setValue(modelUserLocation);
    }

    public static boolean isLocationEmpty(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return TextUtils.isEmpty(sharedPreferences.getString(KEY_LOCATION_LONG, null)) || TextUtils.isEmpty(sharedPreferences.getString(KEY_LOCATION_LAT, null));
    }

    public static boolean isValidLocations(String[] locations)
    {
        if (locations == null || locations.length != 2)
        {
            return false;
        }

        if (locations[0] == null || locations[1] == null)
        {
            return false;
        }

        return true;
    }
}
