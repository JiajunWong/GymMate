package com.jwang.android.gymmate.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.jwang.android.gymmate.data.MediaContract;
import com.jwang.android.gymmate.model.ModelLocation;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.JsonParseUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 6/24/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class InstagramMediaTask extends
        AsyncTask<String, Void, ArrayList<ModelMedia>>
{
    private static final String TAG = InstagramMediaTask.class.getSimpleName();
    private Context mContext;

    public InstagramMediaTask(Context context)
    {
        mContext = context;
    }

    @Override
    protected ArrayList<ModelMedia> doInBackground(String... params)
    {
        if (params.length < 3 && TextUtils.isEmpty(params[0]) || TextUtils.isEmpty(params[1]) || TextUtils.isEmpty(params[2]))
        {
            return null;
        }

        final ArrayList<ModelMedia> arrayList = new ArrayList<>();

        SyncHttpClient googleSyncHttpClient = new SyncHttpClient();
        googleSyncHttpClient.get("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + params[1] + "," + params[2] + "&radius=5000&key=AIzaSyCxzHIfkpQKoHWxHBkeEX-7UcBTq_ykikE&types=gym&language=en", new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                ArrayList<ModelLocation> locations = JsonParseUtil.parseGetGeoLocationByGoogleApiJson(new String(responseBody));
                for (ModelLocation location : locations)
                {
                    fetchData(location.getLocationLat(), location.getLocationLong(), arrayList);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
            }
        });

        //use facebook api get location ids
        //        String facebookLocation = HttpRequestUtil.startHttpRequest("https://graph.facebook.com/search?type=place&center=37.549696,-122.314780&distance=100&access_token=425103717696529|1b77655dba1ccc2ed88fad1f9a932d7b&expires_in=5184000", TAG);
        //        String facebookLocationId = JsonParseUtil.parseGetFaceBookLocationByGeoResultJson(facebookLocation).getId();

        String accessToken = params[0];
        //        String popularJsonStr = HttpRequestUtil.startHttpRequest("https://api.instagram.com/v1/media/search?lat=37.7814460&lng=-122.3921540&distance=3000&access_token=" + accessToken, TAG);
        //        String popularJsonStr = HttpRequestUtil.startHttpRequest("https://api.instagram.com/v1/media/search?lat=37.549696&lng=-122.314780&distance=3000&access_token=" + accessToken, TAG);
        //        String popularJsonStr = HttpRequestUtil.startHttpRequest("https://api.instagram.com/v1/media/popular?access_token=" + accessToken, TAG);
        //        String popularJsonStr = HttpRequestUtil.startHttpRequest("https://api.instagram.com/v1/locations/" + "1572489" + "/media/recent?access_token=588218898.e23a1c4.ee9e21e827144eadacbd607ced01603e", TAG);
        return arrayList;
    }

    private void fetchData(String lat, String lng, final ArrayList<ModelMedia> arrayList)
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
                SyncHttpClient getInstagramSyncHttpClient = new SyncHttpClient();
                getInstagramSyncHttpClient.get("https://api.instagram.com/v1/locations/search?facebook_places_id=" + facebookLocationId + "&access_token=588218898.e23a1c4.ee9e21e827144eadacbd607ced01603e", new AsyncHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                    {
                        String instagramLocationId = JsonParseUtil.parseGetInstagramLocationByFaceBookIdJson(new String(responseBody)).getId();
                        String popularJsonStr = HttpRequestUtil.startHttpRequest("https://api.instagram.com/v1/locations/" + instagramLocationId + "/media/recent?access_token=588218898.e23a1c4.ee9e21e827144eadacbd607ced01603e", TAG);
                        ArrayList<ModelMedia> modelMedias = JsonParseUtil.parseGetMediaByLocationResultJson(popularJsonStr);
                        for (ModelMedia modelMedia : modelMedias)
                        {
                            arrayList.add(modelMedia);
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

    @Override
    protected void onPostExecute(ArrayList<ModelMedia> medias)
    {
        addUserValues(medias);
        addMediaValues(medias);
    }

    private void addUserValues(ArrayList<ModelMedia> medias)
    {
        Cursor userCursor;
        for (ModelMedia modelMedia : medias)
        {
            userCursor = mContext.getContentResolver().query(MediaContract.UserEntry.CONTENT_URI, new String[] { MediaContract.UserEntry.COLUMN_INSTAGRAM_ID }, MediaContract.UserEntry.COLUMN_INSTAGRAM_ID + " = ?", new String[] { Long.toString(modelMedia.getOwner().getInstagramId()) }, null);
            if (userCursor.moveToFirst())
            {
                userCursor.close();
            }
            else
            {
                ContentValues userContentValues = new ContentValues();
                userContentValues.put(MediaContract.UserEntry.COLUMN_INSTAGRAM_ID, modelMedia.getOwner().getInstagramId());
                userContentValues.put(MediaContract.UserEntry.COLUMN_USERNAME, modelMedia.getOwner().getUserName());
                userContentValues.put(MediaContract.UserEntry.COLUMN_FULL_NAME, modelMedia.getOwner().getFullName());
                userContentValues.put(MediaContract.UserEntry.COLUMN_PROFILE_PICTURE, modelMedia.getOwner().getProfilePicture());

                mContext.getContentResolver().insert(MediaContract.UserEntry.CONTENT_URI, userContentValues);
            }
        }
    }

    private void addMediaValues(ArrayList<ModelMedia> medias)
    {
        Cursor mediaCursor;
        for (ModelMedia modelMedia : medias)
        {
            mediaCursor = mContext.getContentResolver().query(MediaContract.MediaEntry.CONTENT_URI, new String[] { MediaContract.MediaEntry.COLUMN_MEDIA_INSTAGRAM_ID }, MediaContract.MediaEntry.COLUMN_MEDIA_INSTAGRAM_ID + " = ?", new String[] { modelMedia.getInstagramId() }, null);
            if (mediaCursor.moveToFirst())
            {
                mediaCursor.close();
            }
            else
            {
                ContentValues mediaContentValues = new ContentValues();
                mediaContentValues.put(MediaContract.MediaEntry.COLUMN_TAGS, modelMedia.getTagString());
                mediaContentValues.put(MediaContract.MediaEntry.COLUMN_TYPE, modelMedia.getType());
                mediaContentValues.put(MediaContract.MediaEntry.COLUMN_LOCATION_LATITUDE, modelMedia.getLocationLat());
                mediaContentValues.put(MediaContract.MediaEntry.COLUMN_LOCATION_LONGITUDE, modelMedia.getLocationLong());
                mediaContentValues.put(MediaContract.MediaEntry.COLUMN_CREATE_TIME, modelMedia.getCreateTime());
                mediaContentValues.put(MediaContract.MediaEntry.COLUMN_LINK, modelMedia.getLink());
                mediaContentValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_LOW, modelMedia.getImageLowRes());
                mediaContentValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_THUMBNAIL, modelMedia.getImageThumbnail());
                mediaContentValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_STANDARD, modelMedia.getImageHighRes());
                mediaContentValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_OWNER_ID, modelMedia.getOwner().getInstagramId());
                mediaContentValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_INSTAGRAM_ID, modelMedia.getInstagramId());
                mediaContentValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_VIDEO_LOW_BANDWIDTH, modelMedia.getVideoLowBandwidth());
                mediaContentValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_VIDEO_STANDARD_RES, modelMedia.getVideoStandardRes());
                mediaContentValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_VIDEO_LOW_RES, modelMedia.getVideoLowRes());
                mediaContentValues.put(MediaContract.MediaEntry.COLUMN_CAPTION_TEXT, modelMedia.getCaptionText());

                mContext.getContentResolver().insert(MediaContract.MediaEntry.CONTENT_URI, mediaContentValues);
            }
        }
    }
}
