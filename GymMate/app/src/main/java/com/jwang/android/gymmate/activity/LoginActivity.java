package com.jwang.android.gymmate.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.util.InstagramOauth;
import com.jwang.android.gymmate.util.MediaSyncWorker;

import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramUser;

/**
 * Created by jiajunwang on 6/23/15.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener
{
    private static final String TAG = LoginActivity.class.getSimpleName();
    private View mLogInButton;

    private InstagramOauth mInstagramOauth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mInstagramOauth = InstagramOauth.getsInstance();
        if (getIntent().getBooleanExtra(MediaSyncWorker.KEY_START_FROM_NOTIFICATION, false))
        {
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(MediaSyncWorker.MEDIA_NOTIFICATION_ID);
        }

        if (!mInstagramOauth.getSession().isActive())
        {
            //log in
            setContentView(R.layout.activity_login);
            mLogInButton = findViewById(R.id.tv_log_in);
            mLogInButton.setOnClickListener(this);
        }
        else
        {
            // Already logged in
            startMainActivity();
        }
    }

    private void startMainActivity()
    {
        Intent intent = new Intent(LoginActivity.this, MainNavigationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tv_log_in:
                //show instagram log in screen.
                mInstagramOauth.authorize(this, mAuthListener);
                break;
        }
    }

    private Instagram.InstagramAuthListener mAuthListener = new Instagram.InstagramAuthListener()
    {
        @Override
        public void onSuccess(InstagramUser user)
        {
            Log.v(TAG, "Author success.");
            startMainActivity();
        }

        @Override
        public void onError(String error)
        {
            Log.e(TAG, "Author failed.");
        }

        @Override
        public void onCancel()
        {
            Log.w(TAG, "Author canceled.");
        }
    };
}
