package com.jwang.android.gymmate.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.util.AndroidUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class BaseActivity extends AppCompatActivity
{
    protected Toolbar mToolbar;
    protected ActionBar mActionBar;

    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    protected void setDisplayHomeAsUpEnabled(boolean enabled)
    {
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(enabled);
        }
    }

    protected void showErrorState(View rootView)
    {
        if (rootView != null)
        {
            // if cursor is empty, why? do we have an invalid location
            int message = R.string.empty_media_list;
            @LocationStatus
            int location = AndroidUtil.getLocationStatus(this);
            switch (location)
            {
                case LOCATION_STATUS_SERVER_DOWN:
                    message = R.string.empty_media_list_server_down;
                    break;
                case LOCATION_STATUS_SERVER_INVALID:
                    message = R.string.empty_media_list_server_error;
                    break;
                default:
                    if (!AndroidUtil.isNetworkAvailable(this))
                    {
                        message = R.string.empty_media_list_no_network;
                    }
            }
            Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ LOCATION_STATUS_OK, LOCATION_STATUS_SERVER_DOWN, LOCATION_STATUS_SERVER_INVALID, LOCATION_STATUS_UNKNOWN, LOCATION_STATUS_INVALID })
    public @interface LocationStatus
    {
    }

    public static final int LOCATION_STATUS_OK = 0;
    public static final int LOCATION_STATUS_SERVER_DOWN = 1;
    public static final int LOCATION_STATUS_SERVER_INVALID = 2;
    public static final int LOCATION_STATUS_UNKNOWN = 3;
    public static final int LOCATION_STATUS_INVALID = 4;

    /**
     * Sets the location status into shared preference.  This function should not be called from
     * the UI thread because it uses commit to write to the shared preferences.
     * @param c Context to get the PreferenceManager from.
     * @param locationStatus The IntDef value to set
     */
    public static void setLocationStatus(Context c, @BaseActivity.LocationStatus int locationStatus)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt(c.getString(R.string.pref_location_status_key), locationStatus);
        spe.commit();
    }
}
