package com.jwang.android.gymmate.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.util.AppConfig;
import com.jwang.android.gymmate.util.GeoLocationUtil;

/**
 * Created by jiajunwang on 6/24/15.
 */
public class MediaProvider extends ContentProvider
{
    private static final String TAG = MediaProvider.class.getSimpleName();
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MediaDBHelper mOpenHelper;

    static final int MEDIA = 100;
    static final int MEDIA_WITH_LOCATION = 101;
    static final int MEDIA_WITH_OWNER_ID = 102;
    static final int MEDIA_WITH_INSTAGRAM_ID = 103;
    static final int USER = 300;
    static final int USER_WITH_INSTAGRAM_ID = 301;
    static final int LOCATION = 500;

    private static final SQLiteQueryBuilder sWeatherByIdQueryBuilder;

    static
    {
        sWeatherByIdQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sWeatherByIdQueryBuilder.setTables(MediaContract.MediaEntry.TABLE_NAME + " INNER JOIN " + MediaContract.UserEntry.TABLE_NAME + " ON " + MediaContract.MediaEntry.TABLE_NAME + "." + MediaContract.MediaEntry.COLUMN_MEDIA_OWNER_ID + " = " + MediaContract.UserEntry.TABLE_NAME + "." + MediaContract.UserEntry.COLUMN_INSTAGRAM_ID);
    }

    private static final String sLatitudeLongitudeSelection = MediaContract.MediaEntry.TABLE_NAME + "." + MediaContract.MediaEntry.COLUMN_LOCATION_LATITUDE + " >= ? AND " + MediaContract.MediaEntry.TABLE_NAME + "." + MediaContract.MediaEntry.COLUMN_LOCATION_LATITUDE + " <= ? AND " + MediaContract.MediaEntry.TABLE_NAME + "." + MediaContract.MediaEntry.COLUMN_LOCATION_LONGITUDE + " >= ? AND " + MediaContract.MediaEntry.TABLE_NAME + "." + MediaContract.MediaEntry.COLUMN_LOCATION_LONGITUDE + " <= ? ";

    private static final String sGetLocationIdSQL = "SELECT " + MediaContract.LocationEntry.COLUMN_INSTAGRAM_LOCATION_ID + " FROM " + MediaContract.LocationEntry.TABLE_NAME + " WHERE " + MediaContract.LocationEntry.TABLE_NAME + "." + MediaContract.LocationEntry.COLUMN_LOCATION_LATITUDE + " >= ? AND " + MediaContract.LocationEntry.TABLE_NAME + "." + MediaContract.LocationEntry.COLUMN_LOCATION_LATITUDE + " <= ? AND " + MediaContract.LocationEntry.TABLE_NAME + "." + MediaContract.LocationEntry.COLUMN_LOCATION_LONGITUDE + " >= ? AND " + MediaContract.LocationEntry.TABLE_NAME + "." + MediaContract.LocationEntry.COLUMN_LOCATION_LONGITUDE + " <= ? ";
    private static final String sMediaTableInnerJoinUserTableSQL = MediaContract.MediaEntry.TABLE_NAME + " INNER JOIN " + MediaContract.UserEntry.TABLE_NAME + " ON " + MediaContract.MediaEntry.TABLE_NAME + "." + MediaContract.MediaEntry.COLUMN_MEDIA_OWNER_ID + " = " + MediaContract.UserEntry.TABLE_NAME + "." + MediaContract.UserEntry.COLUMN_INSTAGRAM_ID;
    private static final String sGetMediaByLocationSQL = "SELECT * FROM " + sMediaTableInnerJoinUserTableSQL + " WHERE " + MediaContract.MediaEntry.TABLE_NAME + "." + MediaContract.MediaEntry.COLUMN_LOCATION_INSTAGRAM_ID + " IN (" + sGetLocationIdSQL + ") ORDER BY " + MediaContract.MediaEntry.TABLE_NAME + "." + MediaContract.MediaEntry.COLUMN_CREATE_TIME + " DESC;";
//    private static final String sGetMediaByLocationSQL = "SELECT * FROM " + sMediaTableInnerJoinUserTableSQL + " WHERE " + MediaContract.MediaEntry.TABLE_NAME + "." + MediaContract.MediaEntry.COLUMN_LOCATION_INSTAGRAM_ID + " IN (" + sGetLocationIdSQL + ");";

    private Cursor getMediaByLatitudeAndLongitude(Uri uri)
    {
        float lat = MediaContract.MediaEntry.getLatFromUri(uri);
        float lng = MediaContract.MediaEntry.getLongFromUri(uri);

        String[] selectionArgs;

        if (lat == Float.POSITIVE_INFINITY || lng == Float.POSITIVE_INFINITY)
        {
            selectionArgs = new String[] {};
        }
        else
        {
            selectionArgs = getArgs(lat, lng);
        }

        Log.d(TAG, "$$$getMediaByLatitudeAndLongitude: the sql is " + sGetMediaByLocationSQL);
        return mOpenHelper.getReadableDatabase().rawQuery(sGetMediaByLocationSQL, selectionArgs);
    }

    private static final String sInstagramIdSelection = MediaContract.UserEntry.TABLE_NAME + "." + MediaContract.UserEntry.COLUMN_INSTAGRAM_ID + " = ?";

