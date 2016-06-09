package com.app.shovonh.mooveez.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.shovonh.mooveez.MainActivity;
import com.app.shovonh.mooveez.R;

import java.util.TimeZone;

import br.com.goncalves.pugnotification.notification.PugNotification;
import hirondelle.date4j.DateTime;

/**
 * Created by Shovon on 5/17/16.
 */
public class MonthlyNotifications extends BroadcastReceiver {
    public static final String LOG_TAG = MonthlyNotifications.class.getSimpleName();


    @Override
    public void onReceive(Context context, Intent intent) {
        DateTime d = DateTime.now(TimeZone.getDefault());
        if (d.getDay() == 1) {
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

        Intent i = new Intent(context, MonthlyNotifications.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

        String firstOfNext = d.getEndOfMonth().plusDays(1).format("YYYY-MM-DD");
        DateTime dt = new DateTime(firstOfNext + " 06:00:00");

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, dt.getMilliseconds(TimeZone.getDefault()), pi);

    }
}