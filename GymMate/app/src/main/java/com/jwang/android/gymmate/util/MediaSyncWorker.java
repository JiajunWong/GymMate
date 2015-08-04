package com.jwang.android.gymmate.util;

import android.content.Context;

import com.jwang.android.gymmate.data.MediaContract;
import com.jwang.android.gymmate.task.RequestInstagramLocationTask;
import com.jwang.android.gymmate.task.media_task.RequestMainLocationMediaTask;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 7/16/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MediaSyncWorker
{
    private static final String TAG = MediaSyncWorker.class.getSimpleName();
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    public static final int MEDIA_NOTIFICATION_ID = 3004;
    public static final String KEY_START_FROM_NOTIFICATION = "start_from_notification";
    private static MediaSyncWorker sMediaSyncWorker;
    private Context mContext;
    private ArrayList<String> mInstagramLocationIds = new ArrayList<>();

    private MediaSyncWorker(Context context)
    {
        mContext = context;
    }

    public static MediaSyncWorker getInstance(Context context)
    {
        if (sMediaSyncWorker == null)
        {
            sMediaSyncWorker = new MediaSyncWorker(context);
        }
        return sMediaSyncWorker;
    }

    public void startFetchGymMedia(String[] location)
    {
        if (InstagramOauth.getsInstance().getSession().isActive())
        {
            if (mInstagramLocationIds != null && !mInstagramLocationIds.isEmpty())
            {
                requestMedias();
            }
            else
            {
                requestLocationids(location);
            }
        }
    }

    public ArrayList<String> getInstagramLocationIds()
    {
        return mInstagramLocationIds;
    }

    public void requestLocationids(String[] location)
    {
        RequestInstagramLocationTask requestInstagramLocationTask = new RequestInstagramLocationTask(mContext);
        requestInstagramLocationTask.setOnRequestLocationIdFinishedListener(mOnRequestLocationIdFinishedListener);
        requestInstagramLocationTask.execute(location[1], location[0]); //lat, lng
    }

    private RequestInstagramLocationTask.OnRequestLocationIdFinishedListener mOnRequestLocationIdFinishedListener = new RequestInstagramLocationTask.OnRequestLocationIdFinishedListener()
    {
        @Override
        public void onFinished(ArrayList<String> locations)
        {
            mInstagramLocationIds.clear();
            mInstagramLocationIds.addAll(locations);
            requestMedias();
        }
    };

    private void requestMedias()
    {
        RequestMainLocationMediaTask requestMainLocationMediaTask = new RequestMainLocationMediaTask(mContext);
        requestMainLocationMediaTask.execute();
    }

    //    private void notifyMedia()
    //    {
    //        //checking the last update and notify if it' the first of the day
    //        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    //        String displayNotificationsKey = mContext.getString(R.string.pref_enable_notifications_key);
    //        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey, Boolean.parseBoolean(mContext.getString(R.string.pref_enable_notifications_default)));
    //
    //        if (displayNotifications)
    //        {
    //
    //            String lastNotificationKey = mContext.getString(R.string.pref_last_notification);
    //            long lastSync = prefs.getLong(lastNotificationKey, System.currentTimeMillis());
    //
    //            if (System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS)
    //            {
    //                Resources resources = mContext.getResources();
    //                String title = mContext.getString(R.string.app_name);
    //                String contentText = mContext.getString(R.string.notification_content);
    //                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext).setColor(resources.getColor(R.color.primary_color)).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title).setContentText(contentText);
    //
    //                Intent resultIntent = new Intent(mContext, LoginActivity.class);
    //                resultIntent.putExtra(KEY_START_FROM_NOTIFICATION, true);
    //
    //                TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
    //                stackBuilder.addNextIntent(resultIntent);
    //                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    //                mBuilder.setContentIntent(resultPendingIntent);
    //
    //                NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    //                // MEDIA_NOTIFICATION_ID allows you to update the notification later on.
    //                mNotificationManager.notify(MEDIA_NOTIFICATION_ID, mBuilder.build());
    //            }
    //            //refreshing last sync
    //            SharedPreferences.Editor editor = prefs.edit();
    //            editor.putLong(lastNotificationKey, System.currentTimeMillis());
    //            editor.commit();
    //        }
    //    }
}
