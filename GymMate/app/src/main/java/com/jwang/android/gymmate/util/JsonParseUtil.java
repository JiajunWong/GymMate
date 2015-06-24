package com.jwang.android.gymmate.util;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Jiajun Wang on 6/24/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class JsonParseUtil
{
    public void parseMediaSearchByLocationJsonResult(String jsonString)
    {
        JSONObject mediaJsonObject = null;
        try
        {
            mediaJsonObject = new JSONObject(jsonString);
            if (mediaJsonObject.has("data"))
            {
                JSONArray mediaDataArray = mediaJsonObject.getJSONArray("data");

            }
        }
        catch (Exception e)
        {

        }
        finally
        {

        }
    }
}