    private Cursor getUserByInstagramId(Uri uri, String[] projection, String sortOrder)
    {
        String id = MediaContract.UserEntry.getInstagramIdFromUri(uri);
        String selection = sInstagramIdSelection;
        String[] selectionArgs = { id };

        return mOpenHelper.getReadableDatabase().query(MediaContract.UserEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public static final String sOwnerSelection = MediaContract.MediaEntry.TABLE_NAME + "." + MediaContract.MediaEntry.COLUMN_MEDIA_OWNER_ID + " = ?";

    private Cursor getMediaByOwnerId(Uri uri, String[] projection, String sortOrder)
    {
        String id = MediaContract.MediaEntry.getOwnerIdFromUri(uri);
        String selection = sOwnerSelection;
        String[] selectionArgs = { id };

        return mOpenHelper.getReadableDatabase().query(MediaContract.MediaEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    private static final String sMediaSelection = MediaContract.MediaEntry.TABLE_NAME + "." + MediaContract.MediaEntry.COLUMN_MEDIA_INSTAGRAM_ID + " = ?";

    private Cursor getMediaByInstagramId(Uri uri, String[] projection, String sortOrder)
    {
        String id = MediaContract.MediaEntry.getInstagramIdFromUri(uri);
        String selection = sMediaSelection;
        String[] selectionArgs = { id };

        return mOpenHelper.getReadableDatabase().query(MediaContract.MediaEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    private String[] getArgs(float lat, float lng)
    {
        String[] selectionArgs = new String[4];
        GeoLocationUtil location = GeoLocationUtil.fromDegrees(lat, lng);
        GeoLocationUtil[] geoLocations = location.boundingCoordinates(AppConfig.RADIUS_FROM_DESTINATION, AppConfig.RADIUS_SPHERE);
        selectionArgs[0] = Double.toString(geoLocations[0].getLatitudeInDegrees());
        selectionArgs[1] = Double.toString(geoLocations[1].getLatitudeInDegrees());
        selectionArgs[2] = Double.toString(geoLocations[0].getLongitudeInDegrees());
        selectionArgs[3] = Double.toString(geoLocations[1].getLongitudeInDegrees());
        return selectionArgs;
    }

    @Override
    public boolean onCreate()
    {
        mOpenHelper = new MediaDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        Cursor retCursor;
        switch (sUriMatcher.match(uri))
        {
            case MEDIA:
                retCursor = mOpenHelper.getReadableDatabase().query(MediaContract.MediaEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case USER:
                retCursor = mOpenHelper.getReadableDatabase().query(MediaContract.UserEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case LOCATION:
                retCursor = mOpenHelper.getReadableDatabase().query(MediaContract.LocationEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MEDIA_WITH_LOCATION:
                retCursor = getMediaByLatitudeAndLongitude(uri);
                break;
            case USER_WITH_INSTAGRAM_ID:
                retCursor = getUserByInstagramId(uri, projection, sortOrder);
                break;
            case MEDIA_WITH_OWNER_ID:
                retCursor = getMediaByOwnerId(uri, projection, sortOrder);
                break;
            case MEDIA_WITH_INSTAGRAM_ID:
                retCursor = getMediaByInstagramId(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri)
    {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match)
        {
            case MEDIA_WITH_OWNER_ID:
            case MEDIA_WITH_LOCATION:
                return MediaContract.MediaEntry.CONTENT_TYPE;
            case MEDIA_WITH_INSTAGRAM_ID:
                return MediaContract.MediaEntry.CONTENT_ITEM_TYPE;
            case MEDIA:
                return MediaContract.MediaEntry.CONTENT_TYPE;
            case USER:
                return MediaContract.UserEntry.CONTENT_TYPE;
            case LOCATION:
                return MediaContract.LocationEntry.CONTENT_TYPE;
            case USER_WITH_INSTAGRAM_ID:
                return MediaContract.UserEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match)
        {
            case MEDIA:
            {
                long _id = db.insert(MediaContract.MediaEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MediaContract.MediaEntry.buildMediaUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case USER:
            {
                long _id = db.insert(MediaContract.UserEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MediaContract.UserEntry.buildUserUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case LOCATION:
            {
                long _id = db.insert(MediaContract.LocationEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MediaContract.LocationEntry.buildUserUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (TextUtils.isEmpty(selection))
        {
            selection = "1";
        }
        switch (match)
        {
            case MEDIA:
                rowsDeleted = db.delete(MediaContract.MediaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USER:
                rowsDeleted = db.delete(MediaContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case LOCATION:
                rowsDeleted = db.delete(MediaContract.LocationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match)
        {
            case MEDIA:
                rowsUpdated = db.update(MediaContract.MediaEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case USER:
                rowsUpdated = db.update(MediaContract.UserEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case LOCATION:
                rowsUpdated = db.update(MediaContract.LocationEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values)
    {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case MEDIA:
                db.beginTransaction();
                int returnCount = 0;
                try
                {
                    for (ContentValues value : values)
                    {
                        long _id = db.insert(MediaContract.MediaEntry.TABLE_NAME, null, value);
                        if (_id != -1)
                        {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally
                {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private static UriMatcher buildUriMatcher()
    {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MediaContract.CONTENT_AUTHORITY;
        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MediaContract.PATH_MEDIA, MEDIA);
        matcher.addURI(authority, MediaContract.PATH_USER, USER);
        matcher.addURI(authority, MediaContract.PATH_LOCATION, LOCATION);
        matcher.addURI(authority, MediaContract.PATH_MEDIA + "/*", MEDIA_WITH_LOCATION);
        matcher.addURI(authority, MediaContract.PATH_MEDIA + "/*/*", MEDIA_WITH_OWNER_ID);
        matcher.addURI(authority, MediaContract.PATH_MEDIA + "/*/*/*", MEDIA_WITH_INSTAGRAM_ID);
        matcher.addURI(authority, MediaContract.PATH_USER + "/*", USER_WITH_INSTAGRAM_ID);
        return matcher;
    }
}
