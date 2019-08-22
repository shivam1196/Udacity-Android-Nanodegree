package com.example.shivams.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.shivams.bakingapp.BakingAppWidget;
import com.example.shivams.bakingapp.R;
import com.example.shivams.bakingapp.internet.Access;
import com.example.shivams.bakingapp.internet.BakingApi;
import com.example.shivams.bakingapp.model.internetpojo.BakingResponse;
import com.example.shivams.bakingapp.model.internetpojo.Ingredients;
import com.example.shivams.bakingapp.model.internetpojo.Steps;
import com.example.shivams.bakingapp.ui.recycleradaptor.BakingRecyclerAdopter;
import com.example.shivams.bakingapp.ui.recycleradaptor.MainScreenAdopter;
import com.example.shivams.bakingapp.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity implements MainScreenAdopter.RecipeClick{
    private RecyclerView mRecyclerView;
    private List<BakingResponse> mTotalData ;
    MainScreenAdopter mainScreenAdopter;
    private boolean mTwoPane;

    private String [] reciepeNames;
    private String [] recipeImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.main_activity_tab_layout)!= null){
            mTwoPane = true;
            mRecyclerView=(RecyclerView)findViewById(R.id.main_screen_rv);
            mainScreenAdopter = new MainScreenAdopter(this);
            GridLayoutManager linearLayoutManager = new GridLayoutManager(this,3);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            fetchBakingData(mRecyclerView,mainScreenAdopter);

        }
        else {
            initViews();
            mTwoPane = false;
        }

    }


    private void initViews(){
        mRecyclerView=(RecyclerView)findViewById(R.id.main_screen_rv);
        mainScreenAdopter = new MainScreenAdopter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        fetchBakingData(mRecyclerView,mainScreenAdopter);


    }
    private void fetchBakingData(final RecyclerView recyclerView , final MainScreenAdopter bakingRecyclerAdopter) {
        BakingApi bakingApi = Access.getResponse();
        Call<List<BakingResponse>> bakingResponseCall = bakingApi.getBakingAppResponse();

        bakingResponseCall.enqueue(new Callback<List<BakingResponse>>() {
            @Override
            public void onResponse(Call<List<BakingResponse>> call, Response<List<BakingResponse>> response) {
                int lengthOfRecipes = response.body().size();
                setTotalData(response.body());
                Log.v("DATA",lengthOfRecipes+"");
                 reciepeNames = new String[lengthOfRecipes];
                 recipeImageUrl = new String[lengthOfRecipes];
                for (int i =0;i<reciepeNames.length;i++){
                    reciepeNames[i] = response.body().get(i).getName();
                    recipeImageUrl[i]=response.body().get(i).getImage();

                }
                bakingRecyclerAdopter.setRecipeName(reciepeNames);
                bakingRecyclerAdopter.setRecipeImage(recipeImageUrl);
                recyclerView.setAdapter(bakingRecyclerAdopter);
                updateWidget(getApplicationContext(),0);
            }

            @Override
            public void onFailure(Call<List<BakingResponse>> call, Throwable t) {
            Log.v("FAILURE",t.getMessage());
            String [] recipeNames = {"No Internet Connection"};
            bakingRecyclerAdopter.setRecipeName(recipeNames);
            recyclerView.setAdapter(bakingRecyclerAdopter);
            }
        });


    }




    public void setTotalData(List<BakingResponse> bakingResponsesList){
        mTotalData = bakingResponsesList;
        Log.v("SIZE",mTotalData.size()+"");
    }


    private void updateWidget(Context context,int position){
        AppWidgetManager appWidgetManager  = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        String completeRecipe = "";
        for(int i = 0;i<mTotalData.size();i++)
            completeRecipe=completeRecipe+reciepeNames[position]+"\n"+mTotalData.get(position).getmIngredients().get(i).getQuantity()+" "+mTotalData.get(position).getmIngredients().get(i).getMeasure()+" "+mTotalData.get(position).getmIngredients().get(i).getIngredient()+" ,";
        remoteViews.setTextViewText(R.id.appwidget_text,completeRecipe);
        ComponentName componentName = new ComponentName(context,BakingAppWidget.class);
        int[] widgetId = appWidgetManager.getAppWidgetIds(componentName);
        BakingAppWidget.updateTextWidgets(context,appWidgetManager,completeRecipe,widgetId);
    }

    @Override
    public void recipeItem(int position) {
        if(DataHelper.checkIsOnline(getApplicationContext())) {
            List<Ingredients> mIngredientsList = mTotalData.get(position).getmIngredients();
            List<Steps> mStepsList = mTotalData.get(position).getmSteps();
            Intent intent = new Intent(this, RecipeName.class);
            intent.putParcelableArrayListExtra("ingredients", (ArrayList) mIngredientsList);
            intent.putParcelableArrayListExtra("steps", (ArrayList) mStepsList);
            updateWidget(getApplicationContext(),position);
            startActivity(intent);


        }
        else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
