package com.jwang.android.gymmate.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.adapter.MediaAdapter;
import com.jwang.android.gymmate.interfaces.OnFetchFinishedListener;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.task.InstagramMediaTask;
import com.jwang.android.gymmate.util.InstagramOauth;
import com.squareup.picasso.Picasso;

import net.londatiga.android.instagram.InstagramUser;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 6/24/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MainNavigationActivity extends BaseActivity
{
    private ImageView mUserAvatarImageView;
    private TextView mUserNameTextView;
    private ListView mListView;
    private MediaAdapter mMediaAdapter;

    private InstagramOauth mInstagramOauth;
    private InstagramUser mInstagramUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);

        mUserAvatarImageView = (ImageView) findViewById(R.id.user_avatar);
        mUserNameTextView = (TextView) findViewById(R.id.user_name);
        mListView = (ListView) findViewById(R.id.lv_medias);
        mMediaAdapter = new MediaAdapter(this);
        mListView.setAdapter(mMediaAdapter);

        mInstagramOauth = InstagramOauth.getsInstance(this);
        mInstagramUser = mInstagramOauth.getSession().getUser();

        if (mInstagramUser != null)
        {
            if (!TextUtils.isEmpty(mInstagramUser.username))
            {
                mUserNameTextView.setText(mInstagramUser.fullName);
            }

            if (!TextUtils.isEmpty(mInstagramUser.profilPicture))
            {
                Picasso.with(this).load(mInstagramUser.profilPicture).fit().into(mUserAvatarImageView);
            }
        }

        InstagramMediaTask instagramMediaTask = new InstagramMediaTask();
        instagramMediaTask.setOnFetchFinishedListener(mOnFetchFinishedListener);
        instagramMediaTask.execute(mInstagramOauth.getSession().getAccessToken());
    }

    private OnFetchFinishedListener mOnFetchFinishedListener = new OnFetchFinishedListener()
    {
        @Override
        public void onSuccess(ArrayList<ModelMedia> medias)
        {
            if (medias != null && medias.size() > 0)
            {
                mMediaAdapter.setModelMedias(medias);
                mMediaAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailed()
        {
        }
    };
}
