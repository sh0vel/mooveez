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
import android.view.View;

import com.app.shovonh.mooveez.Objs.MovieObj;
import com.app.shovonh.mooveez.Receivers.ReleaseNotifications;
import com.app.shovonh.mooveez.data.AlarmDBHelper;

import org.parceler.Parcels;

import java.util.TimeZone;

import hirondelle.date4j.DateTime;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    private MovieObj movie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        final AlarmDBHelper dbHelper = new AlarmDBHelper(this);
        movie = (MovieObj) Parcels.unwrap(getIntent().getParcelableExtra(ThisMonthFragment.SELECTED_MOVIE_BUNDLE_ID));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (Utilities.alreadyReleased(movie.getReleaseDate())) {
            fab.setVisibility(View.INVISIBLE);
        } else fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReleaseNotifications.class);
                intent.putExtra(ReleaseNotifications.BUNDLE_ID_TITLE, movie.getTitle());
                intent.putExtra(ReleaseNotifications.BUNDLE_ID_ID, movie.getId());
                final PendingIntent pi = PendingIntent.getBroadcast(view.getContext(), movie.getId(), intent, PendingIntent.FLAG_ONE_SHOT);
                String release = movie.getReleaseDate();
                DateTime dt = new DateTime(release + " 06:00:00");

                final AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, dt.getMilliseconds(TimeZone.getDefault()), pi);

                dbHelper.insertData(movie.getTitle(), movie.getReleaseDate(), movie.getId());

                View.OnClickListener snackButton = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        am.cancel(pi);
                        dbHelper.deleteEntry(movie.getId());

                    }
                };

                Snackbar sb = Snackbar.make(view, "Release Reminder Set!", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", snackButton)
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary));
                sb.show();

            }
        });


        if (savedInstanceState == null) {

            Fragment fragment =
                    new MovieDetailsFrag().newInstance(movie);
            getSupportFragmentManager().beginTransaction().add(R.id.movie_details_activity, fragment).commit();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.bottom_in, R.anim.stay_still);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.stay_still, R.anim.bottom_out);

    }

    //    @Override
//    public void onBackPressed() {
//        finish();
//        overridePendingTransition(R.anim.left_in, R.anim.right_out);
//    }
//

}
