package com.jwang.android.gymmate.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.jwang.android.gymmate.interfaces.OnRequestMediaFinishWithArrayListener;
import com.jwang.android.gymmate.interfaces.OnRequestMediaFinishWithTimeStampListener;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.util.HttpRequestResultUtil;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.InstagramOauth;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 7/23/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class RequestMediaTask extends
        AsyncTask<String, Void, RequestUserMediaTask.ResultWrapper>
{
    private static final String TAG = RequestMediaTask.class.getSimpleName();
    private Context mContext;
    private OnRequestMediaFinishWithArrayListener mOnRequestMediaFinishWithArrayListener = OnRequestMediaFinishWithArrayListener.NO_OP;
    private OnRequestMediaFinishWithTimeStampListener mOnRequestMediaFinishWithTimeStampListener = OnRequestMediaFinishWithTimeStampListener.NO_OP;

    public RequestMediaTask(Context context)
    {
        mContext = context;
    }

    public void setOnFetchFinishedListener(OnRequestMediaFinishWithArrayListener onRequestMediaFinishWithArrayListener)
    {
        mOnRequestMediaFinishWithArrayListener = onRequestMediaFinishWithArrayListener;
    }

    public void setOnFetchMediaPaginationFinishListener(OnRequestMediaFinishWithTimeStampListener onRequestMediaFinishWithTimeStampListener)
    {
        mOnRequestMediaFinishWithTimeStampListener = onRequestMediaFinishWithTimeStampListener;
    }

    @Override
    protected RequestUserMediaTask.ResultWrapper doInBackground(String... params)
    {
        if (params.length < 1 || TextUtils.isEmpty(params[0]))
        {
            return null;
        }
        String accessToken = InstagramOauth.getsInstance().getSession().getAccessToken();
        String endpoint = params[0];
        String mediaResponse = HttpRequestUtil.startHttpRequest(endpoint, TAG);
        ArrayList<ModelMedia> medias = new ArrayList<>();
        ArrayList<String> minTimeStamps = new ArrayList<>();
        HttpRequestResultUtil.addMediaToDB(mContext, mediaResponse, HttpRequestResultUtil.RequestMediaType.USER, params[0], true, medias, minTimeStamps);
        if (minTimeStamps.isEmpty())
        {
            return new RequestUserMediaTask.ResultWrapper(medias, "");
        }
        return new RequestUserMediaTask.ResultWrapper(medias, minTimeStamps.get(0));
    }

    @Override
    protected void onPostExecute(RequestUserMediaTask.ResultWrapper resultWrapper)
    {
        super.onPostExecute(resultWrapper);
        if (resultWrapper == null)
        {
            return;
        }
        if (!resultWrapper.mMedias.isEmpty())
        {
            mOnRequestMediaFinishWithArrayListener.onFetchFinished(resultWrapper.mMedias);
        }
        if (!TextUtils.isEmpty(resultWrapper.mMinTimeStamp))
        {
            mOnRequestMediaFinishWithTimeStampListener.onFetchFinished(resultWrapper.mMinTimeStamp);
        }
    }
}
