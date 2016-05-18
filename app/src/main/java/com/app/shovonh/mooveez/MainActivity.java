package com.app.shovonh.mooveez;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.app.shovonh.mooveez.data.AlarmDBHelper;

public class MainActivity extends AppCompatActivity implements ThisMonthFragment.Callback {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static AlarmDBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);


//        boolean monthlyAlarmSet = (PendingIntent.getBroadcast(this, 0, new Intent(this, MonthlyNotifications.class), PendingIntent.FLAG_NO_CREATE) != null);
//
//
//        if (monthlyAlarmSet)
//            Log.v(LOG_TAG, "Alarm set already");
//        else {
//            Log.v(LOG_TAG, "Alarm not set, creating now");
//            Intent intent = new Intent(this, MonthlyNotifications.class);
//            PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
//
//            String firstOfNext = DateTime.now(TimeZone.getDefault()).getEndOfMonth().plusDays(1).format("MM-DD-YYYY");
//            DateTime dt = new DateTime(firstOfNext + "06:00:00");
//
//            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+7000, pi);
//
//
//
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.app.shovonh.mooveez.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(String id, String[] movieDetails) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(id, movieDetails);
        startActivity(intent);
    }
}
