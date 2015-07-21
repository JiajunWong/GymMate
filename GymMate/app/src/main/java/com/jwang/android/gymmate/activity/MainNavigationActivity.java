package com.jwang.android.gymmate.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.adapter.MediaSyncAdapter;
import com.jwang.android.gymmate.fragment.MainMediaListFragment;

/**
 * @author Jiajun Wang on 6/24/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MainNavigationActivity extends BaseActivity
{
    protected DrawerLayout mDrawerLayout;
    protected View mDrawerRootLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        mToolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu);
        mDrawerRootLeft = findViewById(R.id.drawer_layout_left);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        MediaSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        MainMediaListFragment mediaListFragment = (MainMediaListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_media_list);
        mediaListFragment.refreshData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                toggleLeftDrawer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toggleLeftDrawer()
    {
        if (mDrawerLayout.isDrawerOpen(mDrawerRootLeft))
        {
            mDrawerLayout.closeDrawer(mDrawerRootLeft);
        }
        else
        {
            mDrawerLayout.openDrawer(mDrawerRootLeft);
        }
    }

    public boolean isLeftDrawerOpen()
    {
        if (mDrawerLayout == null)
        {
            return false;
        }
        return (mDrawerLayout.isDrawerOpen(mDrawerRootLeft));
    }

    @Override
    public void onBackPressed()
    {
        if (isLeftDrawerOpen())
        {
            toggleLeftDrawer();
        }
        else
        {
            super.onBackPressed();
        }
    }

}
