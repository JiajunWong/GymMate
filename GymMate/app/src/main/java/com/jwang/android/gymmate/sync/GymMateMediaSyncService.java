package com.jwang.android.gymmate.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.jwang.android.gymmate.adapter.MediaSyncAdapter;

public class GymMateMediaSyncService extends Service
{
    private static final Object sSyncAdapterLock = new Object();
    private static MediaSyncAdapter sMediaSyncAdapter = null;

    @Override
    public void onCreate()
    {
        Log.d("SunshineSyncService", "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock)
        {
            if (sMediaSyncAdapter == null)
            {
                sMediaSyncAdapter = new MediaSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return sMediaSyncAdapter.getSyncAdapterBinder();
    }
}
