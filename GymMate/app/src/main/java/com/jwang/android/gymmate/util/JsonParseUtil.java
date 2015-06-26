package com.jwang.android.gymmate.util;

import android.util.Log;

import com.jwang.android.gymmate.model.ModelFaceBookLocation;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.model.ModelUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 6/24/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class JsonParseUtil
{
    private static final String TAG = JsonParseUtil.class.getSimpleName();

    public static ModelFaceBookLocation parseGetFaceBookLocationByGeoResultJson(String jsonString)
    {
        ModelFaceBookLocation modelFaceBookLocation = new ModelFaceBookLocation();
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
                        modelFaceBookLocation.setName(locationJsonObject.getString("name"));
                    }
                    if (locationJsonObject.has("id"))
                    {
                        modelFaceBookLocation.setId(locationJsonObject.getString("id"));
                    }
                }
            }
        }
        catch (Exception e)
        {

        }
        return modelFaceBookLocation;
    }

    public static ArrayList<ModelMedia> parseGetMediaByLocationResultJson(String jsonString)
    {
        ArrayList<ModelMedia> medias = new ArrayList<>();

        JSONObject mediaJsonObject;
        try
        {
            mediaJsonObject = new JSONObject(jsonString);
            if (mediaJsonObject.has("meta"))
            {
                JSONObject requestCodeJsonObject = mediaJsonObject.getJSONObject("meta");
                if (requestCodeJsonObject.has("code") && requestCodeJsonObject.getInt("code") != 200)
                {
                    //request failed.
                    return medias;
                }
            }

            if (mediaJsonObject.has("data"))
            {
                JSONArray mediaDataArray = mediaJsonObject.getJSONArray("data");
                ModelMedia modelMedia;
                for (int i = 0; i < mediaDataArray.length(); i++)
                {
                    modelMedia = new ModelMedia();
                    JSONObject mediaObject = (JSONObject) mediaDataArray.get(i);
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
                        JSONObject locationJsonArray = mediaObject.getJSONObject("location");
                        if (locationJsonArray.has("latitude"))
                        {
                            modelMedia.setLocationLat(locationJsonArray.getString("latitude"));
                        }
                        if (locationJsonArray.has("longitude"))
                        {
                            modelMedia.setLocationLong(locationJsonArray.getString("longitude"));
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

                    medias.add(modelMedia);
                }
            }

            return medias;
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }
        return medias;
    }
}
