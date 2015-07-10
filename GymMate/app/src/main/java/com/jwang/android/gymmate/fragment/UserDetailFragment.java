package com.jwang.android.gymmate.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.model.ModelUser;
import com.jwang.android.gymmate.task.FetchUserProfileTask;

import java.util.ArrayList;

/**
 * Created by jiajunwang on 7/2/15.
 */
public class UserDetailFragment extends BaseFragment
{
    private String mUserId;
    private TextView mFollowersTextView;
    private TextView mFollowingTextView;
    private TextView mPostsTextView;

    public UserDetailFragment()
    {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_user_details, container, false);
        mFollowersTextView = (TextView) rootView.findViewById(R.id.followers);
        mFollowingTextView = (TextView) rootView.findViewById(R.id.followings);
        mPostsTextView = (TextView) rootView.findViewById(R.id.posts);

        return rootView;
    }

    public void setResultWrapper(FetchUserProfileTask.ResultWrapper resultWrapper)
    {
        if (resultWrapper == null)
        {
            return;
        }
        ModelUser modelUser = resultWrapper.mModelUser;
        ArrayList<ModelMedia> medias = resultWrapper.modelMediaArrayList;

        if (modelUser != null)
        {
            mFollowingTextView.setText(modelUser.getFollowsCount());
            mFollowersTextView.setText(modelUser.getFollowedByCount());
            mPostsTextView.setText(modelUser.getMediaCount());
        }
    }
}
