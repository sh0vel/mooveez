package com.app.shovonh.mooveez.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.app.shovonh.mooveez.MainActivity;
import com.app.shovonh.mooveez.R;

import java.util.TimeZone;

import br.com.goncalves.pugnotification.notification.PugNotification;
import hirondelle.date4j.DateTime;

/**
 * Created by Shovon on 5/17/16.
 */
public class MonthlyNotifications extends BroadcastReceiver{
    public static final String LOG_TAG = MonthlyNotifications.class.getSimpleName();


    @Override
    public void onReceive(Context context, Intent intent) {
        DateTime d = DateTime.now(TimeZone.getDefault());
        Log.v(LOG_TAG, "Day: "+d.getDay());
            Log.v(LOG_TAG, "Notification Created");
            PugNotification.with(context)
                    .load()
                    .title("New Month, New Movies!")
                    .message("Tap to checkout this months most popular releases.")
                    .largeIcon(R.mipmap.ic_launcher)
                    .smallIcon(R.drawable.small_icon)
                    .click(MainActivity.class)
                    .identifier(d.getMonth())
                    .autoCancel(true)
                    .simple()
                    .build();


    }
}
