package com.app.shovonh.mooveez.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.app.shovonh.mooveez.NotificationHandler;
import com.app.shovonh.mooveez.R;
import com.app.shovonh.mooveez.data.AlarmDBHelper;

import br.com.goncalves.pugnotification.notification.PugNotification;

/**
 * Created by Shovon on 5/13/16.
 */
public class ReleaseNotifications extends BroadcastReceiver {
    private static final String LOG_TAG = ReleaseNotifications.class.getSimpleName();
    public static final String BUNDLE_ID_TITLE = "title";
    public static final String BUNDLE_ID_ID = "id";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_ID_TITLE, intent.getStringExtra(BUNDLE_ID_TITLE));

        PugNotification.with(context)
                .load()
                .title(intent.getStringExtra(BUNDLE_ID_TITLE))
                .message("Released Today!")
                .largeIcon(R.mipmap.ic_launcher)
                .smallIcon(R.drawable.small_icon)
                .identifier(intent.getIntExtra(BUNDLE_ID_ID, 1))
                .click(NotificationHandler.class, bundle)
                .autoCancel(true)
                .simple()
                .build();
        AlarmDBHelper dbHelper = new AlarmDBHelper(context);
        dbHelper.deleteEntry(intent.getIntExtra(BUNDLE_ID_ID, 1));


    }




}
