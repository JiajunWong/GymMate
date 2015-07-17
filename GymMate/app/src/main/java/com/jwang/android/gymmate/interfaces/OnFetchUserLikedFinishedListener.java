package com.jwang.android.gymmate.interfaces;

import com.jwang.android.gymmate.util.JsonParseUtil;

/**
 * Created by jiajunwang on 7/16/15.
 */
public interface OnFetchUserLikedFinishedListener
{
    public static OnFetchUserLikedFinishedListener NO_OP = new OnFetchUserLikedFinishedListener()
    {
        @Override
        public void onFetchFinished(JsonParseUtil.ResultWrapper paginationUrl)
        {
        }
    };

    public void onFetchFinished(JsonParseUtil.ResultWrapper paginationUrl);
}
