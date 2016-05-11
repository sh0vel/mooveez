package com.app.shovonh.mooveez.data;

import android.provider.BaseColumns;

/**
 * Created by Shovon on 5/7/16.
 */
public class MovieContract {

    public static final class MovieEntry implements BaseColumns{
        public static final String TABLE_NAME = "this_month";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_MOVIE_ID = "movieid";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_BACKDROP = "backdrop";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_GENRES = "genres";
        public static final String COLUMN_TRAILER_1 = "trailer1";

    }


}
