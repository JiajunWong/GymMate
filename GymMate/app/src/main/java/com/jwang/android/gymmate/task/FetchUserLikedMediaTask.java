package com.jwang.android.gymmate.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.interfaces.OnFetchFinishedListener;
import com.jwang.android.gymmate.interfaces.OnFetchUserLikedFinishedListener;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.JsonParseUtil;

/**
 * @author Jiajun Wang on 7/15/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class FetchUserLikedMediaTask extends
        AsyncTask<String, Void, JsonParseUtil.ResultWrapper>
{
    private static final String TAG = FetchUserLikedMediaTask.class.getSimpleName();
    private OnFetchUserLikedFinishedListener mOnFetchFinishedListener = OnFetchUserLikedFinishedListener.NO_OP;
    private Context mContext;

    public FetchUserLikedMediaTask(Context context)
    {
        mContext = context;
    }

    public void setOnFetchFinishedListener(OnFetchUserLikedFinishedListener onFetchFinishedListener)
    {
        mOnFetchFinishedListener = onFetchFinishedListener;
    }

    @Override
    protected JsonParseUtil.ResultWrapper doInBackground(String... params)
    {
        if (params.length < 1 && TextUtils.isEmpty(params[0]))
        {
            Log.e(TAG, "doInBackground url is NULL!!");
            return null;
        }

        Log.d(TAG, "doInBackground url is " + params[0]);
        String mediaResponse = HttpRequestUtil.startHttpRequest(params[0], TAG);
        return JsonParseUtil.parseMediaGetMediasAndPagination(mContext, mediaResponse, false);
    }

    @Override
    protected void onPostExecute(JsonParseUtil.ResultWrapper resultWrapper)
    {
        super.onPostExecute(resultWrapper);
        mOnFetchFinishedListener.onFetchFinished(resultWrapper);
    }
}
