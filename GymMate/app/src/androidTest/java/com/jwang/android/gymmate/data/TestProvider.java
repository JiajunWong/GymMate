package com.jwang.android.gymmate.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;

/**
 * @author Jiajun Wang on 6/29/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class TestProvider extends AndroidTestCase
{
    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    /*
           This helper function deletes all records from both database tables using the ContentProvider.
           It also queries the ContentProvider to make sure that the database has been successfully
           deleted, so it cannot be used until the Query and Delete functions have been written
           in the ContentProvider.
    */
    public void deleteAllRecordsFromProvider()
    {
        mContext.getContentResolver().delete(MediaContract.MediaEntry.CONTENT_URI, null, null);
        mContext.getContentResolver().delete(MediaContract.UserEntry.CONTENT_URI, null, null);

        Cursor cursor = mContext.getContentResolver().query(MediaContract.MediaEntry.CONTENT_URI, null, null, null, null);
        assertEquals("Error: Records not deleted from Media table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(MediaContract.UserEntry.CONTENT_URI, null, null, null, null);
        assertEquals("Error: Records not deleted from User table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecords()
    {
        deleteAllRecordsFromProvider();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        deleteAllRecords();
    }

    /*
       This test checks to make sure that the content provider is registered correctly.
    */
    public void testProviderRegistry()
    {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(), MediaProvider.class.getName());
        try
        {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: WeatherProvider registered with authority: " + providerInfo.authority + " instead of authority: " + MediaContract.CONTENT_AUTHORITY, providerInfo.authority, MediaContract.CONTENT_AUTHORITY);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(), false);
        }
    }

    /*
       This test doesn't touch the database.  It verifies that the ContentProvider returns
       the correct type for each type of URI that it can handle.
       Students: Uncomment this test to verify that your implementation of GetType is
       functioning correctly.
    */
    public void testGetType()
    {
        // content://com.jwang.android.gymmate/meida/
        String type = mContext.getContentResolver().getType(MediaContract.MediaEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.jwang.android.gymmate/meida/
        assertEquals("Error: the MediaEntry CONTENT_URI should return MediaEntry.CONTENT_TYPE", MediaContract.MediaEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(MediaContract.UserEntry.CONTENT_URI);
        assertEquals("Error: the LocationEntry CONTENT_URI should return LocationEntry.CONTENT_TYPE", MediaContract.UserEntry.CONTENT_TYPE, type);
    }

    public void testBasicMediaQuery()
    {
        // insert our test records into the database
        MediaDBHelper dbHelper = new MediaDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createUserValues();
        long locationRowId = TestUtilities.insertTextUserValues(mContext);

        // Fantastic.  Now that we have a location, add some weather!
        ContentValues weatherValues = TestUtilities.createMediaValues();

        long weatherRowId = db.insert(MediaContract.MediaEntry.TABLE_NAME, null, weatherValues);
        assertTrue("Unable to Insert WeatherEntry into the Database", weatherRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor weatherCursor = mContext.getContentResolver().query(MediaContract.MediaEntry.CONTENT_URI, null, null, null, null);

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicMediaQuery", weatherCursor, weatherValues);
    }

    public void testBasicUserQuery()
    {
        MediaDBHelper dbHelper = new MediaDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createUserValues();
        long locationRowId = TestUtilities.insertTextUserValues(mContext);

        // Test the basic content provider query
        Cursor locationCursor = mContext.getContentResolver().query(MediaContract.UserEntry.CONTENT_URI, null, null, null, null);
        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicLocationQueries, location query", locationCursor, testValues);

        // Has the NotificationUri been set correctly? --- we can only test this easily against API
        // level 19 or greater because getNotificationUri was added in API level 19.
        if (Build.VERSION.SDK_INT >= 19)
        {
            assertEquals("Error: Location Query did not properly set NotificationUri", locationCursor.getNotificationUri(), MediaContract.UserEntry.CONTENT_URI);
        }
    }

    //TODO: test update, for now because we wont use, I skipped it.

    public void testInsertReadProvider()
    {
        ContentValues testValues = TestUtilities.createUserValues();
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MediaContract.UserEntry.CONTENT_URI, true, tco);
        Uri locationUri = mContext.getContentResolver().insert(MediaContract.UserEntry.CONTENT_URI, testValues);
        // Did our content observer get called?  Students:  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long locationRowId = ContentUris.parseId(locationUri);
        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(MediaContract.UserEntry.CONTENT_URI, null, // leaving "columns" null just returns all the columns.
        null, // cols for "where" clause
        null, // values for "where" clause
        null // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating LocationEntry.", cursor, testValues);
        // Fantastic.  Now that we have a location, add some weather!
        ContentValues weatherValues = TestUtilities.createMediaValues();
        // The TestContentObserver is a one-shot class
        tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MediaContract.MediaEntry.CONTENT_URI, true, tco);
        Uri weatherInsertUri = mContext.getContentResolver().insert(MediaContract.MediaEntry.CONTENT_URI, weatherValues);
        assertTrue(weatherInsertUri != null);

        // Did our content observer get called?  Students:  If this fails, your insert weather
        // in your ContentProvider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        Cursor weatherCursor = mContext.getContentResolver().query(MediaContract.MediaEntry.CONTENT_URI, // Table to Query
        null, // leaving "columns" null just returns all the columns.
        null, // cols for "where" clause
        null, // values for "where" clause
        null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating WeatherEntry insert.", weatherCursor, weatherValues);

        // Add the location values in with the weather data so that we can make
        // sure that the join worked and we actually get all the values back
        weatherValues.putAll(testValues);

        // Get the joined Weather and Location data
        weatherCursor = mContext.getContentResolver().query(MediaContract.MediaEntry.buildMediaWithLocation(TestUtilities.TEST_LOCATION, "37.551492951", "-122.315159747"), null, // leaving "columns" null just returns all the columns.
        null, // cols for "where" clause
        null, // values for "where" clause
        null // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Weather and Location Data.", weatherCursor, weatherValues);
    }

    public void testDeleteRecords()
    {
        testInsertReadProvider();

        // Register a content observer for our location delete.
        TestUtilities.TestContentObserver locationObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MediaContract.UserEntry.CONTENT_URI, true, locationObserver);

        // Register a content observer for our weather delete.
        TestUtilities.TestContentObserver weatherObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MediaContract.MediaEntry.CONTENT_URI, true, weatherObserver);

        deleteAllRecordsFromProvider();

        // Students: If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        locationObserver.waitForNotificationOrFail();
        weatherObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(locationObserver);
        mContext.getContentResolver().unregisterContentObserver(weatherObserver);
    }
}
