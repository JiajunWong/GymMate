package com.jwang.android.gymmate.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.fragment.UserDetailFragment;

/**
 * Created by jiajunwang on 7/2/15.
 */
public class UserDetailActivity extends BaseActivity
{
    public static final String TAG = UserDetailActivity.class.getSimpleName();
    public static final String KEY_USER_ID = "KEY_USER_ID";

    private UserDetailFragment mUserDetailFragment;

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
        mUserDetailFragment = UserDetailFragment.newInstance(userId);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, mUserDetailFragment);
        fragmentTransaction.commit();
    }
}
