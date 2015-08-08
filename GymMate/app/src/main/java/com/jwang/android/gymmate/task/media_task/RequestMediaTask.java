package com.jwang.android.gymmate.task.media_task;

import android.content.Context;
import android.text.TextUtils;

import com.jwang.android.gymmate.data.MediaContract;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.util.HttpRequestResultUtil;
import com.jwang.android.gymmate.util.HttpRequestUtil;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 7/23/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class RequestMediaTask extends BaseMediaRequestTask
{
    private static final String TAG = RequestMediaTask.class.getSimpleName();

    public RequestMediaTask(Context context)
    {
        super(context, MediaContract.PaginationEntry.TYPE_OTHERS);
    }

    @Override
    protected ArrayList<ModelMedia> requestMedias(String... params)
    {
        ArrayList<ModelMedia> totalMedias = new ArrayList<>();
        if (params.length < 1 || TextUtils.isEmpty(params[0]))
        {
            return totalMedias;
        }
        String endpoint = params[0];
        String mediaResponse = HttpRequestUtil.startHttpRequest(endpoint, TAG);
        //TODO: pagination is wrong
        HttpRequestResultUtil.addMediaToDatabase(mContext, mediaResponse, totalMedias, mDataType, null);

        return totalMedias;
    }
}
