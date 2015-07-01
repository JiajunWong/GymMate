package com.jwang.android.gymmate.adapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.model.ModelLocation;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.util.AppConfig;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.InstagramOauth;
import com.jwang.android.gymmate.util.JsonParseUtil;
import com.jwang.android.gymmate.util.LocationUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 7/1/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MediaSyncAdapter extends AbstractThreadedSyncAdapter
{
    private static final String TAG = MediaSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 30;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 6;

    public MediaSyncAdapter(Context context, boolean autoInitialize)
    {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        Log.d(TAG, "onPerformSync");

        Location location = LocationUtil.getCurrentLocation(getContext());
        final ArrayList<ModelMedia> arrayList = new ArrayList<>();

        fetchLocationsFromGoogle(location);
    }

    private void fetchLocationsFromGoogle(Location location)
    {
        //make a google api call to get gym locations
        RequestParams googleRequestParams = new RequestParams();
        googleRequestParams.put("location", location.getLatitude() + "," + location.getLongitude());
        googleRequestParams.put("radius", AppConfig.RADIUS_FROM_DESTINATION * 100);
        googleRequestParams.put("key", AppConfig.GOOGLE_ACCESS_TOKEN);
        googleRequestParams.put("types", "gym");
        googleRequestParams.put("language", "en");
        SyncHttpClient googleSyncHttpClient = new SyncHttpClient();
        googleSyncHttpClient.get(AppConfig.GOOGLE_API_ENDPOINT, googleRequestParams, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                ArrayList<ModelLocation> locations = JsonParseUtil.parseGetGeoLocationByGoogleApiJson(new String(responseBody));
                Log.w(TAG, "***fetchInstagramMedias: Get " + locations.size() + " Locations from Google.");

                for (ModelLocation location : locations)
                {
                    fetchMedias(location.getLocationLat(), location.getLocationLong());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
                Log.e(TAG, "fetchInstagramMedias ERROR: get locations from Google.");
            }
        });
    }

    private void fetchMedias(String lat, String lng)
    {
        RequestParams facebookRequestParams = new RequestParams();
        facebookRequestParams.put("type", "place");
        facebookRequestParams.put("center", lat + "," + lng);
        facebookRequestParams.put("distance", "100");
        facebookRequestParams.put("access_token", "425103717696529|1b77655dba1ccc2ed88fad1f9a932d7b");
        facebookRequestParams.put("expires_in", "5184000");
        SyncHttpClient asyncHttpClient = new SyncHttpClient();
        asyncHttpClient.get("https://graph.facebook.com/search", facebookRequestParams, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                String facebookLocationId = JsonParseUtil.parseGetFaceBookLocationByGeoResultJson(new String(responseBody)).getId();
                Log.w(TAG, "***Facebook Id is " + facebookLocationId);

                final String access_token = InstagramOauth.getsInstance(getContext()).getSession().getAccessToken();
                SyncHttpClient getInstagramSyncHttpClient = new SyncHttpClient();
                RequestParams instagramRequestParams = new RequestParams();
                instagramRequestParams.put("facebook_places_id", facebookLocationId);
                instagramRequestParams.put("access_token", access_token);
                getInstagramSyncHttpClient.get(AppConfig.INSTAGRAM_LOCATION_ENDPOINT, instagramRequestParams, new AsyncHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                    {
                        String instagramLocationId = JsonParseUtil.parseGetInstagramLocationByFaceBookIdJson(new String(responseBody)).getId();
                        Log.w(TAG, "***Instagram Id is " + instagramLocationId);
                        if (!TextUtils.isEmpty(instagramLocationId))
                        {
                            String popularJsonStr = HttpRequestUtil.startHttpRequest("https://api.instagram.com/v1/locations/" + instagramLocationId + "/media/recent?access_token=" + access_token, TAG);
                            ArrayList<ModelMedia> modelMedias = JsonParseUtil.parseGetMediaByLocationResultJson(getContext(), popularJsonStr);
                            Log.w(TAG, "***Get " + modelMedias.size() + " from Instagram.");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                    {
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
            }
        });
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime)
    {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().syncPeriodic(syncInterval, flexTime).setSyncAdapter(account, authority).setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        }
        else
        {
            ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context)
    {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context)
    {
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount))
        {

            /*
             * Add the account and account type, no password or user data
             * If successful, return the Account object, otherwise report an error.
             */
            if (!accountManager.addAccountExplicitly(newAccount, "", null))
            {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context)
    {
        /*
         * Since we've created an account
         */
        MediaSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context)
    {
        getSyncAccount(context);
    }
}
