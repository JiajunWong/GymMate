package com.jwang.android.gymmate;

import android.app.Application;
import android.content.Context;

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
    }

    public static GymMateApp getInstance()
    {
        if (sInstance == null)
        {
            sInstance = new GymMateApp();
        }
        return sInstance;
    }

    public Context getContext()
    {
        return sInstance.mContext;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
