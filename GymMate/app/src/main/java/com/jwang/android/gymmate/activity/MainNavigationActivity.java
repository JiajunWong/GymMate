package com.jwang.android.gymmate.activity;

import android.os.Bundle;

import com.jwang.android.gymmate.R;

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
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //TODO refresh data
    }
}
