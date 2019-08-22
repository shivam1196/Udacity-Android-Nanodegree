package com.example.shivams.udacityassignmentstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shivams.udacityassignmentstage1.model.MovieDetails;
import com.example.shivams.udacityassignmentstage1.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    private ImageView mThumbnail;
    private TextView mTitle ,mReleaseDate,mRating,mDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        MovieDetails movieDetails = intent.getParcelableExtra("MovieDetails");
        mThumbnail=(ImageView)findViewById(R.id.iv_movie_thumbnail);
        mTitle=(TextView)findViewById(R.id.tv_movie_title);
        mReleaseDate=(TextView)findViewById(R.id.tv_release_date);
        mRating=(TextView)findViewById(R.id.tv_movie_rating);
        mDescription=(TextView)findViewById(R.id.tv_movie_description);

        String completeBackdropUrl = NetworkUtilities.completePosterPath(movieDetails.getmMoviePoster());
        Picasso.with(this).load(completeBackdropUrl).into(mThumbnail);
        mTitle.setText(movieDetails.getmMovieName());
        mDescription.setText(movieDetails.getmMovieDescription());
        mReleaseDate.setText(movieDetails.getmMovieReleaseDate());
        mRating.setText(movieDetails.getmMovieRating()+"/10");
    }
}
