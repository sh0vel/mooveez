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

import com.app.shovonh.mooveez.Objs.Cast;
import com.app.shovonh.mooveez.Objs.MovieObj;
import com.app.shovonh.mooveez.Objs.Trailer;

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
    public static ArrayList<MovieObj> tempList;
    public static ArrayList<MovieObj> thisMonthsMovieList;
    public static String SELECTED_MOVIE_BUNDLE_ID = "movie";

    public ThisMonthFragment() {
        // Required empty public constructor
    }

    public ThisMonthFragment newInstance() {
        ThisMonthFragment fragment = new ThisMonthFragment();
        return fragment;
    }

    public interface Callback {
        public void onItemSelected(String id, MovieObj movieObj);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "Created");
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

                ((Callback) getActivity()).onItemSelected(SELECTED_MOVIE_BUNDLE_ID, thisMonthsMovieList.get(i));
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
            final String TMD_ID = "id";
            final String TMD_BACKDROP = "backdrop_path";
            final String TMD_GENRE_ARRAY = "genre_ids";
            final String TMD_ORIGINAL_LANGUAGE = "original_language";

            JSONObject _moviesJson = new JSONObject(_movieJsonStr);
            JSONArray _moviesArray = _moviesJson.getJSONArray(TMD_RESULTS);

            ArrayList<MovieObj> _movies = new ArrayList<>();
            for (int i = 0; i < _moviesArray.length(); i++) {
                String title, description, release, cover, backdrop, lang;
                int[] genres;
                int id;


                JSONObject _movieObject = _moviesArray.getJSONObject(i);
                cover = _movieObject.getString(TMD_POSTER);
                backdrop = _movieObject.getString(TMD_BACKDROP);
                lang = _movieObject.getString(TMD_ORIGINAL_LANGUAGE);
                if (!cover.equals("null") && !backdrop.equals("null")) {
                    // URL url = new URL()


                    title = _movieObject.getString(TMD_TITLE);
                    description = _movieObject.getString(TMD_DESCRIPTION);
                    release = _movieObject.getString(TMD_RELEASE);
                    id = _movieObject.getInt(TMD_ID);
                    JSONArray genreArray = _movieObject.getJSONArray(TMD_GENRE_ARRAY);
                    int s = genreArray.length();
                    genres = new int[s];
                    for (int j = 0; j < s; j++) {
                        genres[j] = genreArray.getInt(j);
                    }

                    MovieObj movie = new MovieObj(title, description, release, cover, backdrop, genres, id);
                    _movies.add(movie);
                }

            }


            MovieObj[] array = new MovieObj[_movies.size()];
            for (int i = 0; i < _movies.size(); i++) {
                array[i] = _movies.get(i);
            }

            return array;
        }


        ////////////////////
        private String formatedDate(int o) {
            DateTime dt = DateTime.now(TimeZone.getDefault());
            String start = dt.getStartOfMonth().format("YYYY-MM-DD");
            String end = dt.getEndOfMonth().format("YYYY-MM-DD");
            if (o == 0)
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


            try {
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String API_KEY = "api_key";
                final String PAGE = "page";
                final String RELEASE_DATE_GTE = "primary_release_date.gte";
                final String RELEASE_DATE_LTE = "primary_release_date.lte";
                final String CERTIFICATION_COUNTRY = "certification_country";
                final String CERTIFICATION_LTE = "certification.lte";
                final String INCLUDE_ADULT = "include_adult";
                final String VOTE_COUNT = "vote_count.gte";
                final String SORT_BY = "sort_by";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .appendQueryParameter(PAGE, "1")
                        .appendQueryParameter(RELEASE_DATE_GTE, formatedDate(0))
                        .appendQueryParameter(RELEASE_DATE_LTE, formatedDate(1))
                        .appendQueryParameter(CERTIFICATION_COUNTRY, "US")
                        .appendQueryParameter(CERTIFICATION_LTE, "NC-17")
                        .appendQueryParameter(SORT_BY, "popularity.desc")
                        //.appendQueryParameter(VOTE_COUNT, "1")
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
        protected void onPostExecute(MovieObj[] movieObjs) {
            super.onPostExecute(movieObjs);
            tempList = new ArrayList<>();
            for (MovieObj m : movieObjs) {
                tempList.add(m);
                //adapter.setData(thisMonthsMovieList);
            }

            Log.v(LOG_TAG, " tempList size: " + tempList.size());

            for (int i = 0; i < tempList.size(); i++) {
                FetchMovieAtributes fetchUUSDate = new FetchMovieAtributes();
                fetchUUSDate.execute(tempList.get(i));
            }
        }
    }


    public static class FetchMovieAtributes extends AsyncTask<MovieObj, Void, MovieObj> {
        public static final String LOG_TAG = FetchMovieAtributes.class.getSimpleName();


        private MovieObj getAttributesFromJson(String jsonString, MovieObj movieObj) throws JSONException {

            final String RELEASE_OBJECT = "release_dates";
            final String RELEASE_RESULTS = "results";
            final String RELEASE_COUNTRY = "iso_3166_1";
            final String RELEASE_ARRAY = "release_dates";
            final String RELEASE_DATE = "release_date";

            final String TRAILER_OBJECT = "videos";
            final String TRAILER_RESULTS = "results";
            final String TRAILER_NAME = "name";
            final String TRAILER_LINK = "key";

            final String CAST_OBJECT = "credits";
            final String CAST_RESULTS = "cast";
            final String CAST_ID = "id";
            final String CAST_NAME = "name";
            final String CAST_CHARACTER = "character";
            final String CAST_IMG = "profile_path";

            JSONObject jsonObject = new JSONObject(jsonString);

            JSONObject releaseJsonObject = jsonObject.getJSONObject(RELEASE_OBJECT);
            JSONArray releaseJsonArray = releaseJsonObject.getJSONArray(RELEASE_RESULTS);
            for (int i = 0; i < releaseJsonArray.length(); i++) {
                JSONObject releaseObject = releaseJsonArray.getJSONObject(i);
                if (releaseObject.getString(RELEASE_COUNTRY).equals("US")) {
                    JSONArray releaseArray = releaseObject.getJSONArray(RELEASE_ARRAY);
                    JSONObject release = releaseArray.getJSONObject(releaseArray.length() - 1);
                    String newDate = release.getString(RELEASE_DATE).substring(0, 10);
                    movieObj.setReleaseDate(newDate);
                    break;
                }
            }

            JSONObject trailersJsonObject = jsonObject.getJSONObject(TRAILER_OBJECT);
            JSONArray trailersJsonArray = trailersJsonObject.getJSONArray(TRAILER_RESULTS);
            Trailer [] trailers = new Trailer[trailersJsonArray.length()];
            for (int i = 0; i < trailers.length; i++){
                String name, link;
                JSONObject trailerJsonObject = trailersJsonArray.getJSONObject(i);
                name = trailerJsonObject.getString(TRAILER_NAME);
                link = trailerJsonObject.getString(TRAILER_LINK);
                trailers [i] = new Trailer(name, link);
            }
            movieObj.setTrailers(trailers);

            JSONObject creditsJsonObject = jsonObject.getJSONObject(CAST_OBJECT);
            JSONArray castJsonArray = creditsJsonObject.getJSONArray(CAST_RESULTS);
            Cast [] castMems = new Cast[castJsonArray.length()];
            for(int i = 0; i < castMems.length; i++){
                int id;
                String name, character, img;
                JSONObject castMemberObject = castJsonArray.getJSONObject(i);
                id = castMemberObject.getInt(CAST_ID);
                name = castMemberObject.getString(CAST_NAME);
                character = castMemberObject.getString(CAST_CHARACTER);
                img = castMemberObject.getString(CAST_IMG);
                Cast newcast = new Cast(id, name, character, img);
                castMems[i] = newcast;
            }
            movieObj.setCastMembers(castMems);




            return movieObj;
        }

        @Override
        protected MovieObj doInBackground(MovieObj... m) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String _resultsJsonStr = null;
            String format = "json";

            try {
                final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/" + m[0].getId() + "?";
                final String API_KEY = "api_key";
                final String APPEND = "append_to_response";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .appendQueryParameter(APPEND, "release_dates,videos,credits")
                        .build();

                URL url = new URL(builtUri.toString());

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
                return getAttributesFromJson(_resultsJsonStr, m[0]);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieObj movieObj) {
            super.onPostExecute(movieObj);
            if (Utilities.isThisMonth(movieObj.getReleaseDate())) {
                thisMonthsMovieList.add(movieObj);
                adapter.setData(thisMonthsMovieList);
            }
        }
    }

}
