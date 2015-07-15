package com.jwang.android.gymmate.interfaces;

/**
 * @author Jiajun Wang on 7/15/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public interface OnRefreshListener
{
    public static final OnRefreshListener NO_OP = new OnRefreshListener()
    {
        @Override
        public void onRefresh()
        {
        }
    };

    public void onRefresh();
}
