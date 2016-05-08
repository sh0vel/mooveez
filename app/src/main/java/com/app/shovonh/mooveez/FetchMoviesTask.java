package com.app.shovonh.mooveez;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by Shovon on 5/7/16.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, MovieObj[]> {
    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    public static ArrayList<MovieObj> _popularMovies = new ArrayList<>();
    public static ArrayList<MovieObj> _topRatedMovies = new ArrayList<>();
    public static ArrayList<MovieObj> _newMovies = new ArrayList<>();


    //passed to AsyncTask to retrieve sertain movies
    public static String FETCH_TOP_RATED = "top_rated";
    public static String FETCH_MOST_POPULAR = "popular";
    public static String FETCH_NOW_PLAYING = "now_playing";

    private static String mFetching = "";


    private MovieObj[] getMovieDataFromJson(String _movieJsonStr)
            throws JSONException {

        final String TMD_RESULTS = "results";
        final String TMD_POSTER = "poster_path";
        final String TMD_DESCRIPTION = "overview";
        final String TMD_RELEASE = "release_date";
        final String TMD_TITLE = "title";
        final String TMD_RATING = "vote_average";

        JSONObject _moviesJson = new JSONObject(_movieJsonStr);
        JSONArray _moviesArray = _moviesJson.getJSONArray(TMD_RESULTS);

        MovieObj[] _movies = new MovieObj[_moviesArray.length()];
        for (int i = 0; i < _moviesArray.length(); i++){
            String title, description, release, rating, cover;

            JSONObject _movieObject = _moviesArray.getJSONObject(i);
            title = _movieObject.getString(TMD_TITLE);
            description = _movieObject.getString(TMD_DESCRIPTION);
            release = _movieObject.getString(TMD_RELEASE);
            rating = Integer.toString(_movieObject.getInt(TMD_RATING));
            cover = _movieObject.getString(TMD_POSTER);

            MovieObj movie = new MovieObj(title, description, release, rating, cover);
            _movies[i] = movie;
        }

        //  for (MovieObj m : _movies)
        //     Log.v(LOG_TAG, "Poster: " + m.cover);


        return _movies;
    }


    @Override
    protected MovieObj[] doInBackground(String... strings) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String _resultsJsonStr = null;
        String format = "json";
        mFetching = strings[0];

        Log.v(LOG_TAG,"Fetching " + mFetching + " movies from db.");

        try{
            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String BEGIN_PARAMS = "?";
            final String API_KEY = "api_key";
            final String PAGE = "page";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL + strings[0] + BEGIN_PARAMS).buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .appendQueryParameter(PAGE, "1")
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
            while ((line = reader.readLine()) != null){
                buffer.append((line + "\n"));
            }

            if(buffer.length() == 0){
                _resultsJsonStr = null;
            }

            _resultsJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Result JSON STR: " + _resultsJsonStr);

        }catch (IOException e){
            Log.e("MovieFragment", "Error ", e);
            _resultsJsonStr = null;
        }finally {
            if(urlConnection != null)
                urlConnection.disconnect();
            if (reader != null){
                try{
                    reader.close();
                }catch (final IOException e){
                    Log.e("PopularMoviesFragment", "Error closingstream", e);
                }
            }
        }

        try {
            return getMovieDataFromJson(_resultsJsonStr);
        }catch (JSONException e){
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(MovieObj[] movieObjs) {
        super.onPostExecute(movieObjs);
        Log.v(LOG_TAG, "Done fetching " + mFetching);
        if (mFetching.equals(FETCH_MOST_POPULAR)) {
            Log.v(LOG_TAG, "Added Most Popular to list");
            for (MovieObj m : movieObjs)

            _popularMovies.add(m);
        }else if (mFetching.equals(FETCH_TOP_RATED)){
            Log.v(LOG_TAG, "Added top rated to list");

            for (MovieObj m : movieObjs)
                _topRatedMovies.add(m);
        }else if (mFetching.equals(FETCH_NOW_PLAYING)){
            Log.v(LOG_TAG, "Added new to list");
            for (MovieObj m : movieObjs)
                _popularMovies.add(m);
        }
    }
}