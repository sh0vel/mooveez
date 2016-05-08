package com.app.shovonh.mooveez.data;

import android.provider.BaseColumns;

/**
 * Created by Shovon on 5/7/16.
 */
public class MovieDBContract {

    public static final class FavoritesEntry implements BaseColumns{
        public static final String TABLE_NAME = "popular";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TRAILER_1 = "trailer1";

    }


}
