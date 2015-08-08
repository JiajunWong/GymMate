package com.jwang.android.gymmate.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.model.ModelLocation;
import com.jwang.android.gymmate.util.AppConfig;
import com.jwang.android.gymmate.util.HttpRequestResultUtil;
import com.jwang.android.gymmate.util.InstagramOauth;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;

import java.util.ArrayList;

/**
 * Created by jiajunwang on 8/3/15.
 */
public class RequestInstagramLocationTask extends
        AsyncTask<String, Void, ArrayList<String>>
{
    private static final String TAG = RequestInstagramLocationTask.class.getSimpleName();
    private Context mContext;
    private OnRequestLocationIdFinishedListener mOnRequestLocationIdFinishedListener = OnRequestLocationIdFinishedListener.NO_OP;

    public RequestInstagramLocationTask(Context context)
    {
        mContext = context;
    }

    public void setOnRequestLocationIdFinishedListener(OnRequestLocationIdFinishedListener listener)
    {
        mOnRequestLocationIdFinishedListener = listener;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params)
    {
        final ArrayList<String> result = new ArrayList<>();

        if (params == null || params.length < 2)
        {
            return result;
        }

        //first is lat, second is lng
        String lat = params[0];
        String lng = params[1];

        //make a google api call to get gym locations
        RequestParams googleRequestParams = new RequestParams();
        googleRequestParams.put("location", lat + "," + lng);
        googleRequestParams.put("radius", AppConfig.RADIUS_FROM_DESTINATION * 1000);
        googleRequestParams.put("key", AppConfig.GOOGLE_ACCESS_TOKEN);
        googleRequestParams.put("types", "gym");
        googleRequestParams.put("language", "en");
        SyncHttpClient googleSyncHttpClient = new SyncHttpClient();
        googleSyncHttpClient.get(AppConfig.GOOGLE_API_ENDPOINT, googleRequestParams, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                ArrayList<ModelLocation> locations = HttpRequestResultUtil.parseGetGeoLocationByGoogleApiJson(new String(responseBody));
                Log.w(TAG, "***fetchInstagramMedias: Get " + locations.size() + " Locations from Google.");

                for (ModelLocation location : locations)
                {
                    fetchMedias(result, location.getLocationLat(), location.getLocationLong());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
                Log.e(TAG, "fetchInstagramMedias ERROR: get locations from Google.");
            }
        });

        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings)
    {
        super.onPostExecute(strings);
        mOnRequestLocationIdFinishedListener.onFinished(strings);
    }

    private void fetchMedias(final ArrayList<String> locations, String lat, String lng)
    {
        if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng))
        {
            return;
        }
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
                String facebookLocationId = HttpRequestResultUtil.parseGetFaceBookLocationByGeoResultJson(new String(responseBody)).getId();
                Log.w(TAG, "***Facebook Id is " + facebookLocationId);

                final String access_token = InstagramOauth.getsInstance().getSession().getAccessToken();
                SyncHttpClient getInstagramSyncHttpClient = new SyncHttpClient();
                RequestParams instagramRequestParams = new RequestParams();
                instagramRequestParams.put("facebook_places_id", facebookLocationId);
                instagramRequestParams.put("access_token", access_token);
                getInstagramSyncHttpClient.get(AppConfig.INSTAGRAM_LOCATION_ENDPOINT, instagramRequestParams, new AsyncHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                    {
                        String instagramLocationId = HttpRequestResultUtil.parseGetInstagramLocationByFaceBookIdJson(mContext, new String(responseBody)).getId();
                        Log.w(TAG, "***Instagram Id is " + instagramLocationId);
                        if (locations != null)
                        {
                            locations.add(instagramLocationId);
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

    public static interface OnRequestLocationIdFinishedListener
    {
        public static OnRequestLocationIdFinishedListener NO_OP = new OnRequestLocationIdFinishedListener()
        {
            @Override
            public void onFinished(ArrayList<String> locations)
            {
            }
        };

        public void onFinished(ArrayList<String> locations);
    }
}
