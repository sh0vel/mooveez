package com.app.shovonh.mooveez;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import br.com.goncalves.pugnotification.notification.PugNotification;

/**
 * Created by Shovon on 5/13/16.
 */
public class Notifications extends BroadcastReceiver{
    private static final String LOG_TAG = Notifications.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        PugNotification.with(context)
                .load()
                .title(intent.getStringArrayExtra(ThisMonthFragment.MOVIE_DETAILS_BUNDLE_ID)[1])
                .message("Released Today!")
                .smallIcon(R.mipmap.ic_launcher)
                .largeIcon(R.mipmap.ic_launcher)
                .autoCancel(true)
                .simple()
                .build();
    }



}
