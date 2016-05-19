package com.app.shovonh.mooveez;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MovieDetailsFrag extends Fragment {
    public static final String LOG_TAG = MovieDetailsFrag.class.getSimpleName();

    private static final String MOVIE_DETAILS_ARRAY = "param1";

    // TODO: Rename and change types of parameters
    //array 0:poster 1:title 2:release 3:rating 4:description
    public static String movieDetails[];


    private static ImageView imgPoster, imgBackdrop;
    private static TextView tvTitle, tvRelease, tvRating, tvDescription, tvTrailer1, tvTrailer2, tvGenres;

    public static ArrayList<Trailer> trailers;
    public static ArrayAdapter arrayAdapter;

    public static LinearLayout linearList;

    public static LayoutInflater layoutInflater;

    public static Context context;

    public MovieDetailsFrag() {
        // Required empty public constructor
    }

    static void initializeView(View view) {

        tvTitle = (TextView) view.findViewById(R.id.movie_title);
        tvRelease = (TextView) view.findViewById(R.id.release);
        tvDescription = (TextView) view.findViewById(R.id.description);
        imgBackdrop = (ImageView) view.findViewById(R.id.backdrop);
        tvGenres = (TextView) view.findViewById(R.id.genres);
    }


    public static MovieDetailsFrag newInstance(String movieDetails[]) {

        MovieDetailsFrag fragment = new MovieDetailsFrag();
        Bundle args = new Bundle();
        args.putStringArray(MOVIE_DETAILS_ARRAY, movieDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieDetails = getArguments().getStringArray(MOVIE_DETAILS_ARRAY);
        }
        FetchTrailers fetchTrailers = new FetchTrailers();
        fetchTrailers.execute(movieDetails[6]);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(com.app.shovonh.mooveez.R.layout.fragment_movie_details, container, false);
        initializeView(view);
        if (!movieDetails[4].equals("http://image.tmdb.org/t/p/w500/null"))
            Picasso.with(getContext()).load(movieDetails[4]).into(imgBackdrop);
        else
            Picasso.with(getContext()).load(movieDetails[0]).into(imgBackdrop);

        tvTitle.setText(movieDetails[1]);
        tvRelease.setText(Utilities.dateFormatter(movieDetails[2]));
        tvDescription.setText(movieDetails[3]);
        tvGenres.setText(movieDetails[5]);

        trailers = new ArrayList<>();


        //listView.setAdapter(arrayAdapter);

        linearList = (LinearLayout) view.findViewById(R.id.add_list_items_here);
        layoutInflater = inflater;
        context = getContext();


        return view;
    }






    public static class FetchUSDate extends AsyncTask<String, Void, String> {
        public static final String LOG_TAG = FetchUSDate.class.getSimpleName();


        private String getReleaseDateFromJson(String jsonString, String defaultDate) throws JSONException {

            final String TMD_RESULTS = "results";
            final String TMD_COUNTRY = "iso_3166_1";
            final String TMD_RELEASE_ARRAY = "release_dates";
            final String TMD_RELEASE_DATE = "release_date";

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TMD_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject releaseObject = jsonArray.getJSONObject(i);
                if (releaseObject.getString(TMD_COUNTRY).equals("US")){
                    JSONArray releaseArray = releaseObject.getJSONArray(TMD_RELEASE_ARRAY);
                    JSONObject release = releaseArray.getJSONObject(releaseArray.length() - 1);
                    return release.getString(TMD_RELEASE_DATE).substring(0, 10);
                }

            }
            return defaultDate;
        }

        @Override
        protected String doInBackground(String... s) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String _resultsJsonStr = null;
            String format = "json";

            try {
                final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/" + s[0] + "/release_dates?";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI :" + builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    _resultsJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append((line + "\n"));
                }

                if (buffer.length() == 0) {
                    _resultsJsonStr = null;
                }

                _resultsJsonStr = buffer.toString();

                // Log.v(LOG_TAG, "Result JSON STR: " + _resultsJsonStr);

            } catch (IOException e) {
                Log.e("MovieFragment", "Error ", e);
                _resultsJsonStr = null;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("MoviesFragment", "Error closingstream", e);
                    }
                }
            }


            try {
                return getReleaseDateFromJson(_resultsJsonStr, s[1]);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvRelease.setText(Utilities.dateFormatter(s));
            MovieDetailActivity.bundle[2] = s;
            if (Utilities.alreadyReleased(s)) {
                MovieDetailActivity.fab.setVisibility(View.INVISIBLE);
            } else MovieDetailActivity.fab.setVisibility(View.VISIBLE);

        }
    }


        public static class FetchTrailers extends AsyncTask<String, Void, Trailer[]> {
        private final String LOG_TAG = FetchTrailers.class.getSimpleName();


        private Trailer[] getMovieDataFromJson(String trailerJsonStr)
                throws JSONException {


            final String TMD_RESULTS = "results";
            final String TMD_NAME = "name";
            final String TMD_LINK = "key";

            JSONObject trailersJson = new JSONObject(trailerJsonStr);
            JSONArray trailerArray = trailersJson.getJSONArray(TMD_RESULTS);

            Trailer [] trailers = new Trailer[trailerArray.length()];
            for (int i = 0; i < trailerArray.length(); i++) {
                String name, link;

                JSONObject trailerJsonObject = trailerArray.getJSONObject(i);

                name = trailerJsonObject.getString(TMD_NAME);
                link = trailerJsonObject.getString(TMD_LINK);

                trailers [i] = new Trailer(name, link);

                }
            return trailers;
        }



        @Override
        protected Trailer[] doInBackground(String... s) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String _resultsJsonStr = null;
            String format = "json";


            try {
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/" + s[0] + "/videos?";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI :" + builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    _resultsJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append((line + "\n"));
                }

                if (buffer.length() == 0) {
                    _resultsJsonStr = null;
                }

                _resultsJsonStr = buffer.toString();

                // Log.v(LOG_TAG, "Result JSON STR: " + _resultsJsonStr);

            } catch (IOException e) {
                Log.e("MovieFragment", "Error ", e);
                _resultsJsonStr = null;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("MoviesFragment", "Error closingstream", e);
                    }
                }
            }


            try {
                return getMovieDataFromJson(_resultsJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Trailer[] trailerArray) {

            super.onPostExecute(trailerArray);
            FetchUSDate fetchUSDate = new FetchUSDate();
            //fetchUSDate.execute(movieDetails[6], movieDetails[2]);

            for (Trailer t : trailerArray) {
                trailers.add(t);
            }
            for (int i = 0; i < trailers.size(); i++) {
                View v = layoutInflater.inflate(R.layout.trailers_list_item, null);
                //TextView tv = (TextView) v.findViewById(R.id.trailer_list_item_text);
                Button b = (Button) v.findViewById(R.id.trailer_list_item_text);
                Trailer tr = trailers.get(i);
                final String link = tr.link;
                //tv.setText(tr.name);
                b.setText(tr.name);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        context.startActivity(browserIntent);
                    }
                });

                linearList.addView(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
    }
}
