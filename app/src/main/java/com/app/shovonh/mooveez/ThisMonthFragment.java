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
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThisMonthFragment extends Fragment {
    private static final String LOG_TAG = ThisMonthFragment.class.getSimpleName();

    public static ImageAdapter adapter;
    public static ArrayList<MovieObj> thisMonthsMovieList;
    public static String MOVIE_DETAILS_BUNDLE_ID = "movedetails";

    public ThisMonthFragment() {
        // Required empty public constructor
    }

    public ThisMonthFragment newInstance(int page_number){
        ThisMonthFragment fragment = new ThisMonthFragment();
        Bundle args = new Bundle();
       // args.putInt(PAGE_NUM, page_number);
        fragment.setArguments(args);
        return fragment;
    }

    public interface Callback{
        public void onItemSelected(String id, String [] movieDetails);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FetchThisMonthsMovies task = new FetchThisMonthsMovies();
        task.execute();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_this_month, container, false);

            thisMonthsMovieList = new ArrayList<>();

            adapter = new ImageAdapter(getActivity(), thisMonthsMovieList);
            GridView gridView = (GridView) view.findViewById(R.id.thisMonthGrid);
            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    MovieObj m = thisMonthsMovieList.get(i);
                    String [] movieDetails = {m.cover, m.title, m.releaseDate, m.description, m.backdrop};
                    ((Callback) getActivity()).onItemSelected(MOVIE_DETAILS_BUNDLE_ID, movieDetails);
                }
            });

        return view;
    }



////////////////////////
    public static class FetchThisMonthsMovies extends AsyncTask<MovieObj, Void, MovieObj[]> {
        private final String LOG_TAG = FetchThisMonthsMovies.class.getSimpleName();


        private MovieObj[] getMovieDataFromJson(String _movieJsonStr)
                throws JSONException {


            final String TMD_RESULTS = "results";
            final String TMD_POSTER = "poster_path";
            final String TMD_DESCRIPTION = "overview";
            final String TMD_RELEASE = "release_date";
            final String TMD_TITLE = "title";
            final String TMD_RATING = "vote_average";
            final String TMD_BACKDROP = "backdrop_path";

            JSONObject _moviesJson = new JSONObject(_movieJsonStr);
            JSONArray _moviesArray = _moviesJson.getJSONArray(TMD_RESULTS);

            MovieObj[] _movies = new MovieObj[_moviesArray.length()];
            for (int i = 0; i < _moviesArray.length(); i++) {
                String title, description, release, cover, backdrop;

                JSONObject _movieObject = _moviesArray.getJSONObject(i);
                title = _movieObject.getString(TMD_TITLE);
                description = _movieObject.getString(TMD_DESCRIPTION);
                release = _movieObject.getString(TMD_RELEASE);
                cover = _movieObject.getString(TMD_POSTER);
                backdrop = _movieObject.getString(TMD_BACKDROP);


                MovieObj movie = new MovieObj(title, description, release, cover, backdrop);
                _movies[i] = movie;
            }

            return _movies;
        }



////////////////////
        private String formatedDate(int o){
            DateTime dt = DateTime.now(TimeZone.getDefault());
            String start = dt.getStartOfMonth().format("YYYY-MM-DD");
            String end = dt.getEndOfMonth().format("YYYY-MM-DD");
            if (o==0)
                return start;
            return end;
        }

///////////////////////////
        @Override
        protected MovieObj[] doInBackground(MovieObj... m) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String _resultsJsonStr = null;
            String format = "json";


            try{
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String API_KEY = "api_key";
                final String PAGE = "page";
                final String RELEASE_DATE_GTE = "primary_release_date.gte";
                final String RELEASE_DATE_LTE = "primary_release_date.lte";
                final String SORT_BY = "sort_by";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .appendQueryParameter(PAGE, "1")
                        .appendQueryParameter(RELEASE_DATE_GTE, formatedDate(0))
                        .appendQueryParameter(RELEASE_DATE_LTE, formatedDate(1))
                        .appendQueryParameter(SORT_BY, "popularity.desc")
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

                // Log.v(LOG_TAG, "Result JSON STR: " + _resultsJsonStr);

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
            for (MovieObj m : movieObjs) {
                thisMonthsMovieList.add(m);
                adapter.setData(thisMonthsMovieList);
            }
        }
    }


}
