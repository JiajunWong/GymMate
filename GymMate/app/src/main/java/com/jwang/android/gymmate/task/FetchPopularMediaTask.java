package com.jwang.android.gymmate.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jwang.android.gymmate.interfaces.OnFetchFinishedListener;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.util.AppConfig;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.InstagramOauth;
import com.jwang.android.gymmate.util.JsonParseUtil;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 7/15/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class FetchPopularMediaTask extends
        AsyncTask<String, Void, ArrayList<ModelMedia>>
{
    private static final String TAG = FetchPopularMediaTask.class.getSimpleName();
    private Context mContext;
    private OnFetchFinishedListener mOnFetchFinishedListener = OnFetchFinishedListener.NO_OP;

    public FetchPopularMediaTask(Context context)
    {
        mContext = context;
    }

    public void setOnFetchFinishedListener(OnFetchFinishedListener onFetchFinishedListener)
    {
        mOnFetchFinishedListener = onFetchFinishedListener;
    }

    @Override
    protected ArrayList<ModelMedia> doInBackground(String... params)
    {
        String accessToken = InstagramOauth.getsInstance(mContext).getSession().getAccessToken();
        String popularUrl = AppConfig.INSTAGRAM_POPULAR_ENDPOINT + accessToken;
        Log.d(TAG, "doInBackground url is " + popularUrl);

        String mediaResponse = HttpRequestUtil.startHttpRequest(popularUrl, TAG);
        return JsonParseUtil.parseGetMediaByLocationResultJson(mContext, mediaResponse);
    }

    @Override
    protected void onPostExecute(ArrayList<ModelMedia> medias)
    {
        super.onPostExecute(medias);
        mOnFetchFinishedListener.onFetchFinished(medias);
    }
}
