package com.app.shovonh.mooveez.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.app.shovonh.mooveez.MovieObj;
import com.app.shovonh.mooveez.data.AlarmDBHelper;

import java.util.ArrayList;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Created by Shovon on 5/17/16.
 */
public class RebootListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("RebootListener", "Reboot has been noticed, re setting all alarms");
        AlarmDBHelper dbHelper = new AlarmDBHelper(context);
        ArrayList<MovieObj> objs = dbHelper.getAllMovies();
        for (MovieObj m : objs){
            Intent i = new Intent(context, ReleaseNotifications.class);
            i.putExtra(ReleaseNotifications.BUNDLE_ID_TITLE, m.getTitle());
            i.putExtra(ReleaseNotifications.BUNDLE_ID_ID, m.getId());
            PendingIntent pi = PendingIntent.getBroadcast(context, m.getId(), i,PendingIntent.FLAG_ONE_SHOT);

            DateTime dt = new DateTime(m.getReleaseDate() + " 06:00:00");

            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, dt.getMilliseconds(TimeZone.getDefault()), pi);
        }

    }
}
