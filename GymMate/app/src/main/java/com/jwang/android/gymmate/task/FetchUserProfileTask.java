package com.jwang.android.gymmate.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.interfaces.OnFetchMediaPaginationFinishListener;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.InstagramOauth;
import com.jwang.android.gymmate.util.JsonParseUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by jiajunwang on 7/2/15.
 */
public class FetchUserProfileTask extends AsyncTask<String, Void, String>
{
    private static final String TAG = FetchUserProfileTask.class.getSimpleName();
    private Context mContext;
    private OnFetchMediaPaginationFinishListener mOnFetchMediaPaginationFinishListener = OnFetchMediaPaginationFinishListener.NO_OP;

    public FetchUserProfileTask(Context context)
    {
        mContext = context;
    }

    public void setOnFetchUserInfoFinishedListener(OnFetchMediaPaginationFinishListener listener)
    {
        mOnFetchMediaPaginationFinishListener = listener;
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
        String mediaResponse = HttpRequestUtil.startHttpRequest(mediaEndPoint, TAG);
        JsonParseUtil.parseUserInfoJson(mContext, infoResponse);
        HashSet<String> paginations = new HashSet<>();
        JsonParseUtil.parseInstagramMediaJson(mContext, mediaResponse, true, new ArrayList<ModelMedia>(), paginations);
        Iterator<String> iterator = paginations.iterator();
        return iterator.next();
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        mOnFetchMediaPaginationFinishListener.onFetchFinished(s);
    }
}
