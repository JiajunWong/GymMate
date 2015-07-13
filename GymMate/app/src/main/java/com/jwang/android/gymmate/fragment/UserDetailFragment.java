package com.jwang.android.gymmate.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.activity.UserDetailActivity;
import com.jwang.android.gymmate.adapter.UserMediaAdapter;
import com.jwang.android.gymmate.data.MediaContract;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.model.ModelUser;
import com.jwang.android.gymmate.task.FetchUserProfileTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jiajunwang on 7/2/15.
 */
public class UserDetailFragment extends BaseFragment implements
        LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String TAG = UserDetailFragment.class.getSimpleName();
    private static final long ANIM_DURATION = 500;

    private String mUserId;

    private TextView mUserNameTextView;
    private TextView mUserRealNameTextView;
    private TextView mPostsCountTextView;
    private TextView mFollowersCountTextView;
    private TextView mFollowingCountTextView;
    private ImageView mUserAvatarImageView;
    private StaggeredGridView mStaggeredGridView;
    private View mBgView;

    private UserMediaAdapter mUserMediaAdapter;

    private static final int USER_NEAR_LOADER = 0;
    private static final int MEDIA_NEAR_LOADER = 1;

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
        mUserId = getArguments().getString(UserDetailActivity.KEY_USER_ID);
        View rootView = inflater.inflate(R.layout.fragment_user_details, container, false);
        mUserAvatarImageView = (ImageView) rootView.findViewById(R.id.user_photo);
        mUserNameTextView = (TextView) rootView.findViewById(R.id.username);
        mUserRealNameTextView = (TextView) rootView.findViewById(R.id.user_real_name);
        mPostsCountTextView = (TextView) rootView.findViewById(R.id.post_count);
        mFollowersCountTextView = (TextView) rootView.findViewById(R.id.follower_count);
        mFollowingCountTextView = (TextView) rootView.findViewById(R.id.following_count);
        mStaggeredGridView = (StaggeredGridView) rootView.findViewById(R.id.list_item_view);
        mBgView = rootView.findViewById(R.id.container);

        setupWindowAnimations();

        fetchUserInfo();
        return rootView;
    }

    private void fetchUserInfo()
    {
        if (TextUtils.isEmpty(mUserId))
        {
            Log.e(TAG, "fetchUserInfo: the user id is null!");
            getActivity().finish();
        }
        Log.d(TAG, "fetchUserInfo: User id is " + mUserId);

        FetchUserProfileTask fetchUserProfileTask = new FetchUserProfileTask(getActivity());
        fetchUserProfileTask.setOnFetchUserDetailFinishListener(mOnFetchUserDetailFinishListener);
        fetchUserProfileTask.execute(mUserId);
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
            ArrayList<ModelMedia> medias = resultWrapper.modelMediaArrayList;

            if (medias != null)
            {
                mUserMediaAdapter = new UserMediaAdapter(getActivity(), medias);
                mStaggeredGridView.setAdapter(mUserMediaAdapter);
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        getLoaderManager().initLoader(USER_NEAR_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        Uri uri = MediaContract.UserEntry.buildUserWithInstagramId(mUserId);
        return new CursorLoader(getActivity(), uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        if (data.moveToFirst())
        {
            int index_profile_image = data.getColumnIndex(MediaContract.UserEntry.COLUMN_PROFILE_PICTURE);
            String profileImageUrl = data.getString(index_profile_image);

            if (!TextUtils.isEmpty(profileImageUrl))
            {
                Picasso.with(getActivity()).load(profileImageUrl).into(mUserAvatarImageView);
            }

            int index_username = data.getColumnIndex(MediaContract.UserEntry.COLUMN_USERNAME);
            String username = data.getString(index_username);
            if (!TextUtils.isEmpty(username))
            {
                mUserNameTextView.setText(username);
            }

            int index_user_full_name = data.getColumnIndex(MediaContract.UserEntry.COLUMN_FULL_NAME);
            String fullName = data.getString(index_user_full_name);
            if (!TextUtils.isEmpty(fullName))
            {
                mUserRealNameTextView.setText(fullName);
            }

            int index_post_count = data.getColumnIndex(MediaContract.UserEntry.COLUMN_MEDIA_COUNT);
            String mediaCount = data.getString(index_post_count);
            if (!TextUtils.isEmpty(mediaCount))
            {
                mPostsCountTextView.setText(mediaCount);
            }

            int index_follows_count = data.getColumnIndex(MediaContract.UserEntry.COLUMN_FOLLOW_COUNT);
            String followsCount = data.getString(index_follows_count);
            if (!TextUtils.isEmpty(followsCount))
            {
                mFollowingCountTextView.setText(followsCount);
            }

            int index_follow_by_count = data.getColumnIndex(MediaContract.UserEntry.COLUMN_FOLLOWED_BY_COUNT);
            String followByCount = data.getString(index_follow_by_count);
            if (!TextUtils.isEmpty(followByCount))
            {
                mFollowersCountTextView.setText(followByCount);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {

    }

    private void setupWindowAnimations()
    {
        setupEnterAnimations();
//        setupExitAnimations();
    }

    private void setupEnterAnimations()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Transition enterTransition = getActivity().getWindow().getSharedElementEnterTransition();
            enterTransition.addListener(new Transition.TransitionListener()
            {
                @Override
                public void onTransitionStart(Transition transition)
                {
                }

                @Override
                public void onTransitionEnd(Transition transition)
                {
                    animateRevealShow(mBgView);
                }

                @Override
                public void onTransitionCancel(Transition transition)
                {
                }

                @Override
                public void onTransitionPause(Transition transition)
                {
                }

                @Override
                public void onTransitionResume(Transition transition)
                {
                }
            });
        }
    }

    private void setupExitAnimations()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Transition sharedElementReturnTransition = getActivity().getWindow().getSharedElementReturnTransition();
            sharedElementReturnTransition.setStartDelay(ANIM_DURATION);

            Transition returnTransition = getActivity().getWindow().getReturnTransition();
            returnTransition.setDuration(ANIM_DURATION);
            returnTransition.addListener(new Transition.TransitionListener()
            {
                @Override
                public void onTransitionStart(Transition transition)
                {
                    animateRevealHide(mBgView);
                }

                @Override
                public void onTransitionEnd(Transition transition)
                {
                }

                @Override
                public void onTransitionCancel(Transition transition)
                {
                }

                @Override
                public void onTransitionPause(Transition transition)
                {
                }

                @Override
                public void onTransitionResume(Transition transition)
                {
                }
            });
        }
    }

    private void animateRevealShow(View viewRoot)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            int cx = (mUserAvatarImageView.getLeft() + mUserAvatarImageView.getRight()) / 2;
            int cy = (mUserAvatarImageView.getTop() + mUserAvatarImageView.getBottom()) / 2;
            int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
            viewRoot.setVisibility(View.VISIBLE);
            anim.setDuration(ANIM_DURATION);
            anim.start();
        }
    }

    private void animateRevealHide(final View viewRoot)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            int cx = (mUserAvatarImageView.getLeft() + mUserAvatarImageView.getRight()) / 2;
            int cy = (mUserAvatarImageView.getTop() + mUserAvatarImageView.getBottom()) / 2;
            int initialRadius = viewRoot.getWidth();

            Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, initialRadius, 0);
            anim.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    super.onAnimationEnd(animation);
                    viewRoot.setVisibility(View.INVISIBLE);
                }
            });
            anim.setDuration(ANIM_DURATION);
            anim.start();
        }
    }
}
