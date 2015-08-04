package com.jwang.android.gymmate.task.media_task;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.data.MediaContract;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.util.HttpRequestResultUtil;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.MediaSyncWorker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by jiajunwang on 7/2/15.
 */
public class RequestMainLocationMediaTask extends BaseMediaRequestTask
{
    private static final String TAG = RequestMainLocationMediaTask.class.getSimpleName();

    public RequestMainLocationMediaTask(Context context)
    {
        super(context, MediaContract.PaginationEntry.TYPE_LOCATION);
    }

    @Override
    protected ArrayList<ModelMedia> requestMedias(String... params)
    {
        ArrayList<String> locations = MediaSyncWorker.getInstance(mContext).getInstagramLocationIds();
        ArrayList<ModelMedia> totalMedias = new ArrayList<>();

        if (locations.isEmpty())
        {
            Log.e(TAG, "RequestMainLocationMediaTask -- requestMedias: timeStamps array is null!");
            return totalMedias;
        }
        else
        {
            Log.d(TAG, "RequestMainLocationMediaTask -- locations size is " + locations.size());
        }

        for (String locationId : locations)
        {
            ArrayList<ModelMedia> newMedias = new ArrayList<>();
            String paginationUrl = getPaginationUrl(locationId);

            String endPoint = "";
            if (!TextUtils.isEmpty(paginationUrl))
            {
                //do load more
                endPoint = paginationUrl;
            }
            else
            {
                //do original request
                endPoint = "https://api.instagram.com/v1/locations/" + locationId + "/media/recent?access_token=" + mAccessToken;
            }

            String mediaResponse = HttpRequestUtil.startHttpRequest(endPoint, TAG);
            HttpRequestResultUtil.addMediaToDatabase(mContext, mediaResponse, newMedias, mDataType, locationId);
            totalMedias.addAll(newMedias);
        }

        return totalMedias;
    }
}
