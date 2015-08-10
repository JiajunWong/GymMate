package com.jwang.android.gymmate;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.firebase.client.Firebase;
import com.jwang.android.gymmate.parse_object.ParseUserLocation;
import com.parse.Parse;
import com.parse.ParseObject;

import io.fabric.sdk.android.Fabric;

/**
 * Created by jiajunwang on 6/23/15.
 */
public class GymMateApp extends Application
{
    private static GymMateApp sInstance;
    private Context mContext;

    public GymMateApp()
    {
        super();
        sInstance = this;
    }

    public static GymMateApp getInstance()
    {
        return sInstance;
    }

    public static Context getContext()
    {
        return getInstance().mContext;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mContext = getApplicationContext();
        Firebase.setAndroidContext(this);
        ParseObject.registerSubclass(ParseUserLocation.class);
        Parse.initialize(this, "gqNHXHXBFMtBHtHVpyvQvrCDlFwqrxUBwK80tiOw", "adxv6M0otK1lx7VgvoMbdWcKYMQOphcyyGqbczGC");
//        Parse.initialize(this, "9fQQh76LqMHRgVuPAJhd67HPEBFXktrm51lBdb72", "rpTnLNlHJ5W4YYgKRSuiTRZMzfrKN90eKNehFbfF");
    }
}
