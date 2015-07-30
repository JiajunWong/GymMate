package com.jwang.android.gymmate.fragment.media_list_fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.ListView;

import com.jwang.android.gymmate.data.MediaContract;
import com.jwang.android.gymmate.interfaces.OnRequestMediaFinishWithTimeStampListener;
import com.jwang.android.gymmate.task.RequestMediaByLocationId;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 7/26/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class LocationMediaListFragment extends BaseMediaListFragment implements
        LoaderManager.LoaderCallbacks<Cursor>
{
    private String mLocationId;
    private static final int MEDIA_NEAR_LOADER = 0;
    private RequestMediaByLocationId mRequestMediaByLocationId;
    private ArrayList<String> mTimeStamps = new ArrayList<>();

    public void setLocationId(String id)
    {
        mLocationId = id;
    }

    @Override
    protected void loadMore()
    {
        mRequestMediaByLocationId = new RequestMediaByLocationId(getActivity());
        mRequestMediaByLocationId.setOnFetchMediaPaginationFinishListener(mOnRequestMediaFinishWithTimeStampListener);
        if (!mTimeStamps.isEmpty())
        {
            mRequestMediaByLocationId.execute(mTimeStamps.get(0));
        }
    }

    @Override
    public void refreshData()
    {

    }

    private OnRequestMediaFinishWithTimeStampListener mOnRequestMediaFinishWithTimeStampListener = new OnRequestMediaFinishWithTimeStampListener()
    {
        @Override
        public void onFetchFinished(String paginationUrl)
        {
            mTimeStamps.add(paginationUrl);
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MEDIA_NEAR_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        String sortOrder = MediaContract.MediaEntry.COLUMN_CREATE_TIME + " DESC";
        Uri uri = MediaContract.LocationEntry.buildLocationUriWithInstagramId(mLocationId);
        return new CursorLoader(getActivity(), uri, null, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        mMediaAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION)
        {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mGridView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        mMediaAdapter.swapCursor(null);
    }
}
