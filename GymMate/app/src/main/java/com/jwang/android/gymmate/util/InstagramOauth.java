package com.jwang.android.gymmate.util;

import android.content.Context;

import com.jwang.android.gymmate.GymMateApp;

import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramSession;

/**
 * Created by jiajunwang on 6/23/15.
 */
public class InstagramOauth
{
    private static final String CLIENT_ID = "e23a1c4052c34b229e0695addacb5329";
    private static final String CLIENT_SECRET = "56a859865f1a48738d688cd235fea4dd";
    private static final String REDIRECT_URI = "http://gymmate.com/callback";

    private static InstagramOauth sInstance;

    private Instagram mInstagram;

    public static InstagramOauth getsInstance()
    {
        if (sInstance == null)
        {
            sInstance = new InstagramOauth();
        }
        return sInstance;
    }

    public InstagramOauth()
    {
        mInstagram = new Instagram(GymMateApp.getContext(), CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
    }

    public InstagramSession getSession()
    {
        return mInstagram.getSession();
    }

    public void authorize(Context context, Instagram.InstagramAuthListener listener)
    {
        mInstagram.authorize(context, listener);
    }
}
