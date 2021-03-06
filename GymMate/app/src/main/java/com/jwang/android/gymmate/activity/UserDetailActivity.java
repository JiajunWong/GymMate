package com.jwang.android.gymmate.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.fragment.UserDetailFragment;

/**
 * Created by jiajunwang on 7/2/15.
 */
public class UserDetailActivity extends BaseActivity
{
    public static final String TAG = UserDetailActivity.class.getSimpleName();
    public static final String KEY_USER_ID = "KEY_USER_ID";
    public static final String KEY_START_LOCATION = "KEY_START_LOCATION";

    private UserDetailFragment mUserDetailFragment;

    public static void startActivity(Context context, View view, String userId)
    {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra(KEY_USER_ID, userId);
        int[] startingLocation = new int[2];
        view.getLocationOnScreen(startingLocation);
        startingLocation[0] += view.getWidth() / 2;
        intent.putExtra(KEY_START_LOCATION, startingLocation);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, view, context.getString(R.string.transition_name_user));
        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            // Call some material design APIs here
            context.startActivity(intent, options.toBundle());
        }
        else
        {
            // Implement this feature without material design
            context.startActivity(intent);
        }
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
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
