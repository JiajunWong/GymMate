package com.jwang.android.gymmate.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jiajunwang on 6/24/15.
 */
public class MediaDBHelper extends SQLiteOpenHelper
{
    private static final String TAG = MediaDBHelper.class.getSimpleName();
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 21;
    private static final String DATABASE_NAME = "gymmate.db";

    public MediaDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //Create location table
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + MediaContract.LocationEntry.TABLE_NAME + " (" +
                MediaContract.LocationEntry._ID + " INTEGER PRIMARY KEY," +
                MediaContract.LocationEntry.COLUMN_INSTAGRAM_LOCATION_ID + " BIGINT UNIQUE NOT NULL, " +
                MediaContract.LocationEntry.COLUMN_LOCATION_LATITUDE + " REAL, " +
                MediaContract.LocationEntry.COLUMN_LOCATION_LONGITUDE + " REAL, " +
                MediaContract.LocationEntry.COLUMN_LOCATION_NAME + " TEXT " + " );";
        Log.d(TAG, "SQL Statement: "+SQL_CREATE_LOCATION_TABLE);

        final String SQL_CREATE_PAGINATION_TABLE = "CREATE TABLE " + MediaContract.PaginationEntry.TABLE_NAME + " (" +
                MediaContract.PaginationEntry._ID + " INTEGER PRIMARY KEY," +
                MediaContract.PaginationEntry.COLUMN_DATA_TYPE + " TEXT NOT NULL, " +
                MediaContract.PaginationEntry.COLUMN_DATA_ID + " BIGINT UNIQUE NOT NULL, " +
                MediaContract.PaginationEntry.COLUMN_DATA_PAGINATION + " TEXT NOT NULL " +
                " );";

        //Create user table
        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + MediaContract.UserEntry.TABLE_NAME + " (" +
                MediaContract.UserEntry._ID + " INTEGER PRIMARY KEY," +
                MediaContract.UserEntry.COLUMN_INSTAGRAM_ID + " BIGINT UNIQUE NOT NULL, " +
                MediaContract.UserEntry.COLUMN_USERNAME + " TEXT NOT NULL, " +
                MediaContract.UserEntry.COLUMN_FULL_NAME + " TEXT NOT NULL, " +
                MediaContract.UserEntry.COLUMN_PROFILE_PICTURE+ " TEXT NOT NULL, " +
                MediaContract.UserEntry.COLUMN_MEDIA_COUNT+ " INTEGER, " +
                MediaContract.UserEntry.COLUMN_FOLLOWED_BY_COUNT+ " INTEGER, " +
                MediaContract.UserEntry.COLUMN_FOLLOW_COUNT+ " INTEGER" +
                " );";
        Log.d(TAG, "SQL Statement: "+SQL_CREATE_USER_TABLE);

        final String SQL_CREATE_MEDIA_TABLE = "CREATE TABLE " + MediaContract.MediaEntry.TABLE_NAME + " (" +
                MediaContract.MediaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_TAGS + " TEXT, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_TYPE + " TEXT, " +
                MediaContract.MediaEntry.COLUMN_LOCATION_LATITUDE + " REAL, " +
                MediaContract.MediaEntry.COLUMN_LOCATION_LONGITUDE + " REAL, " +
                MediaContract.MediaEntry.COLUMN_LOCATION_INSTAGRAM_ID + " BIGINT, " +
                MediaContract.MediaEntry.COLUMN_LOCATION_NAME + " TEXT, " +
                MediaContract.MediaEntry.COLUMN_CREATE_TIME + " INTEGER NOT NULL, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_LINK + " TEXT NOT NULL, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_OWNER_ID + " INTEGER NOT NULL, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_INSTAGRAM_ID + " TEXT NOT NULL, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_LOW + " TEXT, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_THUMBNAIL + " TEXT, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_STANDARD + " TEXT, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_VIDEO_LOW_BANDWIDTH + " TEXT, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_VIDEO_STANDARD_RES + " TEXT, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_VIDEO_LOW_RES + " TEXT, " +
                MediaContract.MediaEntry.COLUMN_CAPTION_TEXT + " TEXT, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_ENABLED + " TEXT" +
                " );";
        Log.d(TAG, "SQL Statement: "+SQL_CREATE_MEDIA_TABLE);

        sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MEDIA_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_LOCATION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PAGINATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MediaContract.UserEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MediaContract.MediaEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MediaContract.LocationEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MediaContract.PaginationEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
