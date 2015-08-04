package com.jwang.android.gymmate.interfaces;

import java.util.ArrayList;

import com.jwang.android.gymmate.model.ModelMedia;

/**
 * @author Jiajun Wang on 7/14/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public interface OnRequestMediaFinishListener
{
    public static OnRequestMediaFinishListener NO_OP = new OnRequestMediaFinishListener()
    {
        @Override
        public void onFetchFinished(ArrayList<ModelMedia> medias)
        {
        }
    };

    public void onFetchFinished(ArrayList<ModelMedia> medias);
}
