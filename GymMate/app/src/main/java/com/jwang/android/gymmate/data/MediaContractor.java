package com.jwang.android.gymmate.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * @author Jiajun Wang on 6/24/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MediaContractor
{
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.jwang.android.gymmate";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_USER = "user";
    public static final String PATH_MEDIA = "media";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate)
    {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static final class UserEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        // Table name
        public static final String TABLE_NAME = "user";

        //Schema
        public static final String COLUMN_INSTAGRAM_ID = "instagram_id";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_FULL_NAME = "full_name";
        public static final String COLUMN_PROFILE_PICTURE = "profile_picture";

        public static Uri buildUserUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class MediaEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEDIA).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEDIA;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEDIA;

        public static final String TABLE_NAME = "media";

        //Schema
        //TODO: will support comment and likes in future.
        public static final String COLUMN_TAGS = "tags";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_LOCATION_LATITUDE = "location_latitude";
        public static final String COLUMN_LOCATION_LONGITUDE = "location_longitude";
        public static final String COLUMN_CREATE_TIME = "create_time";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_MEDIA_LOW = "media_low";
        public static final String COLUMN_MEDIA_THUMBNAIL = "media_thumbnail";
        public static final String COLUMN_MEDIA_HIGH = "media_high";
        public static final String COLUMN_MEDIA_OWNER_ID = "owner_id";

        public static Uri buildMediaUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }
}
