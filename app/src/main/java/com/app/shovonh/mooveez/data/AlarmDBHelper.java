package com.app.shovonh.mooveez.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.shovonh.mooveez.MovieObj;
import com.app.shovonh.mooveez.data.notificationContract.MovieEntry;

import java.util.ArrayList;

/**
 * Created by Shovon on 5/11/16.
 */
public class AlarmDBHelper extends SQLiteOpenHelper{
    public static final String LOG_TAG = AlarmDBHelper.class.getSimpleName();

    public static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "movies.db";

    public AlarmDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MovieEntry.TABLE_NAME + " ("+
                MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }



    public long insertData(String title, String releaseDate, int movieid){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_MOVIE_NAME, title);
        values.put(notificationContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        values.put(notificationContract.MovieEntry.COLUMN_MOVIE_ID, movieid);

        long id = db.insert(notificationContract.MovieEntry.TABLE_NAME, null, values);
       return id;
    }


    public ArrayList<MovieObj> getAllMovies(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + MovieEntry.TABLE_NAME, null);
        ArrayList<MovieObj> objs = new ArrayList<>();
        if (cursor.moveToFirst()){
            int movieName = cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_NAME);
            int movieID = cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID);
            int releaseDate = cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE);
            do{
                Log.v(LOG_TAG, "Do loop entered");
                MovieObj obj = new MovieObj(
                        cursor.getString(movieName), cursor.getString(releaseDate), cursor.getInt(movieID));
                objs.add(obj);
            }while (cursor.moveToNext());
            Log.v(LOG_TAG, "Cursor size: " + cursor.getCount() + " Array Size: " + objs.size());
        }
        cursor.close();
        return objs;
    }

    public void deleteEntry(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MovieEntry.TABLE_NAME, MovieEntry.COLUMN_MOVIE_ID + " = ?",new String[]{String.valueOf(id)});
        db.close();
        Log.v(LOG_TAG, "Deleted " + id);
        ArrayList<MovieObj> objs = getAllMovies();
//        for (MovieObj m : objs){
//
//            Log.v(LOG_TAG, m.getTitle() + ", " + m.getReleaseDate() + ", " + m.getId());
//            //dbHelper.deleteEntry(m.id);
//        }
    }


}
