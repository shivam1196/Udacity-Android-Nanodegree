package com.example.shivams.udacityassignmentstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MovieReviewsAdaptor extends RecyclerView.Adapter<MovieReviewsAdaptor.ReviewsViewHolder> {

   private String [] movieReviewsTitle;
   private String [] movieReviewsDescription;
   private Context context;
    public MovieReviewsAdaptor(){

    }


    @Override
    public int getItemCount() {
        if(movieReviewsTitle.length == 0){

            return 1;
        }
        else {
            return movieReviewsDescription.length;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        if(movieReviewsTitle.length == 0){
            holder.mReviewTitle.setText("No Review Found");
            holder.mReviewDescription.setText("No Review Found");
        }
        else {
            holder.mReviewTitle.setText(movieReviewsTitle[position]);
            Log.v("TITLE", movieReviewsTitle[position]);
            holder.mReviewDescription.setText(movieReviewsDescription[position]);
        }
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.movie_reviews,parent,false);

        return new ReviewsViewHolder(view);
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder{
        private TextView mReviewTitle,mReviewDescription;
        public ReviewsViewHolder(View view){
            super(view);
            mReviewTitle= (TextView)view.findViewById(R.id.tv_movies_review_title);
            mReviewDescription=(TextView)view.findViewById(R.id.tv_movie_review_detail);
        }

    }


    public void setDataForReviews(String[] reviewTitle,String[] reviewDescription){
        movieReviewsTitle=reviewTitle;
        movieReviewsDescription=reviewDescription;

        notifyDataSetChanged();
    }
}
