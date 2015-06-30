package com.jwang.android.gymmate.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.jwang.android.gymmate.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/**
 * @author Jiajun Wang on 6/29/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class TestUtilities extends AndroidTestCase
{
    static final String TEST_LOCATION = "99705";

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues)
    {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues)
    {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet)
        {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() + "' did not match the expected value '" + expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createMediaValues()
    {
        ContentValues mediaValues = new ContentValues();
        mediaValues.put(MediaContract.MediaEntry.COLUMN_TAGS, "highresaudio");
        mediaValues.put(MediaContract.MediaEntry.COLUMN_TYPE, "image");
        mediaValues.put(MediaContract.MediaEntry.COLUMN_LOCATION_LATITUDE, 37.551492951);
        mediaValues.put(MediaContract.MediaEntry.COLUMN_LOCATION_LONGITUDE, -122.315159747);
        mediaValues.put(MediaContract.MediaEntry.COLUMN_CREATE_TIME, 1435609196);
        mediaValues.put(MediaContract.MediaEntry.COLUMN_LINK, "https://instagram.com/p/4hreiusTAP/");
        mediaValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_LOW, "https://scontent.cdninstagram.com/hphotos-xaf1/t51.2885-15/s320x320/e15/11337199_1040192862665131_775189267_n.jpg");
        mediaValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_THUMBNAIL, "https://scontent.cdninstagram.com/hphotos-xaf1/t51.2885-15/s150x150/e15/11337199_1040192862665131_775189267_n.jpg");
        mediaValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_STANDARD, "https://scontent.cdninstagram.com/hphotos-xaf1/t51.2885-15/e15/11337199_1040192862665131_775189267_n.jpg");
        mediaValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_OWNER_ID, 1525713147);
        mediaValues.put(MediaContract.MediaEntry.COLUMN_MEDIA_INSTAGRAM_ID, "1018286205637308431_1525713147");
        return mediaValues;
    }

    static ContentValues createUserValues()
    {
        ContentValues userContentValues = new ContentValues();
        userContentValues.put(MediaContract.UserEntry.COLUMN_INSTAGRAM_ID, 1525713147);
        userContentValues.put(MediaContract.UserEntry.COLUMN_USERNAME, "pono_music");
        userContentValues.put(MediaContract.UserEntry.COLUMN_FULL_NAME, "Pono Music");
        userContentValues.put(MediaContract.UserEntry.COLUMN_PROFILE_PICTURE, "https://igcdn-photos-f-a.akamaihd.net/hphotos-ak-xfa1/t51.2885-19/11116808_362846360581069_985327361_a.jpg");
        return userContentValues;
    }

    static long insertTextUserValues(Context context)
    {
        MediaDBHelper dbHelper = new MediaDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createUserValues();
        long locationRowId;
        locationRowId = db.insert(MediaContract.UserEntry.TABLE_NAME, null, testValues);
        // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole Location Values", locationRowId != -1);

        return locationRowId;
    }

    /*
            Students: The functions we provide inside of TestProvider use this utility class to test
            the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
            CTS tests.
    
            Note that this only tests that the onChange function is called; it does not test that the
            correct Uri is returned.
    */
    static class TestContentObserver extends ContentObserver
    {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver()
        {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht)
        {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange)
        {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri)
        {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail()
        {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000)
            {
                @Override
                protected boolean check()
                {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver()
    {
        return TestContentObserver.getTestContentObserver();
    }
}
