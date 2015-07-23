package com.jwang.android.gymmate.interfaces;

/**
 * Created by jiajunwang on 7/16/15.
 */
public interface OnRequestMediaFinishWithTimeStampListener
{
    public static OnRequestMediaFinishWithTimeStampListener NO_OP = new OnRequestMediaFinishWithTimeStampListener()
    {
        @Override
        public void onFetchFinished(String paginationUrl)
        {
        }
    };

    public void onFetchFinished(String paginationUrl);
}
