package com.jwang.android.gymmate.activity;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.data.MediaContract;
import com.squareup.picasso.Picasso;

/**
 * @author Jiajun Wang on 7/13/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MediaDetailActivity extends BaseActivity implements
        LoaderManager.LoaderCallbacks<Cursor>
{
    public static final String KEY_MEDIA_ID = "media_id_key";
    private static final int MEDIA_NEAR_LOADER = 0;

    private String mMediaId;
    private String mMediaLink;
    private ImageView mMediaImageView;

    public static void startActivity(Context context, View view, String id)
    {
        Intent intent = new Intent(context, MediaDetailActivity.class);
        intent.putExtra(KEY_MEDIA_ID, id);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, view, context.getString(R.string.transition_name_media));
        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            // Call some material design APIs here
            context.startActivity(intent, options.toBundle());
        }
        else
        {
            // Implement this feature without material design
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_detail);
        mMediaImageView = (ImageView) findViewById(R.id.media_image);
        mMediaId = getIntent().getStringExtra(KEY_MEDIA_ID);
        if (TextUtils.isEmpty(mMediaId))
        {
            finish();
        }
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_media_detail, menu);
        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (!TextUtils.isEmpty(mMediaLink))
        {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
        return true;
    }

    private Intent createShareForecastIntent()
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mMediaLink);
        return shareIntent;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        getLoaderManager().initLoader(MEDIA_NEAR_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        String sortOrder = MediaContract.MediaEntry.COLUMN_CREATE_TIME + " DESC";
        Uri uri = MediaContract.MediaEntry.buildMediaWithInstagramId(mMediaId);
        return new CursorLoader(this, uri, null, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        if (data.moveToFirst())
        {
            int indexImage = data.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_STANDARD);
            if (!TextUtils.isEmpty(data.getString(indexImage)))
            {
                Picasso.with(this).load(data.getString(indexImage)).into(mMediaImageView);
            }
            else
            {
                indexImage = data.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_LOW);
                Picasso.with(this).load(data.getString(indexImage)).into(mMediaImageView);
            }

            int indexLink = data.getColumnIndex(MediaContract.MediaEntry.COLUMN_LINK);
            mMediaLink = data.getString(indexLink);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {

    }
}
