package com.app.shovonh.mooveez;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.app.shovonh.mooveez.Receivers.ReleaseNotifications;
import com.app.shovonh.mooveez.data.AlarmDBHelper;

import java.util.ArrayList;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    public static String[] bundle;
    public static FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.app.shovonh.mooveez.R.layout.activity_movie_detail);
        final AlarmDBHelper dbHelper = new AlarmDBHelper(this);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");

        bundle = getIntent().getStringArrayExtra(ThisMonthFragment.MOVIE_DETAILS_BUNDLE_ID);

        fab = (FloatingActionButton) findViewById(R.id.fab);


        if (Utilities.alreadyReleased(bundle[2])) {
            fab.setVisibility(View.INVISIBLE);
        } else fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar sb = Snackbar.make(view, "Release Reminder Set!", Snackbar.LENGTH_LONG);
                sb.show();

                Intent intent = new Intent(view.getContext(), ReleaseNotifications.class);
                intent.putExtra(ReleaseNotifications.BUNDLE_ID_TITLE, bundle[1]);
                intent.putExtra(ReleaseNotifications.BUNDLE_ID_ID, Integer.parseInt(bundle[6]));
                PendingIntent pi = PendingIntent.getBroadcast(view.getContext(), Integer.parseInt(bundle[6]), intent, PendingIntent.FLAG_ONE_SHOT);
                String release = bundle[2];
                DateTime dt = new DateTime(release + " 06:00:00");

                //dt.getMilliseconds(TimeZone.getDefault())
                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, dt.getMilliseconds(TimeZone.getDefault()), pi);

                dbHelper.insertData(bundle[1], bundle[2], Integer.parseInt(bundle[6]));
                ArrayList<MovieObj> objs = dbHelper.getAllMovies();
                for (MovieObj m : objs){
                    Log.v(LOG_TAG, m.getTitle() + ", " + m.getReleaseDate() + ", " + m.getId());
                    //dbHelper.deleteEntry(m.id);
                }


            }
        });


        if (savedInstanceState == null) {

            Fragment fragment =
                    new MovieDetailsFrag().newInstance(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.movie_details_activity, fragment).commit();

        }

    }


}
