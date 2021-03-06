package com.jwang.android.gymmate.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.jwang.android.gymmate.util.AppConfig;

/**
 * @author Jiajun Wang on 6/24/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MediaContract
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
    public static final String PATH_LOCATION = "location";
    public static final String PATH_PAGINATION = "pagination";

    public static final class LocationEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;

        // Table name
        public static final String TABLE_NAME = "location";

        //Schema
        public static final String COLUMN_INSTAGRAM_LOCATION_ID = "location_instagram_id";
        public static final String COLUMN_LOCATION_NAME = "location_name";
        public static final String COLUMN_LOCATION_LONGITUDE = "location_longitude";
        public static final String COLUMN_LOCATION_LATITUDE = "location_latitude";

        public static Uri buildLocationUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildLocationUriWithInstagramId(String id)
        {
            return CONTENT_URI.buildUpon().appendPath(AppConfig.LOCATION).appendQueryParameter(COLUMN_INSTAGRAM_LOCATION_ID, id).build();
        }

        public static String getInstagramIdFromUri(Uri uri)
        {
            return uri.getQueryParameter(COLUMN_INSTAGRAM_LOCATION_ID);
        }
    }

    public static final class PaginationEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PAGINATION).build();

        // Data type
        public static final String TYPE_USER = "type_user";
        public static final String TYPE_LOCATION = "type_location";
        public static final String TYPE_OTHERS = "type_others";

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PAGINATION;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PAGINATION;

        // Table name
        public static final String TABLE_NAME = "pagination";

        //Schema
        public static final String COLUMN_DATA_TYPE = "data_type";
        public static final String COLUMN_DATA_ID = "data_id";
        public static final String COLUMN_DATA_PAGINATION = "data_pagination";

        public static Uri buildLocationUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildLocationUriWithId(String type, String id)
        {
            return CONTENT_URI.buildUpon().appendPath(AppConfig.LOCATION).appendQueryParameter(COLUMN_DATA_TYPE, type).appendQueryParameter(COLUMN_DATA_ID, id).build();
        }

        public static String getDataIdFromUri(Uri uri)
        {
            return uri.getQueryParameter(COLUMN_DATA_ID);
        }

        public static String getDataTypeFromUri(Uri uri)
        {
            return uri.getQueryParameter(COLUMN_DATA_TYPE);
        }
    }

    public static final class UserEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        // Table name
        public static final String TABLE_NAME = "user";

        //Schema
        public static final String COLUMN_INSTAGRAM_ID = "user_instagram_id";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_FULL_NAME = "full_name";
        public static final String COLUMN_PROFILE_PICTURE = "profile_picture";
        public static final String COLUMN_MEDIA_COUNT = "media_count";
        public static final String COLUMN_FOLLOWED_BY_COUNT = "followed_by_count";
        public static final String COLUMN_FOLLOW_COUNT = "follow_count";

        public static Uri buildUserUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildUserWithInstagramId(String id)
        {
            return CONTENT_URI.buildUpon().appendPath(AppConfig.USER).appendQueryParameter(COLUMN_INSTAGRAM_ID, id).build();
        }

        public static String getInstagramIdFromUri(Uri uri)
        {
            return uri.getQueryParameter(COLUMN_INSTAGRAM_ID);
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
        public static final String COLUMN_MEDIA_TAGS = "tags";
        public static final String COLUMN_MEDIA_TYPE = "type";
        public static final String COLUMN_LOCATION_LATITUDE = "location_latitude";
        public static final String COLUMN_LOCATION_LONGITUDE = "location_longitude";
        public static final String COLUMN_LOCATION_INSTAGRAM_ID = "media_location_instagram_id";
        public static final String COLUMN_LOCATION_NAME = "media_location_name";
        public static final String COLUMN_CREATE_TIME = "create_time";
        public static final String COLUMN_MEDIA_LINK = "link";
        public static final String COLUMN_MEDIA_OWNER_ID = "owner_id";
        public static final String COLUMN_MEDIA_INSTAGRAM_ID = "media_instagram_id";
        public static final String COLUMN_MEDIA_IMAGE_LOW = "media_image_low";
        public static final String COLUMN_MEDIA_IMAGE_THUMBNAIL = "media_image_thumbnail";
        public static final String COLUMN_MEDIA_IMAGE_STANDARD = "media_image_standard";
        public static final String COLUMN_MEDIA_VIDEO_LOW_BANDWIDTH = "media_video_low_bandwidth";
        public static final String COLUMN_MEDIA_VIDEO_STANDARD_RES = "media_video_standard";
        public static final String COLUMN_MEDIA_VIDEO_LOW_RES = "media_video_low";
        public static final String COLUMN_CAPTION_TEXT = "caption_text";
        public static final String COLUMN_MEDIA_ENABLED = "media_enabled";

        public static Uri buildMediaUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMediaWithLocation(String locationSetting, String lat, String lng)
        {
            return CONTENT_URI.buildUpon().appendPath(locationSetting).appendQueryParameter(COLUMN_LOCATION_LATITUDE, lat).appendQueryParameter(COLUMN_LOCATION_LONGITUDE, lng).build();
        }

        public static Uri buildMediaWithOwnerId(String id)
        {
            return CONTENT_URI.buildUpon().appendPath(AppConfig.MEDIA).appendPath(AppConfig.USER).appendQueryParameter(COLUMN_MEDIA_OWNER_ID, id).build();
        }

        public static Uri buildMediaWithInstagramId(String id)
        {
            return CONTENT_URI.buildUpon().appendPath(AppConfig.LOCATION).appendPath(AppConfig.USER).appendPath(AppConfig.MEDIA).appendQueryParameter(COLUMN_MEDIA_INSTAGRAM_ID, id).build();
        }

        public static String getInstagramIdFromUri(Uri uri)
        {
            return uri.getQueryParameter(COLUMN_MEDIA_INSTAGRAM_ID);
        }

        public static String getOwnerIdFromUri(Uri uri)
        {
            return uri.getQueryParameter(COLUMN_MEDIA_OWNER_ID);
        }

        public static float getLatFromUri(Uri uri)
        {
            String lat = uri.getQueryParameter(COLUMN_LOCATION_LATITUDE);
            if (!TextUtils.isEmpty(lat) && !lat.equals("null"))
            {
                return Float.valueOf(lat);
            }
            else
            {
                return Float.POSITIVE_INFINITY;
            }
        }

        public static float getLongFromUri(Uri uri)
        {
            String lat = uri.getQueryParameter(COLUMN_LOCATION_LONGITUDE);
            if (!TextUtils.isEmpty(lat) && !lat.equals("null"))
            {
                return Float.valueOf(lat);
            }
            else
            {
                return Float.POSITIVE_INFINITY;
            }
        }
    }
}
