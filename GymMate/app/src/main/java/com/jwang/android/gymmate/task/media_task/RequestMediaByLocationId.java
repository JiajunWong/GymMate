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
 * @author Jiajun Wang on 7/30/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class RequestMediaByLocationId extends BaseMediaRequestTask
{
    private static final String TAG = RequestMediaByLocationId.class.getSimpleName();

    public RequestMediaByLocationId(Context context)
    {
        super(context, MediaContract.PaginationEntry.TYPE_LOCATION);
    }

    @Override
    protected ArrayList<ModelMedia> requestMedias(String... params)
    {
        ArrayList<ModelMedia> totalMedias = new ArrayList<>();
        if (params == null || params.length < 1 || TextUtils.isEmpty(params[0]))
        {
            Log.e(TAG, "RequestMediaByLocationId -- doInBackground: location id and timeStamps is null!");
            return totalMedias;
        }
        String locationId = params[0];
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
        HttpRequestResultUtil.addMediaToDatabase(mContext, mediaResponse, totalMedias, mDataType, locationId);

        return totalMedias;
    }
}
