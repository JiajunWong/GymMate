package com.jwang.android.gymmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jwang.android.gymmate.util.InstagramOauth;
import com.jwang.android.gymmate.R;

import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramUser;

/**
 * Created by jiajunwang on 6/23/15.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener
{
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button mLogInButton;

    private InstagramOauth mInstagramOauth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mInstagramOauth = InstagramOauth.getsInstance(this);
        if (!mInstagramOauth.getSession().isActive())
        {
            //log in
            setContentView(R.layout.activity_login);
            mLogInButton = (Button) findViewById(R.id.btn_log_in);
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
            case R.id.btn_log_in:
                //show instagram log in screen.
                mInstagramOauth.authorize(mAuthListener);
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
