package com.jwang.android.gymmate.interfaces;

import com.jwang.android.gymmate.model.ModelMedia;

/**
 * Created by jiajunwang on 7/18/15.
 */
public interface OnFetchMediaObjectFinishListener
{
    public static OnFetchMediaObjectFinishListener NO_OP = new OnFetchMediaObjectFinishListener()
    {
        @Override
        public void onFetchFinished(ModelMedia modelMedia)
        {
        }
    };

    public void onFetchFinished(ModelMedia modelMedia);
}
