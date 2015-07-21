package com.jwang.android.gymmate.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.interfaces.OnFetchMediaArrayFinishListener;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.JsonParseUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by jiajunwang on 7/2/15.
 */
public class FetchMediaWithStoreAndPaginationTask extends
        AsyncTask<String, Void, ArrayList<ModelMedia>>
{
    private static final String TAG = FetchMediaWithStoreAndPaginationTask.class.getSimpleName();
    private Context mContext;
    private OnFetchMediaArrayFinishListener mOnFetchMediaArrayFinishListener = OnFetchMediaArrayFinishListener.NO_OP;

    public FetchMediaWithStoreAndPaginationTask(Context context)
    {
        mContext = context;
    }

    public void setOnFetchFinishedListener(OnFetchMediaArrayFinishListener onFetchMediaArrayFinishListener)
    {
        mOnFetchMediaArrayFinishListener = onFetchMediaArrayFinishListener;
    }

    @Override
    protected ArrayList<ModelMedia> doInBackground(String... params)
    {
        if (params.length < 0 || TextUtils.isEmpty(params[0]))
        {
            Log.e(TAG, "doInBackground: user id is null!");
            return null;
        }
        Log.d(TAG, "doInBackground url is " + params[0]);
        String mediaResponse = HttpRequestUtil.startHttpRequest(params[0], TAG);
        ArrayList<ModelMedia> medias = new ArrayList<>();
        HashSet<String> paginations = new HashSet<>();
        boolean b = JsonParseUtil.parseInstagramMediaJson(mContext, mediaResponse, true, medias, paginations);

        while (!b && paginations.size() > 0)
        {
            Iterator<String> iterator = paginations.iterator();
            if (iterator.hasNext())
            {
                String paginationUrl = iterator.next();
                String mediaRes = HttpRequestUtil.startHttpRequest(paginationUrl, TAG);
                paginations.clear();
                b = JsonParseUtil.parseInstagramMediaJson(mContext, mediaRes, true, medias, paginations);
            }
        }
        return medias;
    }

    @Override
    protected void onPostExecute(ArrayList<ModelMedia> medias)
    {
        super.onPostExecute(medias);
        mOnFetchMediaArrayFinishListener.onFetchFinished(medias);
    }
}
