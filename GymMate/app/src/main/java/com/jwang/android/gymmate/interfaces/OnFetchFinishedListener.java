package com.jwang.android.gymmate.interfaces;

/**
 * @author Jiajun Wang on 6/24/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public interface OnFetchFinishedListener
{
    public static OnFetchFinishedListener NO_OP = new OnFetchFinishedListener()
    {
        @Override
        public void onSuccess(String json)
        {
        }

        @Override
        public void onFailed()
        {
        }
    };

    public void onSuccess(String json);

    public void onFailed();
}
