package com.app.shovonh.mooveez;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.app.shovonh.mooveez.Objs.MovieObj;
import com.app.shovonh.mooveez.Receivers.MonthlyNotifications;

import org.parceler.Parcels;

import java.util.TimeZone;

import hirondelle.date4j.DateTime;

public class MainActivity extends AppCompatActivity implements MovieRecyclerFrag.Callback {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(Utilities.getMonthName() + " Releases");
        getSupportActionBar().setSubtitle("Powered by TMDb");
        toolbar.setTitleTextColor(Color.WHITE);

        FrameLayout card = (FrameLayout) findViewById(R.id.no_network_view);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);


        setReciever();
        rateSnackBar(findViewById(R.id.container));


        if (hasConnection(this)) {
            card.setVisibility(View.GONE);
            Fragment fragment = new MovieRecyclerFrag().newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.container_grid, fragment).commit();
        } else {
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = getIntent();
                    finish();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });
        }

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
        if (id == R.id.action_rate) {
            rateApp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setReciever() {
        String s = mPrefs.getString("isReceiverSet", "no");
        if (s.equals("no")) {
            DateTime d = DateTime.now(TimeZone.getDefault());
            if (d.getDay() != 1) {
                Intent i = new Intent(this, MonthlyNotifications.class);
                PendingIntent pi = PendingIntent.getBroadcast(this, d.getDay(), i, 0);

                String firstOfNext = d.getEndOfMonth().plusDays(1).format("YYYY-MM-DD");
                DateTime dt = new DateTime(firstOfNext + " 06:00:00");

                AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, dt.getMilliseconds(TimeZone.getDefault()), pi);

                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putString("isReceiverSet", "yes");
                editor.apply();
            }
        }
    }

    public void rateSnackBar(View view) {

        if (mPrefs.getString("hasRated", "no").equals("no")) {
            final SharedPreferences.Editor editor = mPrefs.edit();
            int count = mPrefs.getInt("count", 0);
            if (count >= 3) {
                View.OnClickListener rateButton = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rateApp();
                    }
                };
                Snackbar sb = Snackbar.make(view, "Rate this app?", Snackbar.LENGTH_INDEFINITE)
                        .setAction("RATE", rateButton)
                        .setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                editor.putInt("count", 0).apply();
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary));
                sb.show();
            } else {
                count++;
                editor.putInt("count", count).apply();
            }
        }
    }

    public void rateApp() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("hasRated", "yes");
        editor.apply();
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


    @Override
    public void onItemSelected(String id, MovieObj movieObj) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(id, Parcels.wrap(movieObj));
        startActivity(intent);
    }

    public boolean hasConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isConnected();
        }
        return false;
    }
}
