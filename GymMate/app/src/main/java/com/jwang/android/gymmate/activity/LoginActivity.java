package com.jwang.android.gymmate.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.util.AppConfig;
import com.jwang.android.gymmate.util.InstagramOauth;
import com.jwang.android.gymmate.util.LocationUtil;

import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramUser;

/**
 * Created by jiajunwang on 6/23/15.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener
{
    private static final String TAG = LoginActivity.class.getSimpleName();
    private View mLogInButton;

    private InstagramOauth mInstagramOauth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        updateGeoLocation();
        mInstagramOauth = InstagramOauth.getsInstance();
        if (!mInstagramOauth.getSession().isActive())
        {
            //log in
            setContentView(R.layout.activity_login);
            mLogInButton = findViewById(R.id.tv_log_in);
            mLogInButton.setOnClickListener(this);
        }
        else
        {
            // Already logged in
            startMainActivity();
        }
    }

    private void updateGeoLocation()
    {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LocationUtil.MINIMUM_TIME_BETWEEN_UPDATES, LocationUtil.MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                Log.w(TAG, "onLocationChanged: " + location.getLatitude() + " " + location.getLongitude());
                SharedPreferences sharedPreferences = getSharedPreferences(AppConfig.LOCATION, Context.MODE_PRIVATE);
                sharedPreferences.edit().putString(LocationUtil.KEY_LOCATION_LAT, Double.toString(location.getLatitude())).apply();
                sharedPreferences.edit().putString(LocationUtil.KEY_LOCATION_LONG, Double.toString(location.getLongitude())).apply();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {
            }

            @Override
            public void onProviderEnabled(String provider)
            {
            }

            @Override
            public void onProviderDisabled(String provider)
            {
            }
        });
    }

    private void startMainActivity()
    {
        Intent intent = new Intent(LoginActivity.this, MainNavigationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tv_log_in:
                //show instagram log in screen.
                mInstagramOauth.authorize(this, mAuthListener);
                break;
        }
    }

    private Instagram.InstagramAuthListener mAuthListener = new Instagram.InstagramAuthListener()
    {
        @Override
        public void onSuccess(InstagramUser user)
        {
            Log.v(TAG, "Author success.");
            startMainActivity();
        }

        @Override
        public void onError(String error)
        {
            Log.e(TAG, "Author failed.");
        }

        @Override
        public void onCancel()
        {
            Log.w(TAG, "Author canceled.");
        }
    };
}
