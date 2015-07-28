package com.jwang.android.gymmate.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.fragment.media_list_fragment.LocationMediaListFragment;
import com.jwang.android.gymmate.util.AndroidUtil;

/**
 * @author Jiajun Wang on 7/25/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class LocationMediaListActivity extends BaseActivity
{
    private static final String TAG = LocationMediaListActivity.class.getSimpleName();
    public static final String KEY_START_LOCATION = "arg_drawing_start_location";
    public static final String KEY_LOCATION_ID = "key_location_id";
    private int drawingStartLocation;
    private FrameLayout contentRoot;
    private LocationMediaListFragment mLocationMediaListFragment;

    public static void startActivity(Context context, int location, String locationId)
    {
        Intent intent = new Intent(context, LocationMediaListActivity.class);
        intent.putExtra(KEY_START_LOCATION, location);
        intent.putExtra(KEY_LOCATION_ID, locationId);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_list);
        setDisplayHomeAsUpEnabled(true);
        contentRoot = (FrameLayout) findViewById(R.id.content_root);

        String locationId = getIntent().getStringExtra(KEY_LOCATION_ID);
        if (TextUtils.isEmpty(locationId))
        {
            finish();
        }
        else
        {
            mLocationMediaListFragment = new LocationMediaListFragment();
            mLocationMediaListFragment.setLocationId(locationId);
            FragmentTransaction transitionManager = getSupportFragmentManager().beginTransaction();
            transitionManager.add(R.id.container, mLocationMediaListFragment, null);
            transitionManager.commit();
        }

        drawingStartLocation = getIntent().getIntExtra(KEY_START_LOCATION, 0);
        if (savedInstanceState == null)
        {
            contentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
            {
                @Override
                public boolean onPreDraw()
                {
                    contentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    startIntroAnimation();
                    return true;
                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void startIntroAnimation()
    {
        ViewCompat.setElevation(mToolbar, 0);
        contentRoot.setScaleY(0.1f);
        contentRoot.setPivotY(drawingStartLocation);

        contentRoot.animate().scaleY(1).setDuration(300).setInterpolator(new AccelerateInterpolator()).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                ViewCompat.setElevation(mToolbar, AndroidUtil.dpToPx(8));
            }
        }).start();
    }

}
