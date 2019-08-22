package com.example.shivams.udacityassignmentstage1;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.shivams.udacityassignmentstage1.utilities.JsonUtilities;
import com.example.shivams.udacityassignmentstage1.utilities.NetworkUtilities;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.shivams.udacityassignmentstage1.utilities.NetworkUtilities.responseFromServer;

public class ReviewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String [] >{
    private RecyclerView mRecyclerViewForReviews;
    private ProgressBar mReviewLoaderIndicator;
    private String movieID;

    private String[] reviewTitle;
    private String[] reviewDescription;
    private LoaderManager mLoaderManager;
    private Loader<String[] > mLoader;
    private MovieReviewsAdaptor movieReviewsAdaptor;

    private static final int LOADER_RESOURCE_ID = 2409;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        movieID = getIntent().getStringExtra("id");
        Log.v("MovieId",movieID);
        mRecyclerViewForReviews = (RecyclerView)findViewById(R.id.rv_list_movie_reviews);
        mReviewLoaderIndicator=(ProgressBar)findViewById(R.id.pb_review_loader);
        movieReviewsAdaptor = new MovieReviewsAdaptor();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewForReviews.setLayoutManager(linearLayoutManager);
        mLoaderManager  = getSupportLoaderManager();
        mLoader = mLoaderManager.getLoader(LOADER_RESOURCE_ID);
        mLoaderManager.initLoader(LOADER_RESOURCE_ID,null,this);

    }

    @NonNull
    @Override
    public Loader<String[]> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {
            @Override
            protected void onStartLoading() {
                mReviewLoaderIndicator.setVisibility(View.VISIBLE);
                mRecyclerViewForReviews.setVisibility(View.INVISIBLE);
                if(reviewTitle!=null){
                    deliverResult(reviewTitle);
                }
                else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public String[] loadInBackground() {
                try {
                    URL url = NetworkUtilities.buildReviewsUrl(movieID);
                   String response=  NetworkUtilities.responseFromServer(url);
                    JSONArray jsonArray = JsonUtilities.convertIntoJson(response);
                    String [] responseReview = new String[jsonArray.length()];
                    responseReview = JsonUtilities.generateMovieReviewsTitle(jsonArray);
                    reviewDescription = JsonUtilities.generateMovieReviewsDescription(jsonArray);

                    return responseReview;
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
                reviewTitle = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String[]> loader, String[] data) {
            mReviewLoaderIndicator.setVisibility(View.INVISIBLE);
            mRecyclerViewForReviews.setVisibility(View.VISIBLE);
            movieReviewsAdaptor.setDataForReviews(data,reviewDescription);
            mRecyclerViewForReviews.setAdapter(movieReviewsAdaptor);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<String[]> loader) {

    }
}
