package com.jwang.android.gymmate.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.interfaces.OnFetchMediaArrayFinishListener;
import com.jwang.android.gymmate.interfaces.OnRequestMediaFinishWithTimeStampListener;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.util.HttpRequestResultUtil;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.InstagramOauth;

import java.util.ArrayList;

/**
 * Created by jiajunwang on 7/2/15.
 */
public class RequestUserMediaTask extends
        AsyncTask<String, Void, RequestUserMediaTask.ResultWrapper>
{
    private static final String TAG = RequestUserMediaTask.class.getSimpleName();
    private Context mContext;
    private OnFetchMediaArrayFinishListener mOnFetchMediaArrayFinishListener = OnFetchMediaArrayFinishListener.NO_OP;
    private OnRequestMediaFinishWithTimeStampListener mOnRequestMediaFinishWithTimeStampListener = OnRequestMediaFinishWithTimeStampListener.NO_OP;

    public RequestUserMediaTask(Context context)
    {
        mContext = context;
    }

    public void setOnFetchFinishedListener(OnFetchMediaArrayFinishListener onFetchMediaArrayFinishListener)
    {
        mOnFetchMediaArrayFinishListener = onFetchMediaArrayFinishListener;
    }

    public void setOnFetchMediaPaginationFinishListener(OnRequestMediaFinishWithTimeStampListener onRequestMediaFinishWithTimeStampListener)
    {
        mOnRequestMediaFinishWithTimeStampListener = onRequestMediaFinishWithTimeStampListener;
    }

    @Override
    protected ResultWrapper doInBackground(String... params)
    {
        if (params.length < 1 || TextUtils.isEmpty(params[0]) || TextUtils.isEmpty(params[1]))
        {
            Log.e(TAG, "doInBackground: user id is null!");
            return null;
        }
        Log.d(TAG, "FetchMediaWithStoreAndPaginationTask -- doInBackground");

        String accessToken = InstagramOauth.getsInstance().getSession().getAccessToken();
        String endpoint = "https://api.instagram.com/v1/users/" + params[0] + "/media/recent/?access_token=" + accessToken + "&count=20&max_timestamp=" + params[1];
        String mediaResponse = HttpRequestUtil.startHttpRequest(endpoint, TAG);
        ArrayList<ModelMedia> medias = new ArrayList<>();
        ArrayList<String> minTimeStamps = new ArrayList<>();
        HttpRequestResultUtil.addMediaToDB(mContext, mediaResponse, HttpRequestResultUtil.RequestMediaType.USER, params[0], true, medias, minTimeStamps);

        if (minTimeStamps.isEmpty())
        {
            return new ResultWrapper(medias, "");
        }
        return new ResultWrapper(medias, minTimeStamps.get(0));
    }

    @Override
    protected void onPostExecute(ResultWrapper resultWrapper)
    {
        super.onPostExecute(resultWrapper);
        if (resultWrapper == null)
        {
            return;
        }
        if (!resultWrapper.mMedias.isEmpty())
        {
            mOnFetchMediaArrayFinishListener.onFetchFinished(resultWrapper.mMedias);
        }
        if (!TextUtils.isEmpty(resultWrapper.mMinTimeStamp))
        {
            mOnRequestMediaFinishWithTimeStampListener.onFetchFinished(resultWrapper.mMinTimeStamp);
        }
    }

    protected static class ResultWrapper
    {
        public ArrayList<ModelMedia> mMedias;
        public String mMinTimeStamp;

        public ResultWrapper(ArrayList<ModelMedia> arrayList, String url)
        {
            mMedias = arrayList;
            mMinTimeStamp = url;
        }
    }
}
