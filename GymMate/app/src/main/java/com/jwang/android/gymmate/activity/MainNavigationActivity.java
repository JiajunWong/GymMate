package com.jwang.android.gymmate.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.ListView;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.adapter.MediaAdapter;
import com.jwang.android.gymmate.data.MediaContract;
import com.jwang.android.gymmate.task.InstagramMediaTask;
import com.jwang.android.gymmate.util.AppConfig;
import com.jwang.android.gymmate.util.InstagramOauth;

import net.londatiga.android.instagram.InstagramUser;

/**
 * @author Jiajun Wang on 6/24/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MainNavigationActivity extends BaseActivity implements
        LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String SELECTED_KEY = "selected_position";
    private static final int MEDIA_LOADER = 0;
    private int mPosition = ListView.INVALID_POSITION;

    private ListView mListView;
    private MediaAdapter mMediaAdapter;

    private InstagramOauth mInstagramOauth;
    private InstagramUser mInstagramUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);

        mListView = (ListView) findViewById(R.id.lv_medias);
        mMediaAdapter = new MediaAdapter(this, null, 0);
        mListView.setAdapter(mMediaAdapter);

        mInstagramOauth = InstagramOauth.getsInstance(this);
        mInstagramUser = mInstagramOauth.getSession().getUser();

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

        InstagramMediaTask instagramMediaTask = new InstagramMediaTask(this);
        instagramMediaTask.execute(mInstagramOauth.getSession().getAccessToken());
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        getSupportLoaderManager().initLoader(MEDIA_LOADER, null, this);
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

        // Sort order:  Ascending, by date.
        String sortOrder = MediaContract.MediaEntry.COLUMN_CREATE_TIME + " DESC";
        Uri uri = MediaContract.MediaEntry.buildMediaWithLocation(AppConfig.LOCATION, AppConfig.TEST_GYM_LAT, AppConfig.TEST_GYM_LONG);
        return new CursorLoader(this, uri, null, null, null, sortOrder);
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
