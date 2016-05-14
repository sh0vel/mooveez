package com.app.shovonh.mooveez.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.app.shovonh.mooveez.data.MovieContract.MovieEntry;

import java.util.Arrays;

/**
 * Created by Shovon on 5/11/16.
 */
public class MovieDBHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public MovieDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MovieEntry.TABLE_NAME + " ("+
                MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_GENRES + " TEXT, " +
                MovieEntry.COLUMN_DESCRIPTION + " TEXT, " +
                MovieEntry.COLUMN_POSTER + " TEXT, " +
                MovieEntry.COLUMN_BACKDROP + " TEXT, " +
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public long insertData(String title, String poster, String backdrop, String releaseDate,
                              String description, int [] genres, int movieid){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
        testValues.put(MovieContract.MovieEntry.COLUMN_POSTER, poster);
        testValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP, backdrop);
        testValues.put(MovieContract.MovieEntry.COLUMN_GENRES, Arrays.toString(genres));
        testValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        testValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION, description);
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieid);

       return db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);
    }
    public Cursor getMoviesFromDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }
}
