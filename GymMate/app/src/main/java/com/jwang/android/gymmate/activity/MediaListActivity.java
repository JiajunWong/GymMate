package com.jwang.android.gymmate.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.fragment.media_list_fragment.MediaListFragment;
import com.jwang.android.gymmate.interfaces.OnRefreshListener;
import com.jwang.android.gymmate.interfaces.OnRequestMediaFinishListener;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.task.media_task.BaseMediaRequestTask;
import com.jwang.android.gymmate.task.media_task.RequestMediaTask;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 7/14/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MediaListActivity extends BaseActivity implements
        OnRefreshListener
{
    public static final String KEY_URL = "url_key";
    private RequestMediaTask requestUserMediaTask;
    private MediaListFragment mMediaListFragment;
    private ArrayList<ModelMedia> mMedias = new ArrayList<>();
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
        mMediaListFragment = new MediaListFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, mMediaListFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        ArrayList<ModelMedia> mediaArrayList = (ArrayList<ModelMedia>) getLastCustomNonConfigurationInstance();
        if (mediaArrayList != null && !mediaArrayList.isEmpty())
        {
            mMediaListFragment.setModelMedias(mMedias);
        }
        else
        {
            setFetchTask();
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance()
    {
        return mMedias;
    }

    private void setFetchTask()
    {
        mUrl = getIntent().getStringExtra(KEY_URL);
        if (TextUtils.isEmpty(mUrl))
        {
            finish();
        }
        requestUserMediaTask = new RequestMediaTask(this);
        requestUserMediaTask.setOnRequestMediaFinishListener(mOnRequestMediaFinishListener);
        requestUserMediaTask.execute(mUrl);
    }

    private OnRequestMediaFinishListener mOnRequestMediaFinishListener = new OnRequestMediaFinishListener()
    {
        @Override
        public void onFetchFinished(ArrayList<ModelMedia> medias)
        {
            if (medias != null && !medias.isEmpty())
            {
                mMedias = medias;
                mMediaListFragment.setModelMedias(medias);
            }
        }
    };

    @Override
    public void onRefresh()
    {
        if (requestUserMediaTask.getStatus() == AsyncTask.Status.FINISHED)
        {
            requestUserMediaTask.cancel(false);
            setFetchTask();
        }
    }
}
