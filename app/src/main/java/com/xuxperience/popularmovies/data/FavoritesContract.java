package com.xuxperience.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by badeyemi on 6/9/17.
 */

public class FavoritesContract {
    public static final String AUTHORITY = "com.xuxperience.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoritesEntry implements BaseColumns {
        // Favorites table and column names
        public static final String TABLE_NAME = "favorites";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                                                                .appendPath(PATH_FAVORITES)
                                                                .build();

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_MOVIE_POSTER_THUMBNAIL = "thumbnail";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_USER_RATING = "user_rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
    }
}
