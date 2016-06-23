package com.app.shovonh.mooveez;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.shovonh.mooveez.Objs.MovieObj;
import com.app.shovonh.mooveez.Receivers.ReleaseNotifications;
import com.app.shovonh.mooveez.data.AlarmDBHelper;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.TimeZone;

import hirondelle.date4j.DateTime;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailsFrag.OnFragmentInteractionListener {
    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    boolean mAlreadyReleased = false;
    private MovieObj movie;
    ImageView backdrop;
    TextView title, genres;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ProgressWheel wheel;
    FABToolbarLayout fabToolbarLayout;
    LinearLayout fabToolbar;
    View tickets, reminder;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);
        final AlarmDBHelper dbHelper = new AlarmDBHelper(this);
        movie = (MovieObj) Parcels.unwrap(getIntent().getParcelableExtra(MovieRecyclerFrag.SELECTED_MOVIE_BUNDLE_ID));
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        backdrop = (ImageView) findViewById(R.id.image);
        title = (TextView) findViewById(R.id.movie_title);
        genres = (TextView) findViewById(R.id.genres);
        wheel = (ProgressWheel) findViewById(R.id.progress_wheel2);
        fabToolbarLayout = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
        fabToolbar = (LinearLayout) findViewById(R.id.fabtoolbar_toolbar);

        mAlreadyReleased = Utilities.alreadyReleased(movie.getReleaseDate());


        fab = (FloatingActionButton) findViewById(R.id.fab);

        if (mAlreadyReleased && fab != null) {
            fab.setImageResource(R.drawable.ticket);
        } else {
            tickets = findViewById(R.id.fat_tickets);
            reminder = findViewById(R.id.fat_reminder);

            tickets.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (movie.getReleaseType() != 3) {
                        fabToolbarLayout.hide();
                        fab.hide();
                        Snackbar sb = Snackbar.make(view, "Not released in theatres.", Snackbar.LENGTH_LONG)
                                .setCallback(new Snackbar.Callback() {
                                    @Override
                                    public void onDismissed(Snackbar snackbar, int event) {
                                        super.onDismissed(snackbar, event);
                                        fab.show();
                                        fabToolbarLayout.show();
                                    }
                                });
                        sb.show();
                    } else
                        openMovieTicketInfo();
                }
            });

            reminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fabToolbarLayout.hide();
                    fab.hide();
                    Intent intent = new Intent(view.getContext(), ReleaseNotifications.class);
                    intent.putExtra(ReleaseNotifications.BUNDLE_ID_TITLE, movie.getTitle());
                    intent.putExtra(ReleaseNotifications.BUNDLE_ID_ID, movie.getId());
                    final PendingIntent pi = PendingIntent.getBroadcast(view.getContext(), movie.getId(), intent, PendingIntent.FLAG_ONE_SHOT);
                    String release = movie.getReleaseDate();
                    DateTime dt = new DateTime(release + " 06:00:00");
                    final AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, dt.getMilliseconds(TimeZone.getDefault()), pi);

                    //am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, pi);

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
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    super.onDismissed(snackbar, event);
                                    fab.show();
                                }
                            });
                    sb.show();


                }
            });
        }

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mAlreadyReleased) {
                        if (movie.getReleaseType() != 3) {
                            fab.hide();
                            Snackbar sb = Snackbar.make(view, "Not released in theatres.", Snackbar.LENGTH_LONG)
                                    .setCallback(new Snackbar.Callback() {
                                        @Override
                                        public void onDismissed(Snackbar snackbar, int event) {
                                            super.onDismissed(snackbar, event);
                                            fab.show();
                                        }
                                    });
                            sb.show();
                        } else
                        openMovieTicketInfo();
                    } else {
                        fabToolbarLayout.show();
                    }
                }
            });
        }


        if (savedInstanceState == null) {
            Fragment fragment =
                    new MovieDetailsFrag().newInstance(movie);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();

        }
    }

    void openMovieTicketInfo() {
        Uri uri = Uri.parse("http://www.google.com/#q=Showtimes for " + movie.getTitle());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (fabToolbar.isShown())
            fabToolbarLayout.hide();
        else
            super.onBackPressed();
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

    @Override
    public void setupActionBar(String imgURL, String title, String subtitle) {
        Picasso.with(this).load(imgURL).into(backdrop, new Callback() {
            @Override
            public void onSuccess() {
                wheel.stopSpinning();
            }

            @Override
            public void onError() {

            }
        });
        this.title.setText(title);
        genres.setText(subtitle);
    }
}
