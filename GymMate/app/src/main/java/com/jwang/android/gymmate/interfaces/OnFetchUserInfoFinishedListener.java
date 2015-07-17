package com.jwang.android.gymmate.interfaces;

import com.jwang.android.gymmate.model.ModelMedia;

import java.util.ArrayList;

/**
 * Created by jiajunwang on 7/16/15.
 */
public interface OnFetchUserInfoFinishedListener
{
    public static OnFetchUserInfoFinishedListener NO_OP = new OnFetchUserInfoFinishedListener()
    {
        @Override
        public void onFetchFinished(String paginationUrl)
        {
        }
    };

    public void onFetchFinished(String paginationUrl);
}
