package com.jwang.android.gymmate.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.activity.UserDetailActivity;
import com.jwang.android.gymmate.adapter.UserMediaAdapter;
import com.jwang.android.gymmate.data.MediaContract;
import com.jwang.android.gymmate.interfaces.OnFetchMediaPaginationFinishListener;
import com.jwang.android.gymmate.task.FetchMediaWithStoreAndPaginationTask;
import com.jwang.android.gymmate.task.FetchUserProfileTask;
import com.jwang.android.gymmate.util.AnimationUtil;
import com.jwang.android.gymmate.view.HeaderGridView;
import com.squareup.picasso.Picasso;

/**
 * Created by jiajunwang on 7/2/15.
 */
public class UserDetailFragment extends BaseFragment implements
        LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String TAG = UserDetailFragment.class.getSimpleName();
    private String mUserId;
    private String mPaginationUrl;
    private int mMediaCount;

    private TextView mUserNameTextView;
    private TextView mUserRealNameTextView;
    private TextView mPostsCountTextView;
    private TextView mFollowersCountTextView;
    private TextView mFollowingCountTextView;
    private ImageView mUserAvatarImageView;
    private HeaderGridView mStaggeredGridView;
    private View mBgView;
    private UserMediaAdapter mUserMediaAdapter;

    private int mHeaderHeight;
    private int mMinHeaderTranslation;
    private View mPlaceHolderView;
    private View mHeader;

    private FetchMediaWithStoreAndPaginationTask mFetchMediaWithStoreAndPaginationTask;

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
        mStaggeredGridView = (HeaderGridView) rootView.findViewById(R.id.list_item_view);
        mBgView = rootView.findViewById(R.id.container);
        mHeader = rootView.findViewById(R.id.user_profile_root);

        mUserMediaAdapter = new UserMediaAdapter(getActivity(), null, 0);

        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.fake_header_height);
        mMinHeaderTranslation = -mHeaderHeight;
        setupListView();

        AnimationUtil.activityRevealTransition(getActivity(), mUserAvatarImageView, mBgView);

        fetchUserInfo();
        return rootView;
    }

    private void setupListView()
    {
        mPlaceHolderView = getLayoutInflater(getArguments()).inflate(R.layout.view_header_placeholder, mStaggeredGridView, false);
        mStaggeredGridView.addHeaderView(mPlaceHolderView);
        mStaggeredGridView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                int scrollY = getScrollY();
                //sticky actionbar
                mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if (totalItemCount != 0 && (lastInScreen == totalItemCount) && !(getLoaderManager().hasRunningLoaders()) && totalItemCount != mMediaCount && !TextUtils.isEmpty(mPaginationUrl) && (mFetchMediaWithStoreAndPaginationTask == null || mFetchMediaWithStoreAndPaginationTask.getStatus() == AsyncTask.Status.FINISHED))
                {
                    mFetchMediaWithStoreAndPaginationTask = new FetchMediaWithStoreAndPaginationTask(getActivity());
                    mFetchMediaWithStoreAndPaginationTask.execute(mPaginationUrl);
                }
            }
        });
        mStaggeredGridView.setAdapter(mUserMediaAdapter);
    }

    public int getScrollY()
    {
        View c = mStaggeredGridView.getChildAt(0);
        if (c == null)
        {
            return 0;
        }

        int firstVisiblePosition = mStaggeredGridView.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1)
        {
            headerHeight = mPlaceHolderView.getHeight();
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
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
        fetchUserProfileTask.setOnFetchUserInfoFinishedListener(mOnFetchMediaPaginationFinishListener);
        fetchUserProfileTask.execute(mUserId);
    }

    private OnFetchMediaPaginationFinishListener mOnFetchMediaPaginationFinishListener = new OnFetchMediaPaginationFinishListener()
    {
        @Override
        public void onFetchFinished(String paginationUrl)
        {
            mPaginationUrl = paginationUrl;
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        getLoaderManager().initLoader(USER_NEAR_LOADER, null, this);
        getLoaderManager().initLoader(MEDIA_NEAR_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        switch (id)
        {
            case USER_NEAR_LOADER:
                Uri uri = MediaContract.UserEntry.buildUserWithInstagramId(mUserId);
                return new CursorLoader(getActivity(), uri, null, null, null, null);
            case MEDIA_NEAR_LOADER:
//                String sortOrder = MediaContract.MediaEntry.COLUMN_CREATE_TIME + " DESC";
                Uri uri1 = MediaContract.MediaEntry.buildMediaWithOwnerId(mUserId);
                return new CursorLoader(getActivity(), uri1, null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        switch (loader.getId())
        {
            case USER_NEAR_LOADER:
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
                        mMediaCount = new Integer(mediaCount);
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
                break;
            case MEDIA_NEAR_LOADER:
                mUserMediaAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        switch (loader.getId())
        {
            case MEDIA_NEAR_LOADER:
                mUserMediaAdapter.swapCursor(null);
                break;
        }
    }
}
