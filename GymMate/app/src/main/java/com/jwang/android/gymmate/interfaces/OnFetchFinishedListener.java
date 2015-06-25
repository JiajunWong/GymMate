package com.jwang.android.gymmate.interfaces;

import com.jwang.android.gymmate.model.ModelMedia;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 6/24/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public interface OnFetchFinishedListener
{
    public static OnFetchFinishedListener NO_OP = new OnFetchFinishedListener()
    {
        @Override
        public void onSuccess(ArrayList<ModelMedia> medias)
        {
        }

        @Override
        public void onFailed()
        {
        }
    };

    public void onSuccess(ArrayList<ModelMedia> medias);

    public void onFailed();
}
