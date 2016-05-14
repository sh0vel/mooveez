package com.app.shovonh.mooveez.data;

import android.test.AndroidTestCase;

/**
 * Created by Shovon on 5/11/16.
 */
public class TestDB extends AndroidTestCase {
    public static final String LOG_TAG = TestDB.class.getSimpleName();

    void deleteTheDatabase(){
        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);
    }

    public void setUp(){
        deleteTheDatabase();
    }

//    public void testCreateDB() throws Throwable{
//        final HashSet<String> tableNameHash = new HashSet<String>();
//        tableNameHash.add(MovieContract.MovieEntry.TABLE_NAME);
//
//        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);
//        SQLiteDatabase db = new MovieDBHelper(this.mContext).getWritableDatabase();
//        assertEquals(true, db.isOpen());
//
//        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
//        assertTrue("Error: Databse has not been created corectly", c.moveToFirst());
//
//        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")", null);
//        assertTrue("Error: Unable to query the database for table information", c.moveToFirst());
//
//        final HashSet<String> columnHashSet = new HashSet<String>();
//        columnHashSet.add(MovieContract.MovieEntry._ID);
//        columnHashSet.add(MovieContract.MovieEntry.COLUMN_TITLE);
//        columnHashSet.add(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
//        columnHashSet.add(MovieContract.MovieEntry.COLUMN_GENRES);
//        columnHashSet.add(MovieContract.MovieEntry.COLUMN_BACKDROP);
//        columnHashSet.add(MovieContract.MovieEntry.COLUMN_DESCRIPTION);
//        columnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
//        columnHashSet.add(MovieContract.MovieEntry.COLUMN_POSTER);
//
//        int columnNameIndex = c.getColumnIndex("name");
//        do{
//            String columnName = c.getString(columnNameIndex);
//            Log.v(LOG_TAG, columnName);
//            columnHashSet.remove(columnName);
//        }while(c.moveToNext());
//
//        assertTrue("Error: The database doesnt contain all columns.", columnHashSet.isEmpty());
//
//        db.close();
//
//    }
//
//    public void testMovieTable(){
//        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//
//        ContentValues testValues = new ContentValues();
//        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "titleee");
//        testValues.put(MovieContract.MovieEntry.COLUMN_POSTER, "asd");
//        testValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP, "asd");
//        testValues.put(MovieContract.MovieEntry.COLUMN_GENRES, "asdwq");
//        testValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "assadd");
//        testValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION, "asdesd");
//        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 54);
//
//        long locationRowID = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);
//        assertTrue(locationRowID != -1);
//
//
//        Cursor cursor = db.query(
//                MovieContract.MovieEntry.TABLE_NAME,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null
//        );
//
//        assertTrue("Error: Not records returned from query", cursor.moveToFirst());
//
//        validateCurrentRecord("Error: Validation failed", cursor, testValues);
//
//        assertFalse( "Error: More than one record returned from location query",
//                cursor.moveToNext() );
//        cursor.close();
//        db.close();
//    }
//
//    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
//        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
//        for (Map.Entry<String, Object> entry : valueSet) {
//            String columnName = entry.getKey();
//            int idx = valueCursor.getColumnIndex(columnName);
//            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
//            String expectedValue = entry.getValue().toString();
//            assertEquals("Value '" + entry.getValue().toString() +
//                    "' did not match the expected value '" +
//                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
//        }
//    }
//
   public void testTable(){
        int a [] = {1,2,3};
        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        long t = dbHelper.insertData("12", "fasd", "ger", "hrtfg", "fhg",a, 4);
        assertTrue("err",t != -1);
    }
}
