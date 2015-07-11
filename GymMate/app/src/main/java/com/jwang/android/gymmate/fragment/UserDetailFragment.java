package com.jwang.android.gymmate.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.activity.UserDetailActivity;
import com.jwang.android.gymmate.adapter.UserMediaAdapter;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.model.ModelUser;
import com.jwang.android.gymmate.task.FetchUserProfileTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jiajunwang on 7/2/15.
 */
public class UserDetailFragment extends BaseFragment
{
    private static final String TAG = UserDetailFragment.class.getSimpleName();

    private TextView mUserNameTextView;
    private TextView mUserRealNameTextView;
    private TextView mPostsCountTextView;
    private TextView mFollowersCountTextView;
    private TextView mFollowingCountTextView;
    private ImageView mUserAvatarImageView;
    private StaggeredGridView mStaggeredGridView;

    private UserMediaAdapter mUserMediaAdapter;

    public static UserDetailFragment newInstance(String userId)
    {
        UserDetailFragment userDetailFragment = new UserDetailFragment();

        Bundle args = new Bundle();
        args.putString(UserDetailActivity.KEY_USER_ID, userId);
        userDetailFragment.setArguments(args);

        return userDetailFragment;
    }

    public UserDetailFragment()
    {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_user_details, container, false);
        mUserAvatarImageView = (ImageView) rootView.findViewById(R.id.user_photo);
        mUserNameTextView = (TextView) rootView.findViewById(R.id.username);
        mUserRealNameTextView = (TextView) rootView.findViewById(R.id.user_real_name);
        mPostsCountTextView = (TextView) rootView.findViewById(R.id.post_count);
        mFollowersCountTextView = (TextView) rootView.findViewById(R.id.follower_count);
        mFollowingCountTextView = (TextView) rootView.findViewById(R.id.following_count);
        mStaggeredGridView = (StaggeredGridView) rootView.findViewById(R.id.list_item_view);

        fetchUserInfo();
        return rootView;
    }

    private void fetchUserInfo()
    {
        String userId = getArguments().getString(UserDetailActivity.KEY_USER_ID);
        if (TextUtils.isEmpty(userId))
        {
            Log.e(TAG, "fetchUserInfo: the user id is null!");
            getActivity().finish();
        }
        Log.d(TAG, "fetchUserInfo: User id is " + userId);

        FetchUserProfileTask fetchUserProfileTask = new FetchUserProfileTask(getActivity());
        fetchUserProfileTask.setOnFetchUserDetailFinishListener(mOnFetchUserDetailFinishListener);
        fetchUserProfileTask.execute(userId);
    }

    private FetchUserProfileTask.OnFetchUserDetailFinishListener mOnFetchUserDetailFinishListener = new FetchUserProfileTask.OnFetchUserDetailFinishListener()
    {
        @Override
        public void onFinished(FetchUserProfileTask.ResultWrapper resultWrapper)
        {
            if (resultWrapper == null)
            {
                return;
            }
            ModelUser modelUser = resultWrapper.mModelUser;
            ArrayList<ModelMedia> medias = resultWrapper.modelMediaArrayList;

            if (modelUser != null)
            {
                if (!TextUtils.isEmpty(modelUser.getProfilePicture()))
                {
                    Picasso.with(getActivity()).load(modelUser.getProfilePicture()).into(mUserAvatarImageView);
                }
                if (!TextUtils.isEmpty(modelUser.getUserName()))
                {
                    mUserNameTextView.setText(modelUser.getUserName());
                }
                if (!TextUtils.isEmpty(modelUser.getFullName()))
                {
                    mUserRealNameTextView.setText(modelUser.getFullName());
                }
                mPostsCountTextView.setText(modelUser.getMediaCount() + "");
                mFollowingCountTextView.setText(modelUser.getFollowsCount() + "");
                mFollowersCountTextView.setText(modelUser.getFollowedByCount() + "");
            }

            if (medias != null)
            {
                mUserMediaAdapter = new UserMediaAdapter(getActivity(), medias);
                mStaggeredGridView.setAdapter(mUserMediaAdapter);
            }
        }
    };
}
