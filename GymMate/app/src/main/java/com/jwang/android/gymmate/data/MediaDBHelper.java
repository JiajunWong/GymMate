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
    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "media.db";

    public MediaDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //Create user table
        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + MediaContract.UserEntry.TABLE_NAME + " (" +
                MediaContract.UserEntry._ID + " INTEGER PRIMARY KEY," +
                MediaContract.UserEntry.COLUMN_INSTAGRAM_ID + " BIGINT UNIQUE NOT NULL, " +
                MediaContract.UserEntry.COLUMN_USERNAME + " TEXT NOT NULL, " +
                MediaContract.UserEntry.COLUMN_FULL_NAME + " TEXT NOT NULL, " +
                MediaContract.UserEntry.COLUMN_PROFILE_PICTURE+ " TEXT NOT NULL " +
                " );";
        Log.d(TAG, "SQL Statement: "+SQL_CREATE_USER_TABLE);

        final String SQL_CREATE_MEDIA_TABLE = "CREATE TABLE " + MediaContract.MediaEntry.TABLE_NAME + " (" +
                MediaContract.MediaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MediaContract.MediaEntry.COLUMN_TAGS + " TEXT, " +
                MediaContract.MediaEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                MediaContract.MediaEntry.COLUMN_LOCATION_LATITUDE + " REAL NOT NULL, " +
                MediaContract.MediaEntry.COLUMN_LOCATION_LONGITUDE + " REAL NOT NULL, " +
                MediaContract.MediaEntry.COLUMN_CREATE_TIME + " INTEGER NOT NULL, " +
                MediaContract.MediaEntry.COLUMN_LINK + " TEXT NOT NULL, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_OWNER_ID + " INTEGER NOT NULL, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_INSTAGRAM_ID + " TEXT NOT NULL, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_LOW + " TEXT, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_THUMBNAIL + " TEXT, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_STANDARD + " TEXT, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_VIDEO_LOW_BANDWIDTH + " TEXT, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_VIDEO_STANDARD_RES + " TEXT, " +
                MediaContract.MediaEntry.COLUMN_MEDIA_VIDEO_LOW_RES + " TEXT" +
                " );";
        Log.d(TAG, "SQL Statement: "+SQL_CREATE_MEDIA_TABLE);

        sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MEDIA_TABLE);
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
        onCreate(sqLiteDatabase);
    }
}
