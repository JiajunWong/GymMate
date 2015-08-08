package com.jwang.android.gymmate.task.media_task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.jwang.android.gymmate.data.MediaContract;
import com.jwang.android.gymmate.interfaces.OnRequestMediaFinishListener;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.util.InstagramOauth;

import java.util.ArrayList;

/**
 * Created by jiajunwang on 8/1/15.
 */
public abstract class BaseMediaRequestTask extends
        AsyncTask<String, Void, ArrayList<ModelMedia>>
{
    protected Context mContext;
    protected String mDataType;
    protected String mAccessToken;
    protected OnRequestMediaFinishListener mOnRequestMediaFinishListener = OnRequestMediaFinishListener.NO_OP;

    protected abstract ArrayList<ModelMedia> requestMedias(String... params);

    public BaseMediaRequestTask(Context context, String dataType)
    {
        mContext = context;
        mDataType = dataType;
        mAccessToken = InstagramOauth.getsInstance().getSession().getAccessToken();
    }

    public void setOnRequestMediaFinishListener(OnRequestMediaFinishListener listener)
    {
        mOnRequestMediaFinishListener = listener;
    }

    @Override
    protected ArrayList<ModelMedia> doInBackground(String... params)
    {
        return requestMedias(params);
    }

    @Override
    protected void onPostExecute(ArrayList<ModelMedia> medias)
    {
        super.onPostExecute(medias);
        mOnRequestMediaFinishListener.onFetchFinished(medias);
    }

    protected String getPaginationUrl(String instagramId)
    {
        Cursor paginationCursor = mContext.getContentResolver().query(MediaContract.PaginationEntry.CONTENT_URI, new String[] { MediaContract.PaginationEntry.COLUMN_DATA_PAGINATION }, MediaContract.PaginationEntry.COLUMN_DATA_TYPE + " = ? AND " + MediaContract.PaginationEntry.COLUMN_DATA_ID + " = ?", new String[] { mDataType, instagramId }, null);
        String paginationUrl = null;
        if (paginationCursor.moveToFirst())
        {
            int index_url = paginationCursor.getColumnIndex(MediaContract.PaginationEntry.COLUMN_DATA_PAGINATION);
            paginationUrl = paginationCursor.getString(index_url);
        }
        paginationCursor.close();
        return paginationUrl;
    }
}
