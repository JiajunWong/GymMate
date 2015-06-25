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
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "media.db";

    public MediaDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //Create user table
        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + MediaContractor.UserEntry.TABLE_NAME + " (" +
                MediaContractor.UserEntry._ID + " INTEGER PRIMARY KEY," +
                MediaContractor.UserEntry.COLUMN_INSTAGRAM_ID + " INTEGER UNIQUE NOT NULL, " +
                MediaContractor.UserEntry.COLUMN_USERNAME + " TEXT NOT NULL, " +
                MediaContractor.UserEntry.COLUMN_FULL_NAME + " TEXT NOT NULL, " +
                MediaContractor.UserEntry.COLUMN_PROFILE_PICTURE+ " TEXT NOT NULL " +
                " );";
        Log.d(TAG, "SQL Statement: "+SQL_CREATE_USER_TABLE);

        final String SQL_CREATE_MEDIA_TABLE = "CREATE TABLE " + MediaContractor.MediaEntry.TABLE_NAME + " (" +
                MediaContractor.MediaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MediaContractor.MediaEntry.COLUMN_TAGS + " TEXT, " +
                MediaContractor.MediaEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                MediaContractor.MediaEntry.COLUMN_LOCATION_LATITUDE + " REAL NOT NULL, " +
                MediaContractor.MediaEntry.COLUMN_LOCATION_LONGITUDE + " REAL NOT NULL, " +
                MediaContractor.MediaEntry.COLUMN_CREATE_TIME + " INTEGER NOT NULL, " +
                MediaContractor.MediaEntry.COLUMN_LINK + " TEXT NOT NULL, " +
                MediaContractor.MediaEntry.COLUMN_MEDIA_LOW + " TEXT NOT NULL, " +
                MediaContractor.MediaEntry.COLUMN_MEDIA_THUMBNAIL + " TEXT NOT NULL, " +
                MediaContractor.MediaEntry.COLUMN_MEDIA_HIGH + " TEXT NOT NULL, " +
                MediaContractor.MediaEntry.COLUMN_MEDIA_OWNER_ID + " INTEGER NOT NULL, " +
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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MediaContractor.UserEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MediaContractor.MediaEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
