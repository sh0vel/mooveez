//package com.app.shovonh.mooveez;
//
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.TimeZone;
//
//import hirondelle.date4j.DateTime;
//
///**
// * Created by Shovon on 5/11/16.
// */
//public class MoviesToDB extends AsyncTask<Void, Void, Void>{
//    public static boolean isDone = false;
//
//    private MovieObj[] getMovieDataFromJson(String _movieJsonStr)
//            throws JSONException {
//
//
//        final String TMD_RESULTS = "results";
//        final String TMD_POSTER = "poster_path";
//        final String TMD_DESCRIPTION = "overview";
//        final String TMD_RELEASE = "release_date";
//        final String TMD_TITLE = "title";
//        final String TMD_RATING = "vote_average";
//        final String TMD_BACKDROP = "backdrop_path";
//        final String TMD_GENRE_ARRAY = "genre_ids";
//        final String TMD_ID = "id";
//
//        JSONObject _moviesJson = new JSONObject(_movieJsonStr);
//        JSONArray _moviesArray = _moviesJson.getJSONArray(TMD_RESULTS);
//
//        MovieObj[] _movies = new MovieObj[_moviesArray.length()];
//        for (int i = 0; i < _moviesArray.length(); i++) {
//            String title, description, release, cover, backdrop;
//            int [] genres;
//            int movieid;
//
//
//            JSONObject _movieObject = _moviesArray.getJSONObject(i);
//            title = _movieObject.getString(TMD_TITLE);
//            description = _movieObject.getString(TMD_DESCRIPTION);
//            release = _movieObject.getString(TMD_RELEASE);
//            cover = _movieObject.getString(TMD_POSTER);
//            backdrop = _movieObject.getString(TMD_BACKDROP);
//            movieid = _movieObject.getInt(TMD_ID);
//
//            JSONArray genreArray = _movieObject.getJSONArray(TMD_GENRE_ARRAY);
//            int s = genreArray.length();
//            genres = new int [s];
//            for (int j = 0; j < s; j++){
//                genres[j] = genreArray.getInt(j);
//            }
//            //Log.v(LOG_TAG, Arrays.toString(genres));
//
//            MainActivity.dbHelper.insertData(title, cover, backdrop, release, description, genres, movieid);
//            MovieObj movie = new MovieObj(title, description, release, cover, backdrop, genres);
//            _movies[i] = movie;
//        }
//
//        return _movies;
//    }
//
//
//
//    ////////////////////
//    private String formatedDate(int o){
//        DateTime dt = DateTime.now(TimeZone.getDefault());
//        String start = dt.getStartOfMonth().format("YYYY-MM-DD");
//        String end = dt.getEndOfMonth().format("YYYY-MM-DD");
//        if (o==0)
//            return start;
//        return end;
//    }
//
//    ///////////////////////////
//    @Override
//    protected Void doInBackground(Void... m) {
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//
//        String _resultsJsonStr = null;
//        String format = "json";
//
//
//        try{
//            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
//            final String API_KEY = "api_key";
//            final String PAGE = "page";
//            final String RELEASE_DATE_GTE = "primary_release_date.gte";
//            final String RELEASE_DATE_LTE = "primary_release_date.lte";
//            final String SORT_BY = "sort_by";
//
//            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
//                    .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
//                    .appendQueryParameter(PAGE, "1")
//                    .appendQueryParameter(RELEASE_DATE_GTE, formatedDate(0))
//                    .appendQueryParameter(RELEASE_DATE_LTE, formatedDate(1))
//                    .appendQueryParameter(SORT_BY, "popularity.desc")
//                    .build();
//
//            URL url = new URL(builtUri.toString());
//            //Log.v(LOG_TAG, "Built URI :" + builtUri.toString());
//
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                _resultsJsonStr = null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null){
//                buffer.append((line + "\n"));
//            }
//
//            if(buffer.length() == 0){
//                _resultsJsonStr = null;
//            }
//
//            _resultsJsonStr = buffer.toString();
//
//            // Log.v(LOG_TAG, "Result JSON STR: " + _resultsJsonStr);
//
//        }catch (IOException e){
//            Log.e("MovieFragment", "Error ", e);
//            _resultsJsonStr = null;
//        }finally {
//            if(urlConnection != null)
//                urlConnection.disconnect();
//            if (reader != null){
//                try{
//                    reader.close();
//                }catch (final IOException e){
//                    Log.e("MoviesFragment", "Error closingstream", e);
//                }
//            }
//        }
//
//
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);
//        isDone = true;
//    }
//}
//
//
