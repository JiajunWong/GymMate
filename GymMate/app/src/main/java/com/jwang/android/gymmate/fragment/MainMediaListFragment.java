package com.jwang.android.gymmate.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.etsy.android.grid.StaggeredGridView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.adapter.MediaCursorAdapter;
import com.jwang.android.gymmate.adapter.MediaSyncAdapter;
import com.jwang.android.gymmate.data.MediaContract;
import com.jwang.android.gymmate.task.FetchGymMediaTask;
import com.jwang.android.gymmate.util.AppConfig;
import com.jwang.android.gymmate.util.LocationUtil;

/**
 * @author Jiajun Wang on 6/30/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MainMediaListFragment extends BaseFragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = MainMediaListFragment.class.getSimpleName();

    private View mRootView;
    private StaggeredGridView mGridView;
    private SwipeRefreshLayout swipeContainer;
    private MediaCursorAdapter mMediaAdapter;

    private int mPosition = ListView.INVALID_POSITION;
    private FetchGymMediaTask mFetchGymMediaTask;
    protected GoogleApiClient mGoogleApiClient;

    private static final String SELECTED_KEY = "selected_position";
    private static final int MEDIA_NEAR_LOADER = 0;

    public MainMediaListFragment()
    {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        MediaSyncAdapter.syncImmediately(getActivity());
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

        buildGoogleApiClient();
        return mRootView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MEDIA_NEAR_LOADER, null, this);
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
            if (totalItemCount != 0 && (lastInScreen == totalItemCount) && !(getLoaderManager().hasRunningLoaders()) && (mFetchGymMediaTask == null || mFetchGymMediaTask.getStatus() == AsyncTask.Status.FINISHED))
            {
                mFetchGymMediaTask = new FetchGymMediaTask(getActivity());
                mFetchGymMediaTask.execute();
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
            mGridView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        mMediaAdapter.swapCursor(null);
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null)
        {
            onLocationUpdated(lastLocation);
        }
        else
        {
            Log.e(TAG, "MainMediaListFragment -- onConnected: location error.");
            if (LocationUtil.isLocationEmpty(getActivity()))
            {
                Snackbar.make(mRootView, getString(R.string.get_location_failed), Snackbar.LENGTH_LONG).setAction("Open Location Setting", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }).show();
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, new LocationRequest(), new LocationListener()
            {
                @Override
                public void onLocationChanged(Location location)
                {
                    if (location != null)
                    {
                        Log.e(TAG, "MainMediaListFragment -- requestLocationUpdates: location is " + location.getLatitude() + ", " + location.getLongitude());
                    }
                    onLocationUpdated(location);
                }
            });
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.e(TAG, "MainMediaListFragment -- onConnectionSuspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Log.e(TAG, "MainMediaListFragment -- onConnectionFailed: " + connectionResult.getErrorCode());
    }

    private void onLocationUpdated(Location location)
    {
        if (LocationUtil.updateLocation(getActivity(), location))
        {
            getLoaderManager().restartLoader(MEDIA_NEAR_LOADER, null, this);
            refreshData();
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(this).addApi(LocationServices.API).build();
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
        if (!mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.reconnect();
        }
        MediaSyncAdapter.syncImmediately(getActivity());
        getLoaderManager().restartLoader(MEDIA_NEAR_LOADER, null, this);
    }
}
