package com.jwang.android.gymmate.activity;

import android.os.Bundle;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.adapter.MediaSyncAdapter;
import com.jwang.android.gymmate.fragment.MediaListFragment;

/**
 * @author Jiajun Wang on 6/24/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MainNavigationActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        MediaSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        MediaListFragment mediaListFragment = (MediaListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_media_list);
        mediaListFragment.refreshData();
    }
}
