package com.jwang.android.gymmate.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.model.ModelUser;
import com.jwang.android.gymmate.util.HttpRequestUtil;
import com.jwang.android.gymmate.util.InstagramOauth;
import com.jwang.android.gymmate.util.JsonParseUtil;

import java.util.ArrayList;

/**
 * Created by jiajunwang on 7/2/15.
 */
public class FetchUserProfileTask extends
        AsyncTask<String, Void, FetchUserProfileTask.ResultWrapper>
{
    private static final String TAG = FetchUserProfileTask.class.getSimpleName();

    private Context mContext;
    private OnFetchUserDetailFinishListener mOnFetchUserDetailFinishListener = OnFetchUserDetailFinishListener.NO_OP;

    public FetchUserProfileTask(Context context)
    {
        mContext = context;
    }

    public void setOnFetchUserDetailFinishListener(OnFetchUserDetailFinishListener listener)
    {
        mOnFetchUserDetailFinishListener = listener;
    }

    @Override
    protected ResultWrapper doInBackground(String... params)
    {
        if (params.length < 0 || TextUtils.isEmpty(params[0]))
        {
            Log.e(TAG, "doInBackground: user id is null!");
            return null;
        }
        String instagramId = params[0];
        String accessToken = InstagramOauth.getsInstance(mContext).getSession().getAccessToken();
        String mediaEndPoint = "https://api.instagram.com/v1/users/" + instagramId + "/media/recent/?access_token=" + accessToken;
        String infoEndPoint = "https://api.instagram.com/v1/users/" + instagramId + "/?access_token=" + accessToken;
        //TODO: get more pics
        String infoResponse = HttpRequestUtil.startHttpRequest(infoEndPoint, TAG);
        String mediaResponse = HttpRequestUtil.startHttpRequest(mediaEndPoint, TAG);
        ArrayList<ModelMedia> medias = JsonParseUtil.parseGetMediaByLocationResultJson(mContext, mediaResponse);
        ModelUser modelUser = JsonParseUtil.parseUserInfoJson(mContext, infoResponse);
        ResultWrapper resultWrapper = new ResultWrapper(modelUser, medias);
        return resultWrapper;
    }

    @Override
    protected void onPostExecute(ResultWrapper resultWrapper)
    {
        super.onPostExecute(resultWrapper);
        mOnFetchUserDetailFinishListener.onFinished(resultWrapper);
    }

    public class ResultWrapper
    {
        public ModelUser mModelUser;
        public ArrayList<ModelMedia> modelMediaArrayList;

        public ResultWrapper(ModelUser modelUser, ArrayList<ModelMedia> arrayList)
        {
            mModelUser = modelUser;
            modelMediaArrayList = arrayList;
        }
    }

    public interface OnFetchUserDetailFinishListener
    {
        public static final OnFetchUserDetailFinishListener NO_OP = new OnFetchUserDetailFinishListener()
        {
            @Override
            public void onFinished(ResultWrapper resultWrapper)
            {
            }
        };

        public void onFinished(ResultWrapper resultWrapper);
    }
}
