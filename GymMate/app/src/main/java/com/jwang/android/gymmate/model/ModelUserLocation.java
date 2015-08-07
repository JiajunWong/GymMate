package com.jwang.android.gymmate.model;

/**
 * @author Jiajun Wang on 8/7/15
 */
public class ModelUserLocation extends ModelBase
{
    private String mInstagramId;
    private String mUserName;
    private String mProfilePicture;
    private String mFullName;

    private String mLatitude;
    private String mLongitude;

    public String getFullName()
    {
        return mFullName;
    }

    public void setFullName(String fullName)
    {
        this.mFullName = fullName;
    }

    public String getInstagramId()
    {
        return mInstagramId;
    }

    public void setInstagramId(String instagramId)
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

    public void setLatitude(String latitude)
    {
        mLatitude = latitude;
    }

    public String getLatitude()
    {
        return mLatitude;
    }

    public void setLongitude(String longitude)
    {
        mLongitude = longitude;
    }

    public String getLongitude()
    {
        return mLongitude;
    }
}
