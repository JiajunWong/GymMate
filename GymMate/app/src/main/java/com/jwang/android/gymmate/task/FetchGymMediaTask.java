package com.jwang.android.gymmate.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.util.HttpRequestResultUtil;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.InstagramOauth;
import com.jwang.android.gymmate.util.MediaSyncWorker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by jiajunwang on 7/2/15.
 */
public class FetchGymMediaTask extends AsyncTask<String, Void, Void>
{
    private static final String TAG = FetchGymMediaTask.class.getSimpleName();
    private Context mContext;

    public FetchGymMediaTask(Context context)
    {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params)
    {
        HashMap<String, String> timeStampHashMap = MediaSyncWorker.getInstance(mContext).getTimeStamps();
        HashMap<String, String> newTimeStampHashMap = new HashMap<>();
        if (timeStampHashMap.isEmpty())
        {
            Log.e(TAG, "FetchGymMediaTask -- doInBackground: timeStamps is null!");
            return null;
        }
        Iterator<String> iterator = timeStampHashMap.keySet().iterator();
        while (iterator.hasNext())
        {
            String instagramLocationId = iterator.next();
            if (!TextUtils.isEmpty(instagramLocationId))
            {
                String timeStamps = timeStampHashMap.get(instagramLocationId);
                String access_token = InstagramOauth.getsInstance().getSession().getAccessToken();
                String url = "https://api.instagram.com/v1/locations/" + instagramLocationId + "/media/recent?access_token=" + access_token + "&max_timestamp=" + timeStamps;
                String mediaResponse = HttpRequestUtil.startHttpRequest(url, TAG);

                ArrayList<String> minTimeStamps = new ArrayList<>();
                HttpRequestResultUtil.addMediaToDB(mContext, mediaResponse, HttpRequestResultUtil.RequestMediaType.LOCATION, instagramLocationId, true, null, minTimeStamps);
                if (!minTimeStamps.isEmpty())
                {
                    newTimeStampHashMap.put(instagramLocationId, minTimeStamps.get(0));
                }
            }
        }
        timeStampHashMap.clear();
        timeStampHashMap.putAll(newTimeStampHashMap);
        return null;
    }
}
