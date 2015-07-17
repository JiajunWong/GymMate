package com.jwang.android.gymmate.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.interfaces.OnFetchUserInfoFinishedListener;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.InstagramOauth;
import com.jwang.android.gymmate.util.JsonParseUtil;

/**
 * Created by jiajunwang on 7/2/15.
 */
public class FetchUserMediaTask extends AsyncTask<String, Void, String>
{
    private static final String TAG = FetchUserMediaTask.class.getSimpleName();
    private Context mContext;
    private OnFetchUserInfoFinishedListener mOnFetchUserInfoFinishedListener = OnFetchUserInfoFinishedListener.NO_OP;

    public FetchUserMediaTask(Context context)
    {
        mContext = context;
    }

    public void setOnFetchUserInfoFinishedListener(OnFetchUserInfoFinishedListener listener)
    {
        mOnFetchUserInfoFinishedListener = listener;
    }

    @Override
    protected String doInBackground(String... params)
    {
        if (params.length < 0 || TextUtils.isEmpty(params[0]))
        {
            Log.e(TAG, "doInBackground: user id is null!");
            return null;
        }
        Log.d(TAG, "doInBackground url is " + params[0]);
        String mediaResponse = HttpRequestUtil.startHttpRequest(params[0], TAG);
        return JsonParseUtil.parseMediaGetPagination(mContext, mediaResponse, true);
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        mOnFetchUserInfoFinishedListener.onFetchFinished(s);
    }
}
