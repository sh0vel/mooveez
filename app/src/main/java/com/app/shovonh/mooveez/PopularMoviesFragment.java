package com.app.shovonh.mooveez;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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


public class PopularMoviesFragment extends Fragment {
    public ImageAdapter _gridAdapter;
    public ArrayList<MovieObj> _moviesList;
    public static String MOVIE_DETAILS_BUNDLE_ID = "movedetails";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute();

    }


    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String id, String [] movieDetails);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popular_movies, container, false);

        _moviesList = new ArrayList<>();

        _gridAdapter = new ImageAdapter(getActivity(), _moviesList);
        GridView gridView = (GridView) rootView.findViewById(com.app.shovonh.mooveez.R.id.gridView);
        gridView.setAdapter(_gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MovieObj m = _moviesList.get(i);
                String [] movieDetails = {m.cover, m.title, m.releaseDate, m.rating, m.description};
                ((Callback) getActivity()).onItemSelected(MOVIE_DETAILS_BUNDLE_ID, movieDetails);
            }
        });


        return rootView;
    }
    public class FetchMoviesTask extends AsyncTask<MovieObj, Void, MovieObj[]> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        private MovieObj[] getMovieDataFromJson(String _movieJsonStr)
                throws JSONException{

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
        protected MovieObj[] doInBackground(MovieObj... m) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String _resultsJsonStr = null;
            String format = "json";


            try{
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/popular?";
                final String API_KEY = "api_key";
                final String PAGE = "page";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
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
                        Log.e("MoviesFragment", "Error closingstream", e);
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
            for (MovieObj m : movieObjs)
                _moviesList.add(m);
            _gridAdapter.setData(_moviesList);

        }
    }
}





