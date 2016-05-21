package com.app.shovonh.mooveez;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.app.shovonh.mooveez.Objs.MovieObj;
import com.app.shovonh.mooveez.data.AlarmDBHelper;

import org.parceler.Parcels;

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

        FrameLayout card = (FrameLayout) findViewById(R.id.no_network_view);

        if (hasConnection(this)) {
            card.setVisibility(View.GONE);
            Fragment fragment = new ThisMonthFragment().newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.container_grid, fragment).commit();
        }else{
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(String id, MovieObj movieObj) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(id, Parcels.wrap(movieObj));
        startActivity(intent);
    }

    public boolean hasConnection(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null){
            Log.v(LOG_TAG, ""+networkInfo.isConnected());
            return  networkInfo.isConnected();
        }
        return false;
    }
}
