package com.app.shovonh.mooveez.data;

import android.provider.BaseColumns;

/**
 * Created by Shovon on 5/7/16.
 */
public class notificationContract {

    public static final class MovieEntry implements BaseColumns{
        public static final String TABLE_NAME = "notifications";
        public static final String COLUMN_MOVIE_NAME = "movie_name";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_ID = "movie_id";

    }


}
