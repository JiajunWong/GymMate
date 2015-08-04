package com.jwang.android.gymmate.fragment.media_list_fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.jwang.android.gymmate.data.MediaContract;
import com.jwang.android.gymmate.interfaces.OnRequestMediaFinishListener;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.task.media_task.BaseMediaRequestTask;
import com.jwang.android.gymmate.task.media_task.RequestMediaByLocationId;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 7/26/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class LocationMediaListFragment extends BaseMediaListFragment implements
        LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String TAG = LocationMediaListFragment.class.getSimpleName();
    private String mLocationId;
    private static final int MEDIA_NEAR_LOADER = 0;

    public void setLocationId(String id)
    {
        mLocationId = id;
    }

    @Override
    protected void loadMore()
    {
        RequestMediaByLocationId requestMediaByLocationId = new RequestMediaByLocationId(getActivity());
        if (!TextUtils.isEmpty(mLocationId))
        {
            requestMediaByLocationId.execute(mLocationId);
        }
        else
        {
            Log.e(TAG, "LocationMediaListFragment -- loadMore: mLocationId is null.");
        }
    }

    @Override
    public void refreshData()
    {
        RequestMediaByLocationId requestMediaByLocationId = new RequestMediaByLocationId(getActivity());
        requestMediaByLocationId.execute(mLocationId, "");
    }

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
