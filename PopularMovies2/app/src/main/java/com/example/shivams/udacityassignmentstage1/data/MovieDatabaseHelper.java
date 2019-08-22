package com.example.shivams.udacityassignmentstage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public class MovieDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="userfavmovie.db";
    private static final int DATABASE_VERSION=6;

    public MovieDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
       final   String query = "CREATE TABLE "+ MovieDatabaseContract.MovieDatabaseEntry.TABLE_NAME+" ( "+ MovieDatabaseContract.MovieDatabaseEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + MovieDatabaseContract.MovieDatabaseEntry.MOVIE_ID+" TEXT NOT NULL ,"
                + MovieDatabaseContract.MovieDatabaseEntry.MOVIE_NAME+" TEXT NOT NULL ,"
                + MovieDatabaseContract.MovieDatabaseEntry.MOVIE_DESCRIPTION+" TEXT NOT NULL ,"
                + MovieDatabaseContract.MovieDatabaseEntry.MOVIE_RATINGS+" TEXT NOT NULL ,"
                + MovieDatabaseContract.MovieDatabaseEntry.POSTER_PATH+" TEXT NOT NULL ,"
               + MovieDatabaseContract.MovieDatabaseEntry.BACKDROP_PATH+" TEXT NOT NULL ,"
                + MovieDatabaseContract.MovieDatabaseEntry.RELEASE_DATE+" TEXT NOT NULL );";
         db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS "+ MovieDatabaseContract.MovieDatabaseEntry.TABLE_NAME;
        db.execSQL(query);
        onCreate(db);

    }
}
