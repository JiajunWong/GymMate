package com.jwang.android.gymmate.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.interfaces.OnFetchMediaArrayFinishListener;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.util.HttpRequestUtil;
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
    private OnFetchMediaArrayFinishListener mOnFetchMediaArrayFinishListener = OnFetchMediaArrayFinishListener.NO_OP;
    private Context mContext;

    public FetchPopularMediaTask(Context context)
    {
        mContext = context;
    }

    public void setOnFetchFinishedListener(OnFetchMediaArrayFinishListener onFetchMediaArrayFinishListener)
    {
        mOnFetchMediaArrayFinishListener = onFetchMediaArrayFinishListener;
    }

    @Override
    protected ArrayList<ModelMedia> doInBackground(String... params)
    {
        if (params.length < 1 && TextUtils.isEmpty(params[0]))
        {
            Log.e(TAG, "doInBackground url is NULL!!");
            return null;
        }

        Log.d(TAG, "doInBackground url is " + params[0]);
        String mediaResponse = HttpRequestUtil.startHttpRequest(params[0], TAG);
        return JsonParseUtil.parseMediaGetMedias(mContext, mediaResponse, false);
    }

    @Override
    protected void onPostExecute(ArrayList<ModelMedia> medias)
    {
        super.onPostExecute(medias);
        mOnFetchMediaArrayFinishListener.onFetchFinished(medias);
    }
}
