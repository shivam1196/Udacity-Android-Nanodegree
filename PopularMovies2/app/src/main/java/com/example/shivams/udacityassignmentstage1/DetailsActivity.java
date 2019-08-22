package com.example.shivams.udacityassignmentstage1;

import android.content.ActivityNotFoundException;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shivams.udacityassignmentstage1.data.MovieDatabaseContract;
import com.example.shivams.udacityassignmentstage1.model.MovieDetails;
import com.example.shivams.udacityassignmentstage1.model.TrailerDetails;
import com.example.shivams.udacityassignmentstage1.sync.BackgroundTasks;
import com.example.shivams.udacityassignmentstage1.utilities.JsonUtilities;
import com.example.shivams.udacityassignmentstage1.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]>{
    private ImageView mThumbnail;
    private TextView mTitle ,mReleaseDate,mRating,mDescription,mTrailersTextView;
    private ListView mTrailers;
    private String mId,sTitle,sReleaseDate,sRating,sDescription,sPosterPath,sBackdropPath;
    private Button mMarkFavourite,mReadReviews;
    private LoaderManager loaderManager;
    private static final int LOADER_RESOURCE_ID=1101;
    private Loader<String> loader ;
    private String[] responseFromServer = null;
    private static String[] sResponseKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        String action = intent.getAction();
        MovieDetails movieDetails = intent.getParcelableExtra("MovieDetails");
        mId = movieDetails.getmId();
        sTitle= movieDetails.getmMovieName();
        sDescription= movieDetails.getmMovieDescription();
        sRating=movieDetails.getmMovieRating();
        sReleaseDate=movieDetails.getmMovieReleaseDate();
        sPosterPath =movieDetails.getmMovieBackdrop();
        sBackdropPath = movieDetails.getmMoviePoster();
        mTrailers = (ListView) findViewById(R.id.lv_show_trailers);
        mThumbnail = (ImageView) findViewById(R.id.iv_movie_thumbnail);
        mTitle = (TextView) findViewById(R.id.tv_movie_title);
        mTrailersTextView=(TextView)findViewById(R.id.tv_trailers);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mRating = (TextView) findViewById(R.id.tv_movie_rating);
        mDescription = (TextView) findViewById(R.id.tv_movie_description);
        mMarkFavourite = (Button)findViewById(R.id.bn_mark_fav);
        mReadReviews=(Button)findViewById(R.id.bn_read_reivews);
        boolean favouriteStatus = isMarkedFavourite(mId);

        if (!NetworkUtilities.isOnline(this) && action.equals("storage")) {
          //  Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
//            mThumbnail = (ImageView) findViewById(R.id.iv_movie_thumbnail);
            mThumbnail.setImageResource(R.drawable.nowifi);
            mTitle.setText(movieDetails.getmMovieName());
            mDescription.setText(movieDetails.getmMovieDescription());
            mReleaseDate.setText(movieDetails.getmMovieReleaseDate());
            mRating.setText(movieDetails.getmMovieRating() + "/10");
        }
        else if(!NetworkUtilities.isOnline(this)){
            Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
        }
        else {


            if(favouriteStatus ){
                mMarkFavourite.setText("Marked Favourite");
            }
            else if(!favouriteStatus) {
                mMarkFavourite.setText("Mark as Favourite");
            }
            loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(LOADER_RESOURCE_ID, null, this);
            String completeBackdropUrl = NetworkUtilities.completePosterPath(movieDetails.getmMovieBackdrop());
            Log.v("BackdropUrl",completeBackdropUrl);
            Picasso.with(this).load(completeBackdropUrl).into(mThumbnail);
            mTitle.setText(movieDetails.getmMovieName());
            mDescription.setText(movieDetails.getmMovieDescription());
            mReleaseDate.setText(movieDetails.getmMovieReleaseDate());
            mRating.setText(movieDetails.getmMovieRating() + "/10");
        }
    }


    @NonNull
    @Override
    public Loader<String[]> onCreateLoader(final int id, @Nullable final Bundle args) {
        return new android.support.v4.content.AsyncTaskLoader<String[]>(this) {

            @Override
            protected void onStartLoading() {
                if(responseFromServer!=null){
                    deliverResult(responseFromServer);
               }
               else {
                    forceLoad();
               }
            }

            @Nullable
            @Override
            public String[] loadInBackground() {
                try {
                    String[] trailerDetails = null;
                    URL videoUrl= NetworkUtilities.buildVideosUrl(mId);
                    String response = NetworkUtilities.responseFromServer(videoUrl);
                    Log.v("Trailer_Response",response);
                    JSONArray responseArray = JsonUtilities.convertIntoJson(response);
                    sResponseKey = JsonUtilities.generateKeyForYoutube(responseArray);
                     trailerDetails = JsonUtilities.generateNameForYoutube(responseArray);
                    Log.e("TRAILER_SIZE",trailerDetails.length+"");
                    return trailerDetails;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void deliverResult(@Nullable String[] data) {
                responseFromServer = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String[]> loader, final String[] data) {
        String [] trailerTitle= new String[data.length];
        for(int i =0;i<data.length;i++){
            trailerTitle[i]= data[i];
            Log.v("KEY_VALUES",sResponseKey[i]);
        }
        ArrayAdapter<String > arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,trailerTitle);
        mTrailers.setAdapter(arrayAdapter);
        mTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openYoutube(sResponseKey[position]);
            }
        });

        mMarkFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMarkFavourite.getText().toString().equals("Mark as Favourite")) {
                    Intent intent = new Intent(getApplicationContext(), BackgroundTasks.class);
                    Bundle dataSet = new Bundle();
                    dataSet.putString("movie_id", mId);
                    dataSet.putString("movie_title", sTitle);
                    dataSet.putString("movie_description", sDescription);
                    dataSet.putString("movie_rating", sRating);
                    dataSet.putString("movie_release_date", sReleaseDate);
                    dataSet.putString("movie_poster_path", sPosterPath);
                    dataSet.putString("movie_backdrop_path", sBackdropPath);
                    intent.putExtras(dataSet);
                    startService(intent);
                    mMarkFavourite.setText("Marked Favourites");

                    Cursor cursor = getContentResolver().query(MovieDatabaseContract.MovieDatabaseEntry.mDbUri, null, null, null, null, null);
                }
                else {
                    String[] deletionArgs = {mId};
                    getContentResolver().delete(MovieDatabaseContract.MovieDatabaseEntry.mDbUri, MovieDatabaseContract.MovieDatabaseEntry.MOVIE_ID+"=?",deletionArgs);
                    mMarkFavourite.setText("Mark as Favourite");
                }
               // Toast.makeText(DetailsActivity.this, cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieDatabaseEntry.MOVIE_NAME))+"", Toast.LENGTH_SHORT).show();
            }
        });
        mReadReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(),ReviewsActivity.class);
                newIntent.putExtra("id",mId);
                startActivity(newIntent);
            }
        });
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String[]> loader) {

    }






    public void openYoutube(String position){
        Intent appYoutubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+position));
        Intent webYoutubeIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v="+position));
        try {
            this.startActivity(appYoutubeIntent);
        }catch (ActivityNotFoundException e){
            this.startActivity(webYoutubeIntent);
        }
    }

    public boolean isMarkedFavourite(String id){
        String [] selectionArgs= {id};
        Cursor cursor = getContentResolver().query(MovieDatabaseContract.MovieDatabaseEntry.mDbUri,null, MovieDatabaseContract.MovieDatabaseEntry.MOVIE_ID+"=?",selectionArgs,null);
        if(cursor.getCount() > 0){
            return true;
        }
        else {
            return false;
        }
    }
}
