package com.jwang.android.gymmate.model;

/**
 * @author Jiajun Wang on 6/25/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class ModelUser extends ModelBase
{
    private long mInstagramId;
    private String mUserName;
    private String mProfilePicture;
    private String mFullName;
    private int mMediaCount;
    private int mFollowedByCount;
    private int mFollowsCount;

    public ModelUser()
    {
    }

    public ModelUser(long id, String userName, String profilePicture, String fullName)
    {
        mInstagramId = id;
        mProfilePicture = profilePicture;
        mUserName = userName;
        mFullName = fullName;
    }

    public String getFullName()
    {
        return mFullName;
    }

    public void setFullName(String fullName)
    {
        this.mFullName = fullName;
    }

    public long getInstagramId()
    {
        return mInstagramId;
    }

    public void setInstagramId(long instagramId)
    {
        this.mInstagramId = instagramId;
    }

    public String getProfilePicture()
    {
        return mProfilePicture;
    }

    public void setProfilePicture(String profilePicture)
    {
        this.mProfilePicture = profilePicture;
    }

    public String getUserName()
    {
        return mUserName;
    }

    public void setUserName(String userName)
    {
        this.mUserName = userName;
    }

    public int getFollowedByCount()
    {
        return mFollowedByCount;
    }

    public void setFollowedByCount(int followedByCount)
    {
        this.mFollowedByCount = followedByCount;
    }

    public int getFollowsCount()
    {
        return mFollowsCount;
    }

    public void setFollowsCount(int followsCount)
    {
        this.mFollowsCount = followsCount;
    }

    public int getMediaCount()
    {
        return mMediaCount;
    }

    public void setMediaCount(int mediaCount)
    {
        this.mMediaCount = mediaCount;
    }
}
