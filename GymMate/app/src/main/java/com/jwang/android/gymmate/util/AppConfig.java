package com.jwang.android.gymmate.util;

/**
 * Created by jiajunwang on 6/25/15.
 */
public class AppConfig
{
    //San mateo 24 fitness center
    //    public static final String TEST_GYM_LAT = "37.549696";
    //    public static final String TEST_GYM_LONG = "-122.314780";

    public static final String LOCATION = "location";
    public static final String USER = "user";
    public static final String MEDIA = "media";
    public static final double RADIUS_SPHERE = 6371.01;
    public static final double RADIUS_FROM_DESTINATION = 10;

    public static final String GOOGLE_API_ENDPOINT = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
    public static final String GOOGLE_ACCESS_TOKEN = "AIzaSyCxzHIfkpQKoHWxHBkeEX-7UcBTq_ykikE";

    public static final String FACEBOOK_API_ENDPOINT = "https://graph.facebook.com/search";
    public static final String FACEBOOK_ACCESS_TOKEN = "425103717696529|1b77655dba1ccc2ed88fad1f9a932d7b";

    public static final String INSTAGRAM_LOCATION_ENDPOINT = "https://api.instagram.com/v1/locations/search";
    public static final String INSTAGRAM_POPULAR_ENDPOINT = "https://api.instagram.com/v1/tags/pilates/media/recent?access_token=";
    public static final String INSTAGRAM_LIKED_ENDPOINT = "https://api.instagram.com/v1/users/self/media/liked?access_token=";
    public static final String INSTAGRAM_USER_FEED_MEDIA_ENDPOINT = "https://api.instagram.com/v1/users/self/feed?access_token=";
    public static final String INSTAGRAM_MEDIA_SHORT_CODE_ENDPOINT = "https://api.instagram.com/v1/media/shortcode/";

//    public static final String FIREBASE_ENDPOINT = "https://blazing-torch-989.firebaseio.com/";
    //production
    public static final String FIREBASE_ENDPOINT = "https://gymmate.firebaseio.com/";
}
