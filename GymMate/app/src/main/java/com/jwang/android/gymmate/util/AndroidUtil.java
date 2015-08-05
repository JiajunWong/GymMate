package com.jwang.android.gymmate.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.activity.BaseActivity;

/**
 * @author Jiajun Wang on 7/25/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class AndroidUtil
{
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Returns true if the network is available or about to become available.
     *
     * @param c Context used to get the ConnectivityManager
     * @return true if the network is available
     */
    public static boolean isNetworkAvailable(Context c)
    {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     *
     * @param c Context used to get the SharedPreferences
     * @return the location status integer type
     */
    @SuppressWarnings("ResourceType")
    public static @BaseActivity.LocationStatus int getLocationStatus(Context c)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        return sp.getInt(c.getString(R.string.pref_location_status_key), BaseActivity.LOCATION_STATUS_UNKNOWN);
    }
}
