package com.jwang.android.gymmate.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.interfaces.OnRequestMediaFinishWithTimeStampListener;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.util.HttpRequestResultUtil;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.InstagramOauth;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 7/30/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class RequestMediaByLocationId extends AsyncTask<String, Void, String>
{
    private static final String TAG = RequestMediaByLocationId.class.getSimpleName();
    private Context mContext;
    private OnRequestMediaFinishWithTimeStampListener mOnRequestMediaFinishWithTimeStampListener = OnRequestMediaFinishWithTimeStampListener.NO_OP;

    public RequestMediaByLocationId(Context context)
    {
        mContext = context;
    }

    public void setOnFetchMediaPaginationFinishListener(OnRequestMediaFinishWithTimeStampListener onRequestMediaFinishWithTimeStampListener)
    {
        mOnRequestMediaFinishWithTimeStampListener = onRequestMediaFinishWithTimeStampListener;
    }

    @Override
    protected String doInBackground(String... params)
    {
        if (params.length < 2 || TextUtils.isEmpty(params[0]))
        {
            Log.e(TAG, "RequestMediaByLocationId -- doInBackground: location id and timeStamps is null!");
            return null;
        }

        String locationId = params[0];
        String timeStamp = params[1];
        String access_token = InstagramOauth.getsInstance().getSession().getAccessToken();
        String url;
        if (!TextUtils.isEmpty(timeStamp))
        {
            url = "https://api.instagram.com/v1/locations/" + locationId + "/media/recent?access_token=" + access_token + "&max_timestamp=" + timeStamp;
        }
        else
        {
            url = "https://api.instagram.com/v1/locations/" + locationId + "/media/recent?access_token=" + access_token;
        }

        String mediaResponse = HttpRequestUtil.startHttpRequest(url, TAG);
        ArrayList<ModelMedia> medias = new ArrayList<>();
        ArrayList<String> minTimeStamps = new ArrayList<>();
        HttpRequestResultUtil.addMediaToDB(mContext, mediaResponse, HttpRequestResultUtil.RequestMediaType.USER, params[0], true, medias, minTimeStamps);

        if (minTimeStamps.isEmpty())
        {
            return null;
        }
        else
        {
            return minTimeStamps.get(0);
        }
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        if (!TextUtils.isEmpty(s))
        {
            mOnRequestMediaFinishWithTimeStampListener.onFetchFinished(s);
        }
    }
}
