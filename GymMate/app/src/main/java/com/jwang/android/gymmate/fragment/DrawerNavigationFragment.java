package com.jwang.android.gymmate.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.activity.UserDetailActivity;
import com.jwang.android.gymmate.adapter.DrawerNavigationAdapter;
import com.jwang.android.gymmate.util.InstagramOauth;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import net.londatiga.android.instagram.InstagramSession;
import net.londatiga.android.instagram.InstagramUser;

/**
 * @author Jiajun Wang on 7/14/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class DrawerNavigationFragment extends BaseFragment implements
        View.OnClickListener
{
    private RoundedImageView mRoundedImageView;
    private TextView mUsernameTextView;
    private ListView mListView;

    private DrawerNavigationAdapter mDrawerNavigationAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_drawer_navigation, container, false);
        initComponent(rootView);
        return rootView;
    }

    private void initComponent(View rootView)
    {
        mRoundedImageView = (RoundedImageView) rootView.findViewById(R.id.user_photo);
        mUsernameTextView = (TextView) rootView.findViewById(R.id.username);
        mRoundedImageView.setOnClickListener(this);
        mUsernameTextView.setOnClickListener(this);
        mListView = (ListView) rootView.findViewById(R.id.drawer_list_view);
        mDrawerNavigationAdapter = new DrawerNavigationAdapter(getActivity());
        mListView.setAdapter(mDrawerNavigationAdapter);

        InstagramSession instagramSession = InstagramOauth.getsInstance().getSession();
        if (instagramSession != null && instagramSession.isActive() && instagramSession.getUser() != null)
        {
            InstagramUser owner = instagramSession.getUser();
            if (!TextUtils.isEmpty(owner.username))
            {
                mUsernameTextView.setText(owner.username);
            }
            if (!TextUtils.isEmpty(owner.profilPicture))
            {
                Picasso.with(getActivity()).load(owner.profilPicture).into(mRoundedImageView);
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.user_photo:
            case R.id.username:
                InstagramUser user = InstagramOauth.getsInstance().getSession().getUser();
                UserDetailActivity.startActivity(getActivity(), v, user.id);
                break;
        }
    }
}
