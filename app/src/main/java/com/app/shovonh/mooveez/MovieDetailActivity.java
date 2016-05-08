package com.app.shovonh.mooveez;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailsFrag.OnFragmentInteractionListener {
    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.app.shovonh.mooveez.R.layout.activity_movie_detail);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null){
            //Log.v(LOG_TAG, Arrays.deepToString(getIntent().getStringArrayExtra(PopularMoviesFragment.MOVIE_DETAILS_BUNDLE_ID)));
            Fragment fragment =
                    new MovieDetailsFrag().newInstance(getIntent().getStringArrayExtra(PopularMoviesFragment.MOVIE_DETAILS_BUNDLE_ID));
            getSupportFragmentManager().beginTransaction().add(R.id.movie_details_activity, fragment).commit();

        }

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
