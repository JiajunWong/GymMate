package com.jwang.android.gymmate.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.jwang.android.gymmate.data.MediaContract;
import com.jwang.android.gymmate.model.ModelLocation;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.model.ModelUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 6/24/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class JsonParseUtil
{
    private static final String TAG = JsonParseUtil.class.getSimpleName();

    public static ArrayList<ModelLocation> parseGetGeoLocationByGoogleApiJson(String jsonString)
    {
        ArrayList<ModelLocation> locations = new ArrayList<>();
        JSONObject respondJsonObject;
        try
        {
            respondJsonObject = new JSONObject(jsonString);
            if (respondJsonObject.has("results"))
            {
                if (respondJsonObject.get("results") == null)
                {
                    return locations;
                }
                ModelLocation modelLocation;
                JSONArray locationJsonArray = respondJsonObject.getJSONArray("results");
                for (int i = 0; i < locationJsonArray.length(); i++)
                {
                    modelLocation = new ModelLocation();
                    JSONObject locationJsonObject = locationJsonArray.getJSONObject(i);
                    if (locationJsonObject.has("name"))
                    {
                        modelLocation.setName(locationJsonObject.getString("name"));
                    }
                    if (locationJsonObject.has("geometry"))
                    {
                        JSONObject geometryJsonObject = locationJsonObject.getJSONObject("geometry");
                        if (geometryJsonObject.has("location"))
                        {
                            modelLocation.setLocationLat(geometryJsonObject.getJSONObject("location").getString("lat"));
                            modelLocation.setLocationLong(geometryJsonObject.getJSONObject("location").getString("lng"));
                        }
                    }
                    locations.add(modelLocation);
                    if (i == 3)
                    {
                        break;
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }
        return locations;
    }

    public static ModelLocation parseGetFaceBookLocationByGeoResultJson(String jsonString)
    {
        ModelLocation modelLocation = new ModelLocation();
        JSONObject respondJsonObject;
        try
        {
            respondJsonObject = new JSONObject(jsonString);
            if (respondJsonObject.has("data"))
            {
                JSONArray locationJsonArray = respondJsonObject.getJSONArray("data");
                // for now, we only need the first result.
                // for (int i = 0; i < locationJsonArray.length(); i++)
                if (locationJsonArray.length() > 0)
                {
                    JSONObject locationJsonObject = locationJsonArray.getJSONObject(0);
                    if (locationJsonObject.has("name"))
                    {
                        modelLocation.setName(locationJsonObject.getString("name"));
                    }
                    if (locationJsonObject.has("id"))
                    {
                        modelLocation.setId(locationJsonObject.getString("id"));
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }
        return modelLocation;
    }

    public static ModelUser parseUserInfoJson(Context context, String jsonString)
    {
        ModelUser modelUser = new ModelUser();
        JSONObject respondJsonObject;
        try
        {
            respondJsonObject = new JSONObject(jsonString);
            if (respondJsonObject.has("data"))
            {
                JSONObject dataJsonObject = respondJsonObject.getJSONObject("data");

                if (dataJsonObject.has("username"))
                {
                    modelUser.setUserName(dataJsonObject.getString("username"));
                }
                if (dataJsonObject.has("full_name"))
                {
                    modelUser.setFullName(dataJsonObject.getString("full_name"));
                }
                if (dataJsonObject.has("profile_picture"))
                {
                    modelUser.setProfilePicture(dataJsonObject.getString("profile_picture"));
                }
                if (dataJsonObject.has("id"))
                {
                    modelUser.setInstagramId(dataJsonObject.getLong("id"));
                }
                if (dataJsonObject.has("counts"))
                {
                    JSONObject countsJsonObject = dataJsonObject.getJSONObject("counts");
                    if (countsJsonObject.has("media"))
                    {
                        modelUser.setMediaCount(countsJsonObject.getInt("media"));
                    }
                    if (countsJsonObject.has("followed_by"))
                    {
                        modelUser.setFollowedByCount(countsJsonObject.getInt("followed_by"));
                    }
                    if (countsJsonObject.has("follows"))
                    {
                        modelUser.setFollowsCount(countsJsonObject.getInt("follows"));
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "parseUserInfoJson: " + e.getMessage());
        }
        updateUserValues(context, modelUser);
        return modelUser;
    }

    public static ModelLocation parseGetInstagramLocationByFaceBookIdJson(Context context, String jsonString)
    {
        ModelLocation modelLocation = new ModelLocation();
        JSONObject respondJsonObject;
        try
        {
            respondJsonObject = new JSONObject(jsonString);
            if (respondJsonObject.has("data"))
            {
                JSONArray locationJsonArray = respondJsonObject.getJSONArray("data");
                // for now, we only need the first result.
                // for (int i = 0; i < locationJsonArray.length(); i++)
                if (locationJsonArray.length() > 0)
                {
                    JSONObject locationJsonObject = locationJsonArray.getJSONObject(0);
                    if (locationJsonObject.has("name"))
                    {
                        modelLocation.setName(locationJsonObject.getString("name"));
                    }
                    if (locationJsonObject.has("id"))
                    {
                        modelLocation.setId(locationJsonObject.getString("id"));
                    }
                    if (locationJsonObject.has("latitude"))
                    {
                        modelLocation.setLocationLat(locationJsonObject.getString("latitude"));
                    }
                    if (locationJsonObject.has("longitude"))
                    {
                        modelLocation.setLocationLong(locationJsonObject.getString("longitude"));
                    }
                    addLocationValues(context, modelLocation);
                }
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "parseGetInstagramLocationByFaceBookIdJson: " + e.getMessage());
        }

        return modelLocation;
    }

    public static class ResultWrapper
    {
        public ArrayList<ModelMedia> mMedias;
        public String mPaginationUrl;

        public ResultWrapper(ArrayList<ModelMedia> arrayList, String url)
        {
            mMedias = arrayList;
            mPaginationUrl = url;
        }
    }

    private static String parseMediaJsonGetPagination(String jsonString)
    {
        String nextUrl = "";
        JSONObject mediaJsonObject;
        try
        {
            mediaJsonObject = new JSONObject(jsonString);
            if (mediaJsonObject.has("pagination"))
            {
                JSONObject paginationJsonObject = mediaJsonObject.getJSONObject("pagination");
                if (paginationJsonObject.has("next_url"))
                {
                    nextUrl = paginationJsonObject.getString("next_url");
                }
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "parseMediaJsonGetPagination: " + e.getMessage());
        }
        return nextUrl;
    }

    // Just parse json without store.
    private static ArrayList<ModelMedia> parseMediaJsonWithoutStoreMedia(String jsonString, ArrayList<ModelMedia> medias)
    {
        JSONObject mediaJsonObject;
        try
        {
            mediaJsonObject = new JSONObject(jsonString);

            if (mediaJsonObject.has("data"))
            {
                JSONArray mediaDataArray = mediaJsonObject.getJSONArray("data");
                ModelMedia modelMedia;
                for (int i = 0; i < mediaDataArray.length(); i++)
                {
                    JSONObject mediaObject = (JSONObject) mediaDataArray.get(i);
                    modelMedia = parseMediaJsonObject(mediaObject);
                    medias.add(modelMedia);
                }
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "parseMediaJson ERROR!!: " + e.toString());
        }
        return medias;
    }

    public static ModelMedia parseSingleMedia(String jsonString)
    {
        JSONObject mediaJsonObject;
        ModelMedia modelMedia = new ModelMedia();
        try
        {
            mediaJsonObject = new JSONObject(jsonString);
            if (mediaJsonObject.has("data"))
            {
                JSONObject dataJsonObject = mediaJsonObject.getJSONObject("data");
                modelMedia = parseMediaJsonObject(dataJsonObject);
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "parseMediaJson ERROR!!: " + e.toString());
        }
        return modelMedia;
    }

    private static ModelMedia parseMediaJsonObject(JSONObject mediaObject)
    {
        ModelMedia modelMedia = new ModelMedia();
        try
        {
            if (mediaObject.has("tags")) //tags
            {
                JSONArray tagsJsonArray = mediaObject.getJSONArray("tags");
                for (int j = 0; j < tagsJsonArray.length(); j++)
                {
                    modelMedia.getTags().add((String) tagsJsonArray.get(j));
                }
            }

            if (mediaObject.has("type")) //type
            {
                modelMedia.setType(mediaObject.getString("type"));
            }

            if (mediaObject.has("location") && !mediaObject.getString("location").equals("null")) //location
            {
                JSONObject locationJsonObject = mediaObject.getJSONObject("location");
                if (locationJsonObject.has("latitude"))
                {
                    modelMedia.setLocationLat(locationJsonObject.getString("latitude"));
                }
                if (locationJsonObject.has("longitude"))
                {
                    modelMedia.setLocationLong(locationJsonObject.getString("longitude"));
                }
                if (locationJsonObject.has("id"))
                {
                    modelMedia.setLocationId(locationJsonObject.getString("id"));
                }
                if (locationJsonObject.has("name"))
                {
                    modelMedia.setLocationName(locationJsonObject.getString("name"));
                }
            }

            if (mediaObject.has("created_time"))
            {
                modelMedia.setCreateTime(mediaObject.getLong("created_time"));
            }

            if (mediaObject.has("link"))
            {
                modelMedia.setLink(mediaObject.getString("link"));
            }

            if (mediaObject.has("images"))
            {
                JSONObject imageJsonObject = mediaObject.getJSONObject("images");

                if (imageJsonObject.has("standard_resolution") && imageJsonObject.getJSONObject("standard_resolution").has("url"))
                {
                    modelMedia.setImageHighRes(imageJsonObject.getJSONObject("standard_resolution").getString("url"));
                }
                if (imageJsonObject.has("low_resolution") && imageJsonObject.getJSONObject("low_resolution").has("url"))
                {
                    modelMedia.setImageLowRes(imageJsonObject.getJSONObject("low_resolution").getString("url"));
                }
                if (imageJsonObject.has("thumbnail") && imageJsonObject.getJSONObject("thumbnail").has("url"))
                {
                    modelMedia.setImageThumbnail(imageJsonObject.getJSONObject("thumbnail").getString("url"));
                }
            }

            if (mediaObject.has("videos"))
            {
                JSONObject videoJsonObject = mediaObject.getJSONObject("videos");
                if (videoJsonObject.has("low_bandwidth") && videoJsonObject.getJSONObject("low_bandwidth").has("url"))
                {
                    modelMedia.setVideoLowBandwidth(videoJsonObject.getJSONObject("low_bandwidth").getString("url"));
                }
                if (videoJsonObject.has("standard_resolution") && videoJsonObject.getJSONObject("standard_resolution").has("url"))
                {
                    modelMedia.setVideoStandardRes(videoJsonObject.getJSONObject("standard_resolution").getString("url"));
                }
                if (videoJsonObject.has("low_resolution") && videoJsonObject.getJSONObject("low_resolution").has("url"))
                {
                    modelMedia.setVideoLowRes(videoJsonObject.getJSONObject("low_resolution").getString("url"));
                }
            }

            if (mediaObject.has("caption") && !mediaObject.getString("caption").equals("null"))
            {
                JSONObject captionJsonObject = mediaObject.getJSONObject("caption");
                if (captionJsonObject != null && captionJsonObject.has("text"))
                {
                    modelMedia.setCaptionText(captionJsonObject.getString("text"));
                }
            }

            if (mediaObject.has("id"))
            {
                modelMedia.setInstagramId(mediaObject.getString("id"));
            }

            if (mediaObject.has("user"))
            {
                ModelUser owner = new ModelUser();
                JSONObject userJsonObject = mediaObject.getJSONObject("user");
                if (userJsonObject.has("username"))
                {
                    owner.setUserName(userJsonObject.getString("username"));
                }
                if (userJsonObject.has("profile_picture"))
                {
                    owner.setProfilePicture(userJsonObject.getString("profile_picture"));
                }
                if (userJsonObject.has("id"))
                {
                    owner.setInstagramId(userJsonObject.getLong("id"));
                }
                if (userJsonObject.has("full_name"))
                {
                    owner.setFullName(userJsonObject.getString("full_name"));
                }

                modelMedia.setOwner(owner);
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "parseMediaJson ERROR!!: " + e.toString());
        }
        return modelMedia;
    }

    //parse media json and store
    public boolean parseInstagramMediaJson(Context context, String jsonString, boolean shouldStore, ArrayList<ModelMedia> medias, ArrayList<String> paginations)
    {
        parseMediaJsonWithoutStoreMedia(jsonString, medias);
        paginations.add(parseMediaJsonGetPagination(jsonString));

        boolean isAddNew = false;
        if (shouldStore)
        {
            for (ModelMedia modelMedia : medias)
            {
                boolean b = addMediaValues(context, modelMedia);
                addUserValues(context, modelMedia);
                isAddNew = isAddNew || b;
            }
        }
        return isAddNew;
    }

    private static void addUserValues(Context context, ModelMedia modelMedia)
    {

        Cursor userCursor = context.getContentResolver().query(MediaContract.UserEntry.CONTENT_URI, new String[] { MediaContract.UserEntry.COLUMN_INSTAGRAM_ID }, MediaContract.UserEntry.COLUMN_INSTAGRAM_ID + " = ?", new String[] { Long.toString(modelMedia.getOwner().getInstagramId()) }, null);
        if (!userCursor.moveToFirst())
        {
            ContentValues userContentValues = new ContentValues();
            userContentValues.put(MediaContract.UserEntry.COLUMN_INSTAGRAM_ID, modelMedia.getOwner().getInstagramId());
            userContentValues.put(MediaContract.UserEntry.COLUMN_USERNAME, modelMedia.getOwner().getUserName());
            userContentValues.put(MediaContract.UserEntry.COLUMN_FULL_NAME, modelMedia.getOwner().getFullName());
            userContentValues.put(MediaContract.UserEntry.COLUMN_PROFILE_PICTURE, modelMedia.getOwner().getProfilePicture());

            context.getContentResolver().insert(MediaContract.UserEntry.CONTENT_URI, userContentValues);
        }
        userCursor.close();
    }

    private static void addLocationValues(Context context, ModelLocation modelLocation)
    {
        Cursor locationCursor = context.getContentResolver().query(MediaContract.LocationEntry.CONTENT_URI, new String[] { MediaContract.LocationEntry.COLUMN_INSTAGRAM_LOCATION_ID }, MediaContract.LocationEntry.COLUMN_INSTAGRAM_LOCATION_ID + " = ?", new String[] { modelLocation.getId() }, null);
        if (!locationCursor.moveToFirst())
        {
            ContentValues locationContentValues = new ContentValues();
            locationContentValues.put(MediaContract.LocationEntry.COLUMN_INSTAGRAM_LOCATION_ID, modelLocation.getId());
            locationContentValues.put(MediaContract.LocationEntry.COLUMN_LOCATION_LATITUDE, modelLocation.getLocationLat());
            locationContentValues.put(MediaContract.LocationEntry.COLUMN_LOCATION_LONGITUDE, modelLocation.getLocationLong());
            locationContentValues.put(MediaContract.LocationEntry.COLUMN_LOCATION_NAME, modelLocation.getName());

            context.getContentResolver().insert(MediaContract.LocationEntry.CONTENT_URI, locationContentValues);
        }
        locationCursor.close();
    }

    private static void updateUserValues(Context context, ModelUser modelUser)
    {
        ContentValues userInfoContentValues = new ContentValues();
        userInfoContentValues.put(MediaContract.UserEntry.COLUMN_USERNAME, modelUser.getUserName());
        userInfoContentValues.put(MediaContract.UserEntry.COLUMN_FULL_NAME, modelUser.getFullName());
        userInfoContentValues.put(MediaContract.UserEntry.COLUMN_PROFILE_PICTURE, modelUser.getProfilePicture());
        userInfoContentValues.put(MediaContract.UserEntry.COLUMN_MEDIA_COUNT, modelUser.getMediaCount());
        userInfoContentValues.put(MediaContract.UserEntry.COLUMN_FOLLOWED_BY_COUNT, modelUser.getFollowedByCount());
        userInfoContentValues.put(MediaContract.UserEntry.COLUMN_FOLLOW_COUNT, modelUser.getFollowsCount());

        context.getContentResolver().update(MediaContract.UserEntry.CONTENT_URI, userInfoContentValues, MediaContract.UserEntry.COLUMN_INSTAGRAM_ID + " = ?", new String[] { Long.toString(modelUser.getInstagramId()) });
    }

    private static boolean addMediaValues(Context context, ModelMedia modelMedia)
    {
        boolean isAddNew = false;
        Cursor mediaCursor = context.getContentResolver().query(MediaContract.MediaEntry.CONTENT_URI, new String[] { MediaContract.MediaEntry.COLUMN_MEDIA_INSTAGRAM_ID }, MediaContract.MediaEntry.COLUMN_MEDIA_INSTAGRAM_ID + " = ?", new String[] { modelMedia.getInstagramId() }, null);
        if (!mediaCursor.moveToFirst())
        {
            ContentValues mediaContentValues = new ContentValues();
            mediaContentValues.put(MediaContract.MediaEntry.COLUMN_TAGS, modelMedia.getTagString());
            mediaContentValues.put(MediaContract.MediaEntry.COLUMN_TYPE, modelMedia.getType());
            mediaContentValues.put(MediaContract.MediaEntry.COLUMN_LOCATION_LATITUDE, modelMedia.getLocationLat());
            mediaContentValues.put(MediaContract.MediaEntry.COLUMN_LOCATION_LONGITUDE, modelMedia.getLocationLong());
            mediaContentValues.put(MediaContract.MediaEntry.COLUMN_LOCATION_INSTAGRAM_ID, modelMedia.getLocationId());
            mediaContentValues.put(MediaContract.MediaEntry.COLUMN_LOCATION_NAME, modelMedia.getLocationName());
            mediaContentValues.put(MediaContract.MediaEntry.COLUMN_CREATE_TIME, modelMedia.getCreateTime());
            mediaContentValues.put(MediaContract.MediaEntry.COLUMN_LINK, modelMedia.getLink());
            mediaContentValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_LOW, modelMedia.getImageLowRes());
            mediaContentValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_THUMBNAIL, modelMedia.getImageThumbnail());
            mediaContentValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_STANDARD, modelMedia.getImageHighRes());
            mediaContentValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_OWNER_ID, modelMedia.getOwner().getInstagramId());
            mediaContentValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_INSTAGRAM_ID, modelMedia.getInstagramId());
            mediaContentValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_VIDEO_LOW_BANDWIDTH, modelMedia.getVideoLowBandwidth());
            mediaContentValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_VIDEO_STANDARD_RES, modelMedia.getVideoStandardRes());
            mediaContentValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_VIDEO_LOW_RES, modelMedia.getVideoLowRes());
            mediaContentValues.put(MediaContract.MediaEntry.COLUMN_CAPTION_TEXT, modelMedia.getCaptionText());
            mediaContentValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_ENABLED, "0");

            context.getContentResolver().insert(MediaContract.MediaEntry.CONTENT_URI, mediaContentValues);
            isAddNew = true;
        }
        mediaCursor.close();
        return isAddNew;
    }
}
