package com.example.shivams.udacityassignmentstage1.sync;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.shivams.udacityassignmentstage1.data.MovieDatabaseContract;

public class BackgroundTasks extends IntentService {
    public BackgroundTasks(){
        super("BackgroundTasks");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle dataSet = intent.getExtras();
        String movieId =dataSet.getString("movie_id");
        String movieTitle = dataSet.getString("movie_title");
        String movieDescription = dataSet.getString("movie_description");
        String movieReleaseDate = dataSet.getString("movie_release_date");
        String movieRatings = dataSet.getString("movie_rating");
        String moviePosterPath = dataSet.getString("movie_poster_path");
        String movieBackdropPath = dataSet.getString("movie_backdrop_path");

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDatabaseContract.MovieDatabaseEntry.MOVIE_ID,movieId);
        contentValues.put(MovieDatabaseContract.MovieDatabaseEntry.MOVIE_NAME,movieTitle);
        contentValues.put(MovieDatabaseContract.MovieDatabaseEntry.MOVIE_DESCRIPTION,movieDescription);
        contentValues.put(MovieDatabaseContract.MovieDatabaseEntry.MOVIE_RATINGS,movieRatings);
        contentValues.put(MovieDatabaseContract.MovieDatabaseEntry.RELEASE_DATE,movieReleaseDate);
        contentValues.put(MovieDatabaseContract.MovieDatabaseEntry.POSTER_PATH,moviePosterPath);
        contentValues.put(MovieDatabaseContract.MovieDatabaseEntry.BACKDROP_PATH,movieBackdropPath);
        enterDataInSQLITE(contentValues);



    }

    private void deleteDataInSQLITE(String movieId){

    }
    private void enterDataInSQLITE(ContentValues contentValues){
      Uri insertedUri =   getContentResolver().insert(MovieDatabaseContract.MovieDatabaseEntry.mDbUri,contentValues);
       // Toast.makeText(this, insertedUri+"", Toast.LENGTH_SHORT).show();
        Log.v("INSERTEDURI",insertedUri+toString());

    }

}
