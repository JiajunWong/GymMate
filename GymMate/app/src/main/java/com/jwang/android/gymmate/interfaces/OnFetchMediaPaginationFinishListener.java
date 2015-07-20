package com.jwang.android.gymmate.interfaces;

/**
 * Created by jiajunwang on 7/16/15.
 */
public interface OnFetchMediaPaginationFinishListener
{
    public static OnFetchMediaPaginationFinishListener NO_OP = new OnFetchMediaPaginationFinishListener()
    {
        @Override
        public void onFetchFinished(String paginationUrl)
        {
        }
    };

    public void onFetchFinished(String paginationUrl);
}