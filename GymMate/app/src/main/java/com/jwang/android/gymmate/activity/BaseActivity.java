package com.jwang.android.gymmate.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jwang.android.gymmate.R;

public abstract class BaseActivity extends AppCompatActivity
{
    protected Toolbar mToolbar;
    protected ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    protected void setupActionBar()
    {
        // Use Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null)
        {
            setSupportActionBar(mToolbar);

            mActionBar = getSupportActionBar();
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayShowTitleEnabled(false);

            try
            {
                final String label = getString(getPackageManager().getActivityInfo(getComponentName(), 0).labelRes);
                if (!TextUtils.isEmpty(label))
                {
                    setTitle(label);
                }
            }
            catch (final Exception e)
            {
            }
        }
        else
        {
            mActionBar = getSupportActionBar();
        }

    }
}
