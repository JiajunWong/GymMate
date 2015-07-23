package com.jwang.android.gymmate.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.HttpRequestResultUtil;
import com.jwang.android.gymmate.util.MediaSyncWorker;

import java.util.ArrayList;
import java.util.HashSet;
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
        HashSet<String> paginationUrls = MediaSyncWorker.getInstance(mContext).getPaginationUrls();
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
                //                String nextPaginationUrl = JsonParseUtil.parseMediaGetPagination(mContext, mediaResponse, true);
                //                Log.d(TAG, "!!!!!MainGymTask: doInBackground ~ pagination url is " + nextPaginationUrl);
                //                if (!TextUtils.isEmpty(nextPaginationUrl))
                //                {
                //                    newPaginationUrls.add(nextPaginationUrl);
                //                }
                ArrayList<ModelMedia> medias = new ArrayList<>();
                HashSet<String> paginations = new HashSet<>();
                boolean b = HttpRequestResultUtil.parseInstagramMediaJson(mContext, mediaResponse, true, medias, paginations);

                while (!b && paginations.size() > 0)
                {
                    //TODO: bug?
                    Iterator<String> iterator = paginations.iterator();
                    if (iterator.hasNext())
                    {
                        String paginationUrl = iterator.next();
                        if (!TextUtils.isEmpty(paginationUrl))
                        {
                            String mediaRes = HttpRequestUtil.startHttpRequest(paginationUrl, TAG);
                            paginations.clear();
                            b = HttpRequestResultUtil.parseInstagramMediaJson(mContext, mediaRes, true, medias, paginations);
                        }
                        else
                        {
                            break;
                        }
                    }
                }
                newPaginationUrls.addAll(paginations);
            }
        }
        paginationUrls.clear();
        paginationUrls.addAll(newPaginationUrls);
        return null;
    }
}
