package com.jwang.android.gymmate.interfaces;

import com.jwang.android.gymmate.model.ModelMedia;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 7/14/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public interface OnFetchFinishedListener
{
    public static OnFetchFinishedListener NO_OP = new OnFetchFinishedListener()
    {
        @Override
        public void onFetchFinished(ArrayList<ModelMedia> medias)
        {
        }
    };

    public void onFetchFinished(ArrayList<ModelMedia> medias);
}
