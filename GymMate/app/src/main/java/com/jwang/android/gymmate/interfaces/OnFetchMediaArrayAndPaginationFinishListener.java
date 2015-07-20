package com.jwang.android.gymmate.interfaces;

import com.jwang.android.gymmate.util.JsonParseUtil;

/**
 * Created by jiajunwang on 7/16/15.
 */
public interface OnFetchMediaArrayAndPaginationFinishListener
{
    public static OnFetchMediaArrayAndPaginationFinishListener NO_OP = new OnFetchMediaArrayAndPaginationFinishListener()
    {
        @Override
        public void onFetchFinished(JsonParseUtil.ResultWrapper paginationUrl)
        {
        }
    };

    public void onFetchFinished(JsonParseUtil.ResultWrapper paginationUrl);
}
