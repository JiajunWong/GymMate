package com.jwang.android.gymmate.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.model.ModelUser;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.InstagramOauth;
import com.jwang.android.gymmate.util.JsonParseUtil;

import java.util.ArrayList;

/**
 * Created by jiajunwang on 7/2/15.
 */
public class FetchUserProfileTask extends AsyncTask<String, Void, Void>
{
    private static final String TAG = FetchUserProfileTask.class.getSimpleName();

    private Context mContext;

    public FetchUserProfileTask(Context context)
    {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params)
    {
        if (params.length < 0 || TextUtils.isEmpty(params[0]))
        {
            Log.e(TAG, "doInBackground: user id is null!");
            return null;
        }
        String instagramId = params[0];
        String accessToken = InstagramOauth.getsInstance(mContext).getSession().getAccessToken();
        String mediaEndPoint = "https://api.instagram.com/v1/users/" + instagramId + "/media/recent/?access_token=" + accessToken;
        String infoEndPoint = "https://api.instagram.com/v1/users/" + instagramId + "/?access_token=" + accessToken;
        //TODO: get more pics
        String infoResponse = HttpRequestUtil.startHttpRequest(infoEndPoint, TAG);
        String mediaResponse = HttpRequestUtil.startHttpRequest(mediaEndPoint, TAG);
        JsonParseUtil.parseGetMediaByLocationResultJson(mContext, mediaResponse);
        JsonParseUtil.parseUserInfoJson(mContext, infoResponse);
        return null;
    }
}
