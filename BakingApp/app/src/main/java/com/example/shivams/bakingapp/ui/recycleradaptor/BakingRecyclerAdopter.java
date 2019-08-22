package com.example.shivams.bakingapp.ui.recycleradaptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shivams.bakingapp.R;

public class BakingRecyclerAdopter extends RecyclerView.Adapter<BakingRecyclerAdopter.BakingViewHolder> {
    public BakingItemClick bakingItemClick;
    private String[] recipesNames;

    public void setImageUrl(String[] imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String[] imageUrl;

    public interface BakingItemClick{
        public void itemClick(int position);
    }

    public BakingRecyclerAdopter(BakingItemClick bakingItemClick){
        this.bakingItemClick = bakingItemClick;
    }
    @NonNull
    @Override
    public BakingRecyclerAdopter.BakingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.baking_recycler,parent,false);
        return new BakingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BakingRecyclerAdopter.BakingViewHolder holder, int position) {
        holder.mRecipeNames.setText(recipesNames[position]);


    }

    @Override
    public int getItemCount() {
        if (recipesNames == null){
            recipesNames[0] = "No internet connection";
            return 1;
        }
        else {
           return recipesNames.length;
        }

    }

    public void setReciepesNames(String [] recipeNames){
        this.recipesNames = recipeNames;
        notifyDataSetChanged();
    }


    public class BakingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mRecipeNames;

        public BakingViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            mRecipeNames = (TextView)view.findViewById(R.id.tv_recipe_name);

        }

        @Override
        public void onClick(View v) {
            int postion = getAdapterPosition();
            bakingItemClick.itemClick(postion);
        }
    }
}
