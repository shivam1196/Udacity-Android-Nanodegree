package com.example.shivams.udacityassignmentstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MoviePoster extends RecyclerView.Adapter<MoviePoster.MovieViewHolder> {
    private String [] arrayOfPosters;
    private ListItemClickHandler listItemClickHandler;
    private Context context;


    public MoviePoster( ListItemClickHandler listItemClickHandler){

        this.listItemClickHandler = listItemClickHandler;
    }

    @Override
    public int getItemCount() {
        if(arrayOfPosters == null){
            return 1;
        }
        else {
            return arrayOfPosters.length;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MoviePoster.MovieViewHolder holder, int position) {

        Picasso.with(context).load(arrayOfPosters[position]).into(holder.showPosterImages);
    }

    @NonNull
    @Override
    public MoviePoster.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.movie_poster,parent,false);
        return new MovieViewHolder(view);
    }
    public void setData(String [] arrayOfPosters){
        this.arrayOfPosters =arrayOfPosters;
        notifyDataSetChanged();
    }

    public interface ListItemClickHandler{
        public void clickMethod(int position);
    }

    public  class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView showPosterImages;

        public MovieViewHolder(View view){
            super(view);
            showPosterImages = (ImageView)view.findViewById(R.id.iv_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int postion = getAdapterPosition();
            listItemClickHandler.clickMethod(postion);

        }
    }
}
