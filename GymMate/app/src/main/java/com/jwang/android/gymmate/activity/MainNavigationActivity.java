package com.jwang.android.gymmate.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jwang.android.gymmate.util.InstagramOauth;
import com.jwang.android.gymmate.R;
import com.squareup.picasso.Picasso;

import net.londatiga.android.instagram.InstagramUser;

/**
 * @author Jiajun Wang on 6/24/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MainNavigationActivity extends BaseActivity
{
    private ImageView mUserAvatarImageView;
    private TextView mUserNameTextView;
    private TextView mUserAccessTokenTextView;

    private InstagramOauth mInstagramOauth;
    private InstagramUser mInstagramUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);

        mUserAvatarImageView = (ImageView) findViewById(R.id.user_avatar);
        mUserNameTextView = (TextView) findViewById(R.id.user_name);
        mUserAccessTokenTextView = (TextView) findViewById(R.id.user_access_token);

        mInstagramOauth = InstagramOauth.getsInstance(this);
        mInstagramUser = mInstagramOauth.getSession().getUser();

        if (mInstagramUser != null)
        {
            if (!TextUtils.isEmpty(mInstagramUser.username))
            {
                mUserNameTextView.setText(mInstagramUser.fullName);
            }

            if (!TextUtils.isEmpty(mInstagramUser.accessToken))
            {
                mUserAccessTokenTextView.setText(mInstagramUser.accessToken);
            }

            if (!TextUtils.isEmpty(mInstagramUser.profilPicture))
            {
                Picasso.with(this).load(mInstagramUser.profilPicture).fit().into(mUserAvatarImageView);
            }
        }
    }
}
