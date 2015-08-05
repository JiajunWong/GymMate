package com.jwang.android.gymmate.interfaces;

/**
 * @author Jiajun Wang on 8/4/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public interface OnLoadMoreMediaListener
{
    public OnLoadMoreMediaListener NO_OP = new OnLoadMoreMediaListener()
    {
        @Override
        public void loadMore()
        {
        }
    };

    public void loadMore();
}
