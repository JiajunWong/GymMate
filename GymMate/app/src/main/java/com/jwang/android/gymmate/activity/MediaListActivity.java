package com.jwang.android.gymmate.activity;

import android.os.AsyncTask;
import android.os.Bundle;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.fragment.MediaListFragment;
import com.jwang.android.gymmate.interfaces.OnFetchFinishedListener;
import com.jwang.android.gymmate.interfaces.OnRefreshListener;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.task.FetchPopularMediaTask;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 7/14/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MediaListActivity extends BaseActivity implements
        OnRefreshListener
{
    private FetchPopularMediaTask mFetchPopularMediaTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_list);
        setDisplayHomeAsUpEnabled(true);

        setFetchTask();
    }

    private void setFetchTask()
    {
        mFetchPopularMediaTask = new FetchPopularMediaTask(this);
        mFetchPopularMediaTask.setOnFetchFinishedListener(mOnFetchFinishedListener);
        mFetchPopularMediaTask.execute();
    }

    private OnFetchFinishedListener mOnFetchFinishedListener = new OnFetchFinishedListener()
    {
        @Override
        public void onFetchFinished(ArrayList<ModelMedia> medias)
        {
            if (medias != null)
            {
                MediaListFragment mediaListFragment = (MediaListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_media_list);
                mediaListFragment.setModelMedias(medias);
            }
        }
    };

    @Override
    public void onRefresh()
    {
        if (mFetchPopularMediaTask.getStatus() == AsyncTask.Status.FINISHED)
        {
            mFetchPopularMediaTask.cancel(false);
            setFetchTask();
        }
    }
}
