package com.jwang.android.gymmate;

import android.content.Context;
import android.content.Intent;

import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramSession;
import net.londatiga.android.instagram.InstagramUser;

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
    private Context mContext;

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
        mContext = GymMateApp.getInstance().getContext();
        mInstagram = new Instagram(mContext, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
    }

    public InstagramSession getSession()
    {
        return mInstagram.getSession();
    }
}
