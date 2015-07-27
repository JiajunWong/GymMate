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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.data.MediaContract;
import com.jwang.android.gymmate.fragment.MediaDetailFragment;
import com.jwang.android.gymmate.interfaces.OnFetchMediaObjectFinishListener;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.task.FetchMediaByShortCode;
import com.jwang.android.gymmate.util.InstagramOauth;

/**
 * @author Jiajun Wang on 7/13/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MediaDetailActivity extends BaseActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener
{
    private static final String TAG = MediaDetailActivity.class.getSimpleName();
    public static final String KEY_MEDIA_ID = "media_id_key";
    private static final int MEDIA_NEAR_LOADER = 0;

    private String mMediaId;
    private String mMediaLink;
    private MediaDetailFragment mMediaDetailFragment;

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
        extractUrlFromIntent();
        setContentView(R.layout.activity_media_detail);

        mMediaDetailFragment = (MediaDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_media_detail);

        if (TextUtils.isEmpty(mMediaId))
        {
            finish();
        }
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void extractUrlFromIntent()
    {
        final Intent intent = getIntent();
        final String action = intent.getAction();
        if (Intent.ACTION_SEND.equals(action) || Intent.ACTION_SENDTO.equals(action) || Intent.ACTION_VIEW.equals(action))
        {
            mMediaLink = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        else if (intent.getStringExtra(KEY_MEDIA_ID) != null)
        {
            mMediaId = intent.getStringExtra(KEY_MEDIA_ID);
        }

        if (!TextUtils.isEmpty(mMediaLink))
        {
            if (!InstagramOauth.getsInstance().getSession().isActive())
            {
                Intent launchLoginIntent = new Intent(this, LoginActivity.class);
                startActivity(launchLoginIntent);
            }
            Uri uri = Uri.parse(mMediaLink);
            String shortCode = uri.getLastPathSegment();
            Log.w(TAG, "extractUrlFromIntent: url is " + shortCode);
            FetchMediaByShortCode fetchMediaByShortCode = new FetchMediaByShortCode(this);
            fetchMediaByShortCode.setMediaObjectFinishListener(mOnFetchMediaObjectFinishListener);
            fetchMediaByShortCode.execute(shortCode);
        }
    }

    private OnFetchMediaObjectFinishListener mOnFetchMediaObjectFinishListener = new OnFetchMediaObjectFinishListener()
    {
        @Override
        public void onFetchFinished(ModelMedia modelMedia)
        {
            //            mMediaDetailFragment.setModelMedia(modelMedia);
        }
    };

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_instagram:
                Intent shareIntent = new Intent(Intent.ACTION_VIEW);
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                shareIntent.setData(Uri.parse(mMediaLink));
                startActivity(shareIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent createShareForecastIntent()
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mMediaLink + " " + getString(R.string.app_hash_tag));
        return shareIntent;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (!TextUtils.isEmpty(mMediaId))
        {
            getLoaderManager().initLoader(MEDIA_NEAR_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        Uri uri = MediaContract.MediaEntry.buildMediaWithInstagramId(mMediaId);
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        if (data.moveToFirst())
        {
            ModelMedia modelMedia = new ModelMedia();
            int indexImage = data.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_STANDARD);
            int indexLink = data.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_LINK);
            int indexLowResImage = data.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_LOW);
            if (!TextUtils.isEmpty(data.getString(indexImage)))
            {
                modelMedia.setImageHighRes(data.getString(indexImage));
            }
            modelMedia.setImageLowRes(data.getString(indexLowResImage));
            mMediaLink = data.getString(indexLink);
            mMediaDetailFragment.setModelMedia(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        }
    }
}
