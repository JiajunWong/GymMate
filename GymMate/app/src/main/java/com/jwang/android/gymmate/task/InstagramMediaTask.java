package com.jwang.android.gymmate.task;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.interfaces.OnFetchFinishedListener;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.JsonParseUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @author Jiajun Wang on 6/24/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class InstagramMediaTask extends
        AsyncTask<String, Void, ArrayList<ModelMedia>>
{
    private static final String TAG = InstagramMediaTask.class.getSimpleName();
    private OnFetchFinishedListener mOnFetchFinishedListener = OnFetchFinishedListener.NO_OP;

    public void setOnFetchFinishedListener(OnFetchFinishedListener listener)
    {
        mOnFetchFinishedListener = listener;
    }

    @Override
    protected ArrayList<ModelMedia> doInBackground(String... params)
    {
        if (TextUtils.isEmpty(params[0]))
        {
            return null;
        }

        final ArrayList<ModelMedia> arrayList = new ArrayList<>();

        RequestParams facebookRequestParams = new RequestParams();
        facebookRequestParams.put("type", "place");
        facebookRequestParams.put("center", "37.549696,-122.314780");
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

    @Override
    protected void onPostExecute(ArrayList<ModelMedia> medias)
    {
        super.onPostExecute(medias);
        if (medias != null && medias.size() > 0)
        {
            mOnFetchFinishedListener.onSuccess(medias);
        }
        else
        {
            mOnFetchFinishedListener.onFailed();
        }
    }
}