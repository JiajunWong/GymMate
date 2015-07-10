package com.jwang.android.gymmate.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.fragment.UserDetailFragment;
import com.jwang.android.gymmate.task.FetchUserProfileTask;

/**
 * Created by jiajunwang on 7/2/15.
 */
public class UserDetailActivity extends BaseActivity
{
    public static final String TAG = UserDetailActivity.class.getSimpleName();
    public static final String KEY_USER_ID = "KEY_USER_ID";

    public static void startActivity(Context context, String userId)
    {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra(KEY_USER_ID, userId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        String userId = getIntent().getStringExtra(KEY_USER_ID);
        if (TextUtils.isEmpty(userId))
        {
            Log.e(TAG, "UserDetailActivity has a null user! Close immediately");
            finish();
        }
        setContentView(R.layout.activity_user_details);

        FetchUserProfileTask fetchUserProfileTask = new FetchUserProfileTask(this);
        fetchUserProfileTask.setOnFetchUserDetailFinishListener(mOnFetchUserDetailFinishListener);
        fetchUserProfileTask.execute(userId);
    }

    private FetchUserProfileTask.OnFetchUserDetailFinishListener mOnFetchUserDetailFinishListener = new FetchUserProfileTask.OnFetchUserDetailFinishListener()
    {
        @Override
        public void onFinished(FetchUserProfileTask.ResultWrapper resultWrapper)
        {
            UserDetailFragment userDetailFragment = (UserDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_user_details);
            userDetailFragment.setResultWrapper(resultWrapper);
        }
    };
}
