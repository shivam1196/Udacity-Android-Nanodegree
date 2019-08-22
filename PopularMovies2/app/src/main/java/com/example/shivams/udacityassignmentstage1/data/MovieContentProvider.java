package com.example.shivams.udacityassignmentstage1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieContentProvider extends ContentProvider {
    private MovieDatabaseHelper movieDatabaseHelper;
    private static final int MOVIE_TASK_ID =100;
    private static final int MOVIE_TASK_ITEM_ID= 102;
    private SQLiteDatabase mSQLite;

    public static UriMatcher sUriMatcher=buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieDatabaseContract.AUTHORITY, MovieDatabaseContract.MovieDatabaseEntry.TABLE_NAME,MOVIE_TASK_ID);
        uriMatcher.addURI(MovieDatabaseContract.AUTHORITY, MovieDatabaseContract.MovieDatabaseEntry.TABLE_NAME+"/#",MOVIE_TASK_ITEM_ID);
        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        movieDatabaseHelper = new MovieDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        mSQLite = movieDatabaseHelper.getReadableDatabase();
        Cursor cursor;
        int uriId = sUriMatcher.match(uri);
        switch (uriId){
            case MOVIE_TASK_ID:
                cursor = mSQLite.query(MovieDatabaseContract.MovieDatabaseEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
                break;

            case  MOVIE_TASK_ITEM_ID:
                String selectionId = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String [] mSelectionArgs = new String []{selectionId};
                cursor = mSQLite.query(MovieDatabaseContract.MovieDatabaseEntry.TABLE_NAME,projection,mSelection,mSelectionArgs,null,null,sortOrder);
                break;
                default:
                    throw  new UnsupportedOperationException("Unable to perform the query operation");
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        mSQLite = movieDatabaseHelper.getWritableDatabase();
        Uri returnUri;
        int matchTaskId = sUriMatcher.match(uri);
        switch (matchTaskId){
            case MOVIE_TASK_ID:
              long successId=  mSQLite.insert(MovieDatabaseContract.MovieDatabaseEntry.TABLE_NAME,null,values);
              if(successId >0){
                  returnUri = ContentUris.withAppendedId(MovieDatabaseContract.MovieDatabaseEntry.mDbUri,successId);
              }
              else {
                  throw new android.database.SQLException("Failed to perform the operation");
              }

                break;
                default:
                    throw new UnsupportedOperationException("Unable to Locate insert operation ");
        }
        getContext().getContentResolver().notifyChange(returnUri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        mSQLite = movieDatabaseHelper.getWritableDatabase();
        int deleteId = sUriMatcher.match(uri);
        int deleteRows;
        switch (deleteId){
            case MOVIE_TASK_ID:
                deleteRows= mSQLite.delete(MovieDatabaseContract.MovieDatabaseEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case MOVIE_TASK_ITEM_ID:
                String deletionId = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String [] mSelectionArgs = new String[]{deletionId};
                deleteRows = mSQLite.delete(MovieDatabaseContract.MovieDatabaseEntry.TABLE_NAME,mSelection,mSelectionArgs);
                default:
                    throw new UnsupportedOperationException("Unable to Locate the Uri");
        }
        return deleteRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        return super.bulkInsert(uri, values);
    }
}
