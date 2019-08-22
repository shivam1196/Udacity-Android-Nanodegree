package com.example.shivams.udacityassignmentstage1.utilities;

import android.util.Log;

import com.example.shivams.udacityassignmentstage1.model.MovieDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public final class JsonUtilities {

    public static JSONArray convertIntoJson(String response) throws JSONException{
        JSONObject movieResponse = new JSONObject(response);
        JSONArray movieResponseData = movieResponse.getJSONArray("results");
        return movieResponseData;
        }

    public static String[] generateCompletePosterPath(JSONArray jsonArray) throws JSONException{
        JSONObject jsonObject = null;
        String posterPath= null;
        String []arrayOfPosterPath  = new String[jsonArray.length()];
        for(int i =0;i<jsonArray.length();i++){
           jsonObject = jsonArray.getJSONObject(i);
           posterPath = jsonObject.getString("poster_path");
           arrayOfPosterPath[i]=NetworkUtilities.completePosterPath(posterPath);
           Log.v("POSTERPATH",arrayOfPosterPath[i]);
        }


        return arrayOfPosterPath;

    }

    public static MovieDetails generateMovieDetails(JSONArray jsonArray,int position) throws JSONException{
        JSONObject jsonObject = null;
        MovieDetails movieDetails = new MovieDetails();
        for(int i = 0;i<jsonArray.length();i++){
            if(i == position){
               jsonObject = jsonArray.getJSONObject(position);
               movieDetails.setmMovieName(jsonObject.getString("title"));
               movieDetails.setmMoviePoster(jsonObject.getString("backdrop_path"));
               movieDetails.setmMovieDescription(jsonObject.getString("overview"));
               movieDetails.setmMovieReleaseDate(jsonObject.getString("release_date"));
               movieDetails.setmMovieRating(jsonObject.getString("vote_average"));
            }
        }
        return movieDetails;
    }

}
