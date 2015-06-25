package com.jwang.android.gymmate.task;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.jwang.android.gymmate.interfaces.OnFetchFinishedListener;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.JsonParseUtil;

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
        String accessToken = params[0];

        String popularJsonStr = HttpRequestUtil.startHttpRequest("https://api.instagram.com/v1/media/search?lat=37.549696&lng=-122.314780&access_token=" + accessToken, TAG);
        return JsonParseUtil.parseMediaSearchByLocationJsonResult(popularJsonStr);
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
