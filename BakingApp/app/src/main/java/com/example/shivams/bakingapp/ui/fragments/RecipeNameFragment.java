package com.example.shivams.bakingapp.ui.fragments;

import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.shivams.bakingapp.R;
import com.example.shivams.bakingapp.model.internetpojo.Ingredients;
import com.example.shivams.bakingapp.model.internetpojo.Steps;
import com.example.shivams.bakingapp.ui.recycleradaptor.BakingRecyclerAdopter;

import java.util.ArrayList;
import java.util.List;

public class RecipeNameFragment extends Fragment  implements BakingRecyclerAdopter.BakingItemClick{
    private RecyclerView mRecipesStepsandIngredients;
    private List<Ingredients> ingredientsList;
    private List<Steps> stepsList;
    StepClick mStepClick;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mStepClick = (StepClick) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement ClickListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState !=null){
//            ingredientsList = savedInstanceState.getParcelableArrayList("ingredients");
//            stepsList = savedInstanceState.getParcelableArrayList("steps");
//            Log.v("StepList",stepsList.size()+"");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState !=null){
            ingredientsList = savedInstanceState.getParcelableArrayList("ingredients");
            stepsList = savedInstanceState.getParcelableArrayList("steps");
            Log.v("StepListCreate",stepsList.size()+"");
        }
    }

    @Override
    public void itemClick(int position) {
        int actualPosition = position - ingredientsList.size();
        if (actualPosition >= 0) {
            mStepClick.clickedStepIndex(actualPosition);
            Log.v("LISTVIEW", actualPosition + "");
        }
    }

    public interface StepClick{
        void clickedStepIndex(int postion);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View rootView = null;
        if(savedInstanceState == null) {
             rootView = inflater.inflate(R.layout.recipe_name_fragment, null);
            mRecipesStepsandIngredients = (RecyclerView) rootView.findViewById(R.id.rv_recipe_name_screen);
            setUpListView(mRecipesStepsandIngredients, ingredientsList, stepsList);
            return rootView;
        }
        return rootView;
    }

    public void setIngredientsList(List<Ingredients> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public void setStepsList(List<Steps> stepsList) {
        this.stepsList = stepsList;
    }
    private void setUpListView(RecyclerView listView,List<Ingredients> ingredients, List<Steps> steps){
        int totalLength = ingredients.size()+steps.size();
        int counter =0;
        String [] recipeDescription = new String[totalLength];
        for(int i =0;i<ingredients.size();i++){
            recipeDescription[counter] = ingredients.get(i).getQuantity()+" "+ingredients.get(i).getMeasure()+" "+ingredients.get(i).getIngredient();
            Log.v("RECIPEDATA",recipeDescription[i]);
            counter++;
        }
        for(int i =0;i< steps.size();i++){
            recipeDescription[counter] = steps.get(i).getShortDescription();
            Log.v("STEPSDATA",recipeDescription[counter]);
            counter++;
        }
        for(int i = 0;i<counter;i++){
            Log.v("ARRAYLIST",recipeDescription[i]);
        }
        BakingRecyclerAdopter bakingRecyclerAdopter = new BakingRecyclerAdopter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        listView.setLayoutManager(linearLayoutManager);
        bakingRecyclerAdopter.setReciepesNames(recipeDescription);
        listView.setAdapter(bakingRecyclerAdopter);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("ingredients",(ArrayList)ingredientsList);
        outState.putParcelableArrayList("steps",(ArrayList)stepsList);
    }
}
