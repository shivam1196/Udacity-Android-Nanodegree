package com.example.shivams.udacityassignmentstage1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shivams.udacityassignmentstage1.model.MovieDetails;
import com.example.shivams.udacityassignmentstage1.utilities.JsonUtilities;
import com.example.shivams.udacityassignmentstage1.utilities.NetworkUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MoviePoster.ListItemClickHandler {
    private RecyclerView mShowPoster;
    private ProgressBar mLoadingIndicator;
    private MoviePoster mMoviePoster;
    private String[] posterUrl={"http://img3.looper.com/img/gallery/the-untold-truth-of-dragon-ball-z/rock-the-power-1517840155.jpg",
            "http://img.kbhgames.com/2015/04/dragon-ball-z-the-legacy-of-goku-21.jpg","https://images2.alphacoders.com/656/thumb-1920-656016.jpg",
                "https://qph.fs.quoracdn.net/main-qimg-fcab3bd84a94e472808e9c286897102b-c"};
    private String responseFromServer;
    private JSONArray responseInJson;
    public static int mId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            mId= savedInstanceState.getInt("key");
        }
        else {
            mId =0;
        }
        setContentView(R.layout.activity_main);
        if(!isOnline()){
            Toast.makeText(this, "No Internet Connection! Please Try Later", Toast.LENGTH_SHORT).show();
        }
        else {
            mShowPoster = (RecyclerView) findViewById(R.id.rv_show_movies);
            int numberOfColoumns = 2;
            mShowPoster.setLayoutManager(new GridLayoutManager(this, numberOfColoumns));
            mShowPoster.setHasFixedSize(true);
            mMoviePoster = new MoviePoster(this);
            mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
            loadData(mId);


        }
    }

    @Override
    public void clickMethod(int position) {
       // Toast.makeText(this, position+" Clicked", Toast.LENGTH_SHORT).show();
        try {
            MovieDetails movieDetails = JsonUtilities.generateMovieDetails(responseInJson,position);
            Intent intent = new Intent(this,DetailsActivity.class);
            intent.putExtra("MovieDetails",movieDetails);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    private void loadData(int i ){
        if(!isOnline()){
            Toast.makeText(this, "No Internet Connection! Please Try Later", Toast.LENGTH_SHORT).show();
        }
        else {
            new FetchData().execute(i);
        }
    }
    public class FetchData extends AsyncTask<Integer,Void,String[]>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(Integer... integers) {
            int id = integers[0];
            URL moviesUrl = null;

            try {
                if(id == 0) {
                    moviesUrl = NetworkUtilities.buildUrl();
                    mId = 0;
                }
                else if(id == 1){
                    moviesUrl =NetworkUtilities.buildUrlForTopRated();
                    mId=1;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                responseFromServer = NetworkUtilities.responseFromServer(moviesUrl);
                Log.v("SERVER",responseFromServer);
                responseInJson = JsonUtilities.convertIntoJson(responseFromServer);
                String []  completePosterUrl = JsonUtilities.generateCompletePosterPath(responseInJson);
                String s1 = completePosterUrl[0];
                return completePosterUrl;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String[] strings) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            Log.v("POSTEXECUTE",strings[0]);
            if(strings!=null){
                Log.v("PostExecute",strings[0]);
                mMoviePoster.setData(strings);
                mShowPoster.setAdapter(mMoviePoster);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_item,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
      if(id == R.id.menu_sort_popular){
           loadData(0);
        }
        else if(id == R.id.menu_sort_most_view){
            loadData(1);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline(){
        ConnectivityManager connectivityManager =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnectedOrConnecting());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("key",mId);
        super.onSaveInstanceState(outState);
    }
}
