package com.jwang.android.gymmate.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.interfaces.OnRequestMediaFinishWithTimeStampListener;
import com.jwang.android.gymmate.util.HttpRequestResultUtil;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.InstagramOauth;

import java.util.ArrayList;

/**
 * Created by jiajunwang on 7/2/15.
 */
public class FetchUserProfileTask extends AsyncTask<String, Void, String>
{
    private static final String TAG = FetchUserProfileTask.class.getSimpleName();
    private Context mContext;
    private OnRequestMediaFinishWithTimeStampListener mOnRequestMediaFinishWithTimeStampListener = OnRequestMediaFinishWithTimeStampListener.NO_OP;

    public FetchUserProfileTask(Context context)
    {
        mContext = context;
    }

    public void setOnFetchUserInfoFinishedListener(OnRequestMediaFinishWithTimeStampListener listener)
    {
        mOnRequestMediaFinishWithTimeStampListener = listener;
    }

    @Override
    protected String doInBackground(String... params)
    {
        if (params.length < 0 || TextUtils.isEmpty(params[0]))
        {
            Log.e(TAG, "doInBackground: user id is null!");
            return null;
        }
        String instagramId = params[0];
        String accessToken = InstagramOauth.getsInstance().getSession().getAccessToken();
        String mediaEndPoint = "https://api.instagram.com/v1/users/" + instagramId + "/media/recent/?access_token=" + accessToken + "&count=20";
        String infoEndPoint = "https://api.instagram.com/v1/users/" + instagramId + "/?access_token=" + accessToken;

        String infoResponse = HttpRequestUtil.startHttpRequest(infoEndPoint, TAG);
        HttpRequestResultUtil.parseUserInfoJson(mContext, infoResponse);

        String mediaResponse = HttpRequestUtil.startHttpRequest(mediaEndPoint, TAG);
        ArrayList<String> minCreateTimes = new ArrayList<>();
        HttpRequestResultUtil.addMediaToDB(mContext, mediaResponse, HttpRequestResultUtil.RequestMediaType.USER, instagramId, true, null, minCreateTimes);
        if (minCreateTimes.isEmpty())
        {
            return null;
        }
        return minCreateTimes.get(0);
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
