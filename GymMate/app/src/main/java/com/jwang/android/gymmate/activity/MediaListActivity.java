package com.jwang.android.gymmate.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.fragment.MediaListFragment;
import com.jwang.android.gymmate.interfaces.OnFetchMediaArrayFinishListener;
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
    public static final String KEY_URL = "url_key";
    private FetchPopularMediaTask mFetchPopularMediaTask;
    private String mUrl;

    public static void startActivity(Context context, String url)
    {
        Intent intent = new Intent(context, MediaListActivity.class);
        intent.putExtra(KEY_URL, url);
        context.startActivity(intent);
    }

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
        mUrl = getIntent().getStringExtra(KEY_URL);
        if (TextUtils.isEmpty(mUrl))
        {
            finish();
        }
        mFetchPopularMediaTask = new FetchPopularMediaTask(this);
        mFetchPopularMediaTask.setOnFetchFinishedListener(mOnFetchMediaArrayFinishListener);
        mFetchPopularMediaTask.execute(mUrl);
    }

    private OnFetchMediaArrayFinishListener mOnFetchMediaArrayFinishListener = new OnFetchMediaArrayFinishListener()
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
