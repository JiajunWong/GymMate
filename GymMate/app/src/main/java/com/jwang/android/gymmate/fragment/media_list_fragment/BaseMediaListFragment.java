package com.jwang.android.gymmate.fragment.media_list_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.etsy.android.grid.StaggeredGridView;
import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.adapter.MediaCursorAdapter;
import com.jwang.android.gymmate.fragment.BaseFragment;

/**
 * @author Jiajun Wang on 7/24/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public abstract class BaseMediaListFragment extends BaseFragment
{
    private static final String SELECTED_KEY = "selected_position";
    protected int mPosition = ListView.INVALID_POSITION;

    protected View mRootView;
    protected StaggeredGridView mGridView;
    protected SwipeRefreshLayout swipeContainer;
    protected MediaCursorAdapter mMediaAdapter;

    protected abstract void loadMore();

    public abstract void refreshData();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mRootView = inflater.inflate(R.layout.fragment_media_list, container, false);
        swipeContainer = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipeContainer);

        mMediaAdapter = new MediaCursorAdapter(getActivity(), null, 0);
        mGridView = (StaggeredGridView) mRootView.findViewById(R.id.lv_medias);
        mGridView.setAdapter(mMediaAdapter);
        mGridView.setOnScrollListener(mOnScrollListener);
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
        return mRootView;
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
            if ((view.getFirstVisiblePosition() == 0 && (view.getChildAt(0) != null && view.getChildAt(0).getTop() >= 0)) || totalItemCount == 0)
            {
                swipeContainer.setEnabled(true);
            }
            else
            {
                swipeContainer.setEnabled(false);
            }

            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (totalItemCount != 0 && (lastInScreen == totalItemCount))
            {
//                loadMore();
            }
        }
    };

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

    SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener()
    {
        @Override
        public void onRefresh()
        {
            refreshData();
            swipeContainer.setRefreshing(false);
        }
    };
}
