package com.jwang.android.gymmate.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by jiajunwang on 6/24/15.
 */
public class MediaProvider extends ContentProvider
{
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MediaDBHelper mOpenHelper;

    static final int MEDIA = 100;
    static final int MEDIA_WITH_LOCATION = 101;
    static final int WEATHER_WITH_LOCATION_AND_DATE = 102;
    static final int USER = 300;

    private static final SQLiteQueryBuilder sWeatherByLocationSettingQueryBuilder;

    static
    {
        sWeatherByLocationSettingQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sWeatherByLocationSettingQueryBuilder.setTables(
                MediaContractor.MediaEntry.TABLE_NAME + " INNER JOIN " +
                        MediaContractor.UserEntry.TABLE_NAME +
                        " ON " + MediaContractor.MediaEntry.TABLE_NAME +
                        "." + MediaContractor.MediaEntry.COLUMN_MEDIA_OWNER_ID +
                        " = " + MediaContractor.UserEntry.TABLE_NAME +
                        "." + MediaContractor.UserEntry.COLUMN_INSTAGRAM_ID);
    }

    @Override
    public boolean onCreate()
    {
        mOpenHelper = new MediaDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1)
    {
        return null;
    }

    @Override
    public String getType(Uri uri)
    {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues)
    {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings)
    {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings)
    {
        return 0;
    }

    private static UriMatcher buildUriMatcher()
    {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MediaContractor.CONTENT_AUTHORITY;
        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MediaContractor.PATH_MEDIA, MEDIA);
        matcher.addURI(authority, MediaContractor.PATH_MEDIA + "/*", MEDIA_WITH_LOCATION);
        matcher.addURI(authority, MediaContractor.PATH_MEDIA + "/*/#", WEATHER_WITH_LOCATION_AND_DATE);
        matcher.addURI(authority, MediaContractor.PATH_USER, USER);
        return matcher;
    }
}
