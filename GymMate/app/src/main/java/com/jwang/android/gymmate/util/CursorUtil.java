package com.jwang.android.gymmate.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.jwang.android.gymmate.data.MediaContract;

/**
 * @author Jiajun Wang on 7/15/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class CursorUtil
{
    private static final String TAG = CursorUtil.class.getSimpleName();

    public static long minTimeStampByLocation(Context context, String lat, String lng)
    {
        return 0;
    }

    public static String minTimeStampByUserId(Context context, String id)
    {
        Uri uri = MediaContract.MediaEntry.CONTENT_URI;
        String[] PROJECTION = { MediaContract.MediaEntry.COLUMN_CREATE_TIME };
        String selection = MediaContract.MediaEntry.TABLE_NAME + "." + MediaContract.MediaEntry.COLUMN_MEDIA_OWNER_ID + " = ?";
        String[] selectionArgs = { id };
        String sortOrder = MediaContract.MediaEntry.COLUMN_CREATE_TIME + " ASC LIMIT 1";
        Cursor cursor = context.getContentResolver().query(uri, PROJECTION, selection, selectionArgs, sortOrder);
        if (cursor != null)
        {
            cursor.moveToFirst();
            int index_create_time = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_CREATE_TIME);
            String date = cursor.getString(index_create_time);
            if (!TextUtils.isEmpty(date))
                Log.w(TAG, date);
            cursor.close();
            return date;
        }
        return "";
    }
}
