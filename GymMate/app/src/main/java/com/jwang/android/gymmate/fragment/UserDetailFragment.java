package com.jwang.android.gymmate.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.activity.UserDetailActivity;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.model.ModelUser;
import com.jwang.android.gymmate.task.FetchUserProfileTask;

import java.util.ArrayList;

/**
 * Created by jiajunwang on 7/2/15.
 */
public class UserDetailFragment extends BaseFragment
{
    private static final String TAG = UserDetailFragment.class.getSimpleName();

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
        fetchUserInfo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_user_details, container, false);
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
            }
        }
    };
}
