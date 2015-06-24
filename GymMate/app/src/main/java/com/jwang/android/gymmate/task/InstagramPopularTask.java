package com.jwang.android.gymmate.task;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.jwang.android.gymmate.interfaces.OnFetchFinishedListener;
import com.jwang.android.gymmate.util.HttpRequestUtil;

/**
 * @author Jiajun Wang on 6/24/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class InstagramPopularTask extends AsyncTask<String, Void, String>
{
    private static final String TAG = InstagramPopularTask.class.getSimpleName();
    private OnFetchFinishedListener mOnFetchFinishedListener = OnFetchFinishedListener.NO_OP;

    public void setOnFetchFinishedListener(OnFetchFinishedListener listener)
    {
        mOnFetchFinishedListener = listener;
    }

    @Override
    protected String doInBackground(String... params)
    {
        if (TextUtils.isEmpty(params[0]))
        {
            return null;
        }
        String accessToken = params[0];

        String popularJsonStr = HttpRequestUtil.startHttpRequest("https://api.instagram.com/v1/media/search?lat=37.549696&lng=-122.314780&access_token=" + accessToken, TAG);
        return popularJsonStr;
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        if (TextUtils.isEmpty(s))
        {
            mOnFetchFinishedListener.onFailed();
        }
        else
        {
            mOnFetchFinishedListener.onSuccess(s);
        }
    }
}
