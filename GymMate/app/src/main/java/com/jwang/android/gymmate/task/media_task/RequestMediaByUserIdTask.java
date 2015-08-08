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
public class RequestMediaByUserIdTask extends BaseMediaRequestTask
{
    private static final String TAG = RequestMediaByUserIdTask.class.getSimpleName();

    public RequestMediaByUserIdTask(Context context)
    {
        super(context, MediaContract.PaginationEntry.TYPE_USER);
    }

    @Override
    protected ArrayList<ModelMedia> requestMedias(String... params)
    {
        ArrayList<ModelMedia> totalMedias = new ArrayList<>();
        if (params == null || params.length < 1 || TextUtils.isEmpty(params[0]))
        {
            Log.e(TAG, "doInBackground: user id is null!");
            return totalMedias;
        }
        Log.d(TAG, "RequestMediaByUserIdTask -- doInBackground");

        String userId = params[0];
        String paginationUrl = getPaginationUrl(userId);

        String endPoint;
        if (!TextUtils.isEmpty(paginationUrl))
        {
            //do load more
            endPoint = paginationUrl;
            Log.e(TAG, "RequestMediaByUserIdTask -- doInBackground: pagination url is " + paginationUrl);
        }
        else
        {
            //do original request
            Log.e(TAG, "RequestMediaByUserIdTask -- doInBackground: pagination url is null");
            endPoint = "https://api.instagram.com/v1/users/" + userId + "/media/recent/?access_token=" + mAccessToken + "&count=20";
        }

        String mediaResponse = HttpRequestUtil.startHttpRequest(endPoint, TAG);
        HttpRequestResultUtil.addMediaToDatabase(mContext, mediaResponse, totalMedias, mDataType, userId);

        return totalMedias;
    }
}
