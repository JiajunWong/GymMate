package com.jwang.android.gymmate.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.JsonParseUtil;
import com.jwang.android.gymmate.util.MediaWorker;

import java.util.ArrayList;
import java.util.HashSet;

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
        HashSet<String> paginationUrls = MediaWorker.getInstance(mContext).getPaginationUrls();
        ArrayList<String> newPaginationUrls = new ArrayList<>();
        if (paginationUrls.size() == 0)
        {
            Log.e(TAG, "doInBackground: pagination url is null!");
            return null;
        }
        for (String url : paginationUrls)
        {
            if (!TextUtils.isEmpty(url))
            {
                String mediaResponse = HttpRequestUtil.startHttpRequest(url, TAG);
                String nextPaginationUrl = JsonParseUtil.parseMediaGetPagination(mContext, mediaResponse, true);
                Log.d(TAG, "!!!!!MainGymTask: doInBackground ~ pagination url is " + nextPaginationUrl);
                if (!TextUtils.isEmpty(nextPaginationUrl))
                {
                    newPaginationUrls.add(nextPaginationUrl);
                }
            }
        }
        paginationUrls.clear();
        paginationUrls.addAll(newPaginationUrls);
        return null;
    }
}
