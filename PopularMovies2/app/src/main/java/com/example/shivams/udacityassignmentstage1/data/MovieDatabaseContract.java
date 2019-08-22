package com.example.shivams.udacityassignmentstage1.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieDatabaseContract {

        public static final String AUTHORITY ="com.example.shivams.udacityassignmentstage1";

        public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+AUTHORITY);

    public static final class MovieDatabaseEntry implements BaseColumns{
        public static final String TABLE_NAME="user_fav_movie";
        public static final Uri mDbUri = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();


        public static final String MOVIE_ID="movie_id";
        public static final String MOVIE_NAME="movie_name";
        public static final String MOVIE_RATINGS="movie_ratings";
        public static final String MOVIE_DESCRIPTION="movie_description";
        public static final String POSTER_PATH="poster_path";
        public static final String RELEASE_DATE="release_date";
        public static final String BACKDROP_PATH="backdrop_path";
    }
}
