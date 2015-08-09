package com.jwang.android.gymmate.fragment.media_list_fragment;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.adapter.MediaSyncAdapter;
import com.jwang.android.gymmate.data.MediaContract;
import com.jwang.android.gymmate.task.media_task.RequestMainLocationMediaTask;
import com.jwang.android.gymmate.util.AppConfig;
import com.jwang.android.gymmate.util.LocationUtil;

/**
 * @author Jiajun Wang on 6/30/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MainMediaListFragment extends BaseMediaListFragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = MainMediaListFragment.class.getSimpleName();

    protected GoogleApiClient mGoogleApiClient;
    private RequestMainLocationMediaTask mRequestMainLocationMediaTask;

    private static final int MEDIA_NEAR_LOADER = 0;

    public MainMediaListFragment()
    {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        showLoadingDialog();
        MediaSyncAdapter.syncImmediately(getActivity());
        buildGoogleApiClient();
        return super.onCreateView(inflater, container, savedInstanceState);
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
    protected void loadMore()
    {
        if (!(getLoaderManager().hasRunningLoaders()) && (mRequestMainLocationMediaTask == null || mRequestMainLocationMediaTask.getStatus() == AsyncTask.Status.FINISHED))
        {
            mRequestMainLocationMediaTask = new RequestMainLocationMediaTask(getActivity());
            mRequestMainLocationMediaTask.execute();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MEDIA_NEAR_LOADER, null, this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (mPosition != ListView.INVALID_POSITION)
        {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mGridView.smoothScrollToPosition(mPosition);
        }
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
        Uri uri = MediaContract.MediaEntry.buildMediaWithLocation(AppConfig.LOCATION, locations[1], locations[0]);
        return new CursorLoader(getActivity(), uri, null, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        mMediaAdapter.swapCursor(data);
        if (data.getCount() > 0)
        {
            dismissLoadingDialog();
        }
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
            dismissLoadingDialog();
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
                        onLocationUpdated(location);
                    }
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

    @Override
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
