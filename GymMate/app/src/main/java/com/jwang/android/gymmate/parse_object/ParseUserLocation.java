package com.jwang.android.gymmate.parse_object;

import com.jwang.android.gymmate.model.ModelUserLocation;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

/**
 * @author Jiajun Wang on 8/7/15
 */
@ParseClassName("UserLocation")
public class ParseUserLocation extends ParseObject
{
    private static final String INSTAGRAM_ID = "instagram_id";
    private static final String USER_NAME = "user_name";
    private static final String PROFILE_PICTURE = "profile_picture";
    private static final String FULL_NAME = "full_name";
    private static final String PARSE_GEO_POINT = "parse_geo_point";

    public ParseUserLocation()
    {
    }

    public String getFullName()
    {
        return getString(FULL_NAME);
    }

    public void setFullName(String fullName)
    {
        put(FULL_NAME, fullName);
    }

    public String getInstagramId()
    {
        return getString(INSTAGRAM_ID);
    }

    public void setInstagramId(String instagramId)
    {
        put(INSTAGRAM_ID, instagramId);
    }

    public String getProfilePicture()
    {
        return getString(PROFILE_PICTURE);
    }

    public void setProfilePicture(String profilePicture)
    {
        put(PROFILE_PICTURE, profilePicture);
    }

    public String getUserName()
    {
        return getString(USER_NAME);
    }

    public void setUserName(String userName)
    {
        put(USER_NAME, userName);
    }

    public void setParseGeoPoint(ParseGeoPoint parseGeoPoint)
    {
        put(PARSE_GEO_POINT, parseGeoPoint);
    }
}
