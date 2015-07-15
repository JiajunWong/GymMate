package com.jwang.android.gymmate.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.etsy.android.grid.StaggeredGridView;
import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.adapter.MediaAdapter;
import com.jwang.android.gymmate.adapter.MediaSyncAdapter;
import com.jwang.android.gymmate.data.MediaContract;
import com.jwang.android.gymmate.util.AppConfig;
import com.jwang.android.gymmate.util.LocationUtil;

/**
 * @author Jiajun Wang on 6/30/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MainMediaListFragment extends BaseFragment implements
        LoaderManager.LoaderCallbacks<Cursor>
{
    private StaggeredGridView mListView;
    private SwipeRefreshLayout swipeContainer;
    private MediaAdapter mMediaAdapter;

    private static final String SELECTED_KEY = "selected_position";
    private int mPosition = ListView.INVALID_POSITION;
    private static final int MEDIA_NEAR_LOADER = 0;

    public MainMediaListFragment()
    {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        MediaSyncAdapter.syncImmediately(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_media_list, container, false);
        mMediaAdapter = new MediaAdapter(getActivity(), null, 0);
        mListView = (StaggeredGridView) rootView.findViewById(R.id.lv_medias);
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        mListView.setAdapter(mMediaAdapter);
        mListView.setOnScrollListener(mOnScrollListener);
        swipeContainer.setOnRefreshListener(mOnRefreshListener);
        swipeContainer.setColorSchemeResources(R.color.holo_red_light, R.color.holo_blue_bright, R.color.holo_green_light, R.color.holo_orange_light);

        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY))
        {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        return rootView;
    }

    SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener()
    {
        @Override
        public void onRefresh()
        {
            refreshData();
            swipeContainer.setRefreshing(false);
        }
    };

    public void refreshData()
    {
        MediaSyncAdapter.syncImmediately(getActivity());
        getLoaderManager().restartLoader(MEDIA_NEAR_LOADER, null, this);
    }

    AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener()
    {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState)
        {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
        {
            if (view.getFirstVisiblePosition() == 0 && (view.getChildAt(0) != null && view.getChildAt(0).getTop() >= 0))
            {
                swipeContainer.setEnabled(true);
            }
            else
            {
                swipeContainer.setEnabled(false);
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        getLoaderManager().initLoader(MEDIA_NEAR_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION)
        {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.

        // To only show current and future dates, filter the query to return weather only for
        // dates after or including today.

        String sortOrder = MediaContract.MediaEntry.COLUMN_CREATE_TIME + " DESC";
        String[] locations = LocationUtil.getCurrentLocation(getActivity());
        Uri uri = MediaContract.MediaEntry.buildMediaWithLocation(AppConfig.LOCATION, locations[1], locations[0]);;
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
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        mMediaAdapter.swapCursor(null);
    }
}