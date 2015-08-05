package com.jwang.android.gymmate.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.interfaces.OnFetchMediaObjectFinishListener;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.util.AppConfig;
import com.jwang.android.gymmate.util.HttpRequestResultUtil;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.InstagramOauth;

/**
 * Created by jiajunwang on 7/18/15.
 */
public class FetchMediaByShortCode extends AsyncTask<String, Void, ModelMedia>
{
    private static final String TAG = FetchMediaByShortCode.class.getSimpleName();
    private Context mContext;
    private OnFetchMediaObjectFinishListener mMediaObjectFinishListener = OnFetchMediaObjectFinishListener.NO_OP;

    public FetchMediaByShortCode(Context context)
    {
        mContext = context;
    }

    public void setMediaObjectFinishListener(OnFetchMediaObjectFinishListener listener)
    {
        mMediaObjectFinishListener = listener;
    }

    @Override
    protected ModelMedia doInBackground(String... params)
    {
        if (params.length < 1 || TextUtils.isEmpty(params[0]))
        {
            Log.e(TAG, "doInBackground short code is null!");
            return null;
        }
        String shortCode = params[0];
        //https://api.instagram.com/v1/media/shortcode/D?access_token=ACCESS-TOKEN
        String url = AppConfig.INSTAGRAM_MEDIA_SHORT_CODE_ENDPOINT + shortCode + "?access_token=" + InstagramOauth.getsInstance().getSession().getAccessToken();
        String response = HttpRequestUtil.startHttpRequest(url, TAG);
        return HttpRequestResultUtil.parseSingleMedia(mContext, response);
    }

    @Override
    protected void onPostExecute(ModelMedia modelMedia)
    {
        super.onPostExecute(modelMedia);
        mMediaObjectFinishListener.onFetchFinished(modelMedia);
    }
}
