package com.jwang.android.gymmate.model;

/**
 * Created by jiajunwang on 6/25/15.
 */
public class ModelLocation extends ModelBase
{
    private String mName;
    private String mId;
    private String mLocationLong;
    private String mLocationLat;

    public String getName()
    {
        return mName;
    }

    public void setName(String name)
    {
        this.mName = name;
    }

    public String getId()
    {
        return mId;
    }

    public void setId(String id)
    {
        this.mId = id;
    }

    public String getLocationLat()
    {
        return mLocationLat;
    }

    public void setLocationLat(String locationLat)
    {
        this.mLocationLat = locationLat;
    }

    public String getLocationLong()
    {
        return mLocationLong;
    }

    public void setLocationLong(String locationLong)
    {
        this.mLocationLong = locationLong;
    }
}
