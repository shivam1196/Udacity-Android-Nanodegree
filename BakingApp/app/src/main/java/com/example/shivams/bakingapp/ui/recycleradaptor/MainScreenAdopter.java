package com.example.shivams.bakingapp.ui.recycleradaptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shivams.bakingapp.R;
import com.squareup.picasso.Picasso;

public class MainScreenAdopter extends RecyclerView.Adapter<MainScreenAdopter.MainScreenViewHolder> {
    private String[] recipeName;
    private String[] recipeImage;
    private  RecipeClick recipeClick;
    private Context context;
    private boolean status;

    public MainScreenAdopter(RecipeClick recipeClick){
        this.recipeClick=recipeClick;
    }
    @NonNull
    @Override
    public MainScreenAdopter.MainScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.main_screen_recycler_view,parent,false);
        return new MainScreenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainScreenAdopter.MainScreenViewHolder holder, int position) {
        holder.mRecipeName.setText(recipeName[position]);
        if(recipeImage[position] == "" || status){
            holder.mRecipeImage.setImageResource(R.drawable.ic_image_black_24dp);
        }
        else {
            Picasso.with(context).load(recipeImage[position]).into(holder.mRecipeImage);
        }

    }

    @Override
    public int getItemCount() {
        if (recipeName == null){
            recipeName[0] = "No internet connection";
            if(recipeImage == null){
                status = true;
            }
            return 1;
        }

        else {
            return recipeName.length;
        }

    }

    public void setRecipeName(String[] recipeName) {
        this.recipeName = recipeName;
    }

    public void setRecipeImage(String[] recipeImage) {
        this.recipeImage = recipeImage;
    }

    public interface RecipeClick{
        void recipeItem(int position);
    }

    public class MainScreenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mRecipeName;
        private ImageView mRecipeImage;

        public MainScreenViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            mRecipeImage=(ImageView)view.findViewById(R.id.main_screen_image);
            mRecipeName=(TextView)view.findViewById(R.id.tv_recipe_name_main);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            recipeClick.recipeItem(position);
        }
    }
}
