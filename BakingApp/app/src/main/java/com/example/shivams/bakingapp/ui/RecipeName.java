package com.example.shivams.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.example.shivams.bakingapp.R;
import com.example.shivams.bakingapp.model.internetpojo.Ingredients;
import com.example.shivams.bakingapp.model.internetpojo.Steps;
import com.example.shivams.bakingapp.ui.fragments.RecipeNameFragment;
import com.example.shivams.bakingapp.ui.fragments.RecipeStepFragments;

import java.util.ArrayList;
import java.util.List;

public class RecipeName extends AppCompatActivity implements RecipeNameFragment.StepClick , RecipeStepFragments.BackToActivity{
    private ListView mRecipeIngredientsAndDescription;
    private List<Ingredients> ingredientsList;
    private List<Steps> stepsList;
    private boolean mTwoPane;
    FragmentManager fragmentManager;
    private RecipeStepFragments recipeStepFragments;
    private RecipeNameFragment recipeNameFragment;
    private int Position;
    private long mili;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ingredientsList = getIntent().getParcelableArrayListExtra("ingredients");
        stepsList = getIntent().getParcelableArrayListExtra("steps");
        if(savedInstanceState!=null){
            ingredientsList = savedInstanceState.getParcelableArrayList("ingredients");
            stepsList = savedInstanceState.getParcelableArrayList("steps");
            mTwoPane = savedInstanceState.getBoolean("twopane");
            Position = savedInstanceState.getInt("position");
            mili=savedInstanceState.getLong("mili");
            if(findViewById(R.id.activity_recipe_name)!=null){
                recipeNameFragment = new RecipeNameFragment();
               recipeNameFragment.setIngredientsList(ingredientsList);
               recipeNameFragment.setStepsList(stepsList);
               getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_container,recipeNameFragment).commit();

                recipeStepFragments = new RecipeStepFragments();
                recipeStepFragments.setmStepsDescriptionText(stepsList.get(Position).getDescription());
                recipeStepFragments.setmVideoUri(stepsList.get(Position).getVideoURL());
                recipeStepFragments.setListPosition(Position);
                recipeStepFragments.setmStepsList(stepsList);
                recipeStepFragments.setmThumbnailUrl(stepsList.get(Position).getThumbnailURL());
                recipeStepFragments.setDisableButton(mTwoPane);
                recipeStepFragments.setMediaPlayerPosition(mili);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_recipe_steps_container,recipeStepFragments).commit();

            }
            else {
                 recipeNameFragment = new RecipeNameFragment();
                recipeNameFragment.setIngredientsList(ingredientsList);
                recipeNameFragment.setStepsList(stepsList);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_container,recipeNameFragment).commit();
            }
        }
        else {
            if (findViewById(R.id.activity_recipe_name) != null) {
                mTwoPane = true;
                recipeNameFragment = new RecipeNameFragment();
                recipeNameFragment.setIngredientsList(ingredientsList);
                recipeNameFragment.setStepsList(stepsList);

                fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction().add(R.id.frame_layout_container, recipeNameFragment).commit();

                recipeStepFragments = new RecipeStepFragments();
                recipeStepFragments.setmStepsDescriptionText(stepsList.get(0).getDescription());
                recipeStepFragments.setmVideoUri(stepsList.get(0).getVideoURL());
                recipeStepFragments.setListPosition(0);
                recipeStepFragments.setmStepsList(stepsList);
                recipeStepFragments.setDisableButton(mTwoPane);
                recipeStepFragments.setmThumbnailUrl(stepsList.get(Position).getThumbnailURL());
                recipeStepFragments.setMediaPlayerPosition(0);
                getSupportFragmentManager().beginTransaction().add(R.id.frame_recipe_steps_container, recipeStepFragments).commit();


            } else {
                mTwoPane = false;
                recipeNameFragment = new RecipeNameFragment();
                recipeNameFragment.setIngredientsList(ingredientsList);
                recipeNameFragment.setStepsList(stepsList);

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction().add(R.id.frame_layout_container, recipeNameFragment).commit();


            }
        }

        }



    @Override
    public void clickedStepIndex(int postion) {
        if(!mTwoPane) {
            Intent intent = new Intent(RecipeName.this, RecipeSteps.class);
            intent.putParcelableArrayListExtra("ingredients", (ArrayList) ingredientsList);
            intent.putParcelableArrayListExtra("steps", (ArrayList) stepsList);
            intent.putExtra("position", postion);
            startActivity(intent);
        }
        else{
            RecipeStepFragments recipeStepFragments = new RecipeStepFragments();
            recipeStepFragments.setmStepsDescriptionText(stepsList.get(postion).getDescription());
            recipeStepFragments.setmVideoUri(stepsList.get(postion).getVideoURL());
            recipeStepFragments.setListPosition(postion);
            recipeStepFragments.setmStepsList(stepsList);
            recipeStepFragments.setDisableButton(mTwoPane);
            recipeStepFragments.setmThumbnailUrl(stepsList.get(Position).getThumbnailURL());
            recipeStepFragments.setMediaPlayerPosition(0);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_recipe_steps_container,recipeStepFragments).commit();
            Position = postion;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("steps",(ArrayList) stepsList);
        outState.putParcelableArrayList("ingredients",(ArrayList)ingredientsList);
        outState.putBoolean("twopane",mTwoPane);
        outState.putInt("position",Position);
        outState.putLong("mili",mili);
    }

    @Override
    public void miliSec(long mili) {
        this.mili=mili;
    }
}
