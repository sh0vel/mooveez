package com.app.shovonh.mooveez.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.app.shovonh.mooveez.MovieObj;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Shovon on 5/11/16.
 */
public class TestDB extends AndroidTestCase {
    public static final String LOG_TAG = TestDB.class.getSimpleName();

    void deleteTheDatabase(){
        mContext.deleteDatabase(AlarmDBHelper.DATABASE_NAME);
    }

    public void setUp(){
        deleteTheDatabase();
    }

    public void testCreateDB() throws Throwable{
        final HashSet<String> tableNameHash = new HashSet<String>();
        tableNameHash.add(notificationContract.MovieEntry.TABLE_NAME);

        mContext.deleteDatabase(AlarmDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new AlarmDBHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: Databse has not been created corectly", c.moveToFirst());

        c = db.rawQuery("PRAGMA table_info(" + notificationContract.MovieEntry.TABLE_NAME + ")", null);
        assertTrue("Error: Unable to query the database for table information", c.moveToFirst());

        final HashSet<String> columnHashSet = new HashSet<String>();
        columnHashSet.add(notificationContract.MovieEntry._ID);
        columnHashSet.add(notificationContract.MovieEntry.COLUMN_RELEASE_DATE);
        columnHashSet.add(notificationContract.MovieEntry.COLUMN_MOVIE_ID);

        int columnNameIndex = c.getColumnIndex("name");
        do{
            String columnName = c.getString(columnNameIndex);
            Log.v(LOG_TAG, columnName);
            columnHashSet.remove(columnName);
        }while(c.moveToNext());

        assertTrue("Error: The database doesnt contain all columns.", columnHashSet.isEmpty());

        db.close();

    }

    public void testMovieTable(){
        AlarmDBHelper dbHelper = new AlarmDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        ContentValues testValues = new ContentValues();
        testValues.put(notificationContract.MovieEntry.COLUMN_MOVIE_NAME, "movienameyo");
        testValues.put(notificationContract.MovieEntry.COLUMN_RELEASE_DATE, "assadd");
        testValues.put(notificationContract.MovieEntry.COLUMN_MOVIE_ID, 54);

        long locationRowID = db.insert(notificationContract.MovieEntry.TABLE_NAME, null, testValues);
        assertTrue(locationRowID != -1);


        Cursor cursor = db.query(
                notificationContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertTrue("Error: Not records returned from query", cursor.moveToFirst());

        validateCurrentRecord("Error: Validation failed", cursor, testValues);

        assertFalse( "Error: More than one record returned from location query",
                cursor.moveToNext() );
        cursor.close();
        db.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

   public void testTable(){
        int a [] = {1,2,3};
        AlarmDBHelper dbHelper = new AlarmDBHelper(mContext);
        long t = dbHelper.insertData("title", "1121-12-13", 1231);
        assertTrue("err",t != -1);
    }

    public void testInsert(){
        AlarmDBHelper dbHelper = new AlarmDBHelper(mContext);

        dbHelper.insertData("X-Men", "tomorrow", 3);
        dbHelper.insertData("Avengers", "LastMonth", 4);
        ArrayList<MovieObj> objs = dbHelper.getAllMovies();
        for (MovieObj m : objs){
        }

    }

}
