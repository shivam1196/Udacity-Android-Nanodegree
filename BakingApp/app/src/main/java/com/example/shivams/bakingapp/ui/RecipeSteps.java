package com.example.shivams.bakingapp.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.shivams.bakingapp.R;
import com.example.shivams.bakingapp.model.internetpojo.Ingredients;
import com.example.shivams.bakingapp.model.internetpojo.Steps;
import com.example.shivams.bakingapp.ui.fragments.RecipeStepFragments;
import com.example.shivams.bakingapp.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class RecipeSteps extends AppCompatActivity implements RecipeStepFragments.ButtonClick ,RecipeStepFragments.BackToActivity {
    private List<Ingredients> ingredientsList;
    private List<Steps> stepsList;
    private int Position;
    private long milisec;
    RecipeStepFragments recipeStepFragments;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(!DataHelper.checkIsOnline(getApplicationContext())){
            Toast.makeText(this, "No Internet Connection! Videos will not be avaialble", Toast.LENGTH_SHORT).show();
        }
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
          //  recipeStepFragments = (RecipeStepFragments) getSupportFragmentManager().getFragment(savedInstanceState,"steps");
            recipeStepFragments = new RecipeStepFragments();
            Position = savedInstanceState.getInt("position");
            milisec=savedInstanceState.getLong("mili");
            Log.v("VALUEOFPOS",Position+"");
            stepsList = savedInstanceState.getParcelableArrayList("steps");
            recipeStepFragments.setListPosition(Position);
            recipeStepFragments.setmStepsDescriptionText(stepsList.get(Position).getDescription());
            recipeStepFragments.setmVideoUri(stepsList.get(Position).getVideoURL());
            recipeStepFragments.setmStepsList(stepsList);
            recipeStepFragments.setmThumbnailUrl(stepsList.get(Position).getThumbnailURL());
            recipeStepFragments.setMediaPlayerPosition(milisec);
            fragmentManager.beginTransaction().replace(R.id.frame_recipe_steps_container,recipeStepFragments).commit();
        }else if(savedInstanceState == null){
            ingredientsList = getIntent().getParcelableArrayListExtra("ingredients");
            stepsList = getIntent().getParcelableArrayListExtra("steps");
            Position = getIntent().getIntExtra("position", 0);
            Log.v("INDEXPOSITION", Position + "");
            recipeStepFragments = new RecipeStepFragments();
            recipeStepFragments.setmStepsDescriptionText(stepsList.get(Position).getDescription());
            recipeStepFragments.setmVideoUri(stepsList.get(Position).getVideoURL());
            recipeStepFragments.setListPosition(Position);
            recipeStepFragments.setmStepsList(stepsList);
            recipeStepFragments.setmThumbnailUrl(stepsList.get(Position).getThumbnailURL());
            recipeStepFragments.setMediaPlayerPosition(0);
            fragmentManager.beginTransaction().add(R.id.frame_recipe_steps_container, recipeStepFragments).commit();
        }
        }


    @Override
    public void toggleButton(int position) {
       // Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
         recipeStepFragments = new RecipeStepFragments();
        recipeStepFragments.setmStepsDescriptionText(stepsList.get(position).getDescription());
        recipeStepFragments.setmVideoUri(stepsList.get(position).getVideoURL());
        recipeStepFragments.setListPosition(position);
        recipeStepFragments.setmStepsList(stepsList);
        recipeStepFragments.setmThumbnailUrl(stepsList.get(Position).getThumbnailURL());
        recipeStepFragments.setMediaPlayerPosition(0);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_recipe_steps_container,recipeStepFragments).commit();
        Position = position;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
      //  getSupportFragmentManager().putFragment(outState,"steps",recipeStepFragments);
        outState.putInt("position",Position);
        Log.v("POSITION",Position+"");
        outState.putParcelableArrayList("steps",(ArrayList)stepsList);
        outState.putLong("mili",milisec);
    }



    @Override
    public void miliSec(long mili) {
        milisec=mili;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
