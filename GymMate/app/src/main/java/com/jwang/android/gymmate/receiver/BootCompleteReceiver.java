package com.jwang.android.gymmate.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jwang.android.gymmate.sync.GymMateMediaSyncService;

/**
 * @author Jiajun Wang on 7/20/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class BootCompleteReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent i = new Intent(context, GymMateMediaSyncService.class);
        context.startService(i);
    }
}
