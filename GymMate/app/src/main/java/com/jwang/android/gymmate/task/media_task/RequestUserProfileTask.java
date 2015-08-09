package com.jwang.android.gymmate.task.media_task;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.data.MediaContract;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.util.HttpRequestResultUtil;
import com.jwang.android.gymmate.util.HttpRequestUtil;

import java.util.ArrayList;

/**
 * Created by jiajunwang on 7/2/15.
 */
public class RequestUserProfileTask extends BaseMediaRequestTask
{
    private static final String TAG = RequestUserProfileTask.class.getSimpleName();

    public RequestUserProfileTask(Context context)
    {
        super(context, MediaContract.PaginationEntry.TYPE_USER);
    }

    @Override
    protected ArrayList<ModelMedia> requestMedias(String... params)
    {
        ArrayList<ModelMedia> totalMedias = new ArrayList<>();
        if (params.length < 0 || TextUtils.isEmpty(params[0]))
        {
            Log.e(TAG, "doInBackground: user id is null!");
            return totalMedias;
        }
        String instagramId = params[0];
        Log.d(TAG, "RequestUserProfileTask - doInBackground: user id is " + instagramId);
        String infoEndPoint = "https://api.instagram.com/v1/users/" + instagramId + "/?access_token=" + mAccessToken;

        String infoResponse = HttpRequestUtil.startHttpRequest(infoEndPoint, TAG);
        HttpRequestResultUtil.parseUserInfoJson(mContext, infoResponse);

        return totalMedias;
    }
}
