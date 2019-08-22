package com.example.shivams.udacityassignmentstage1.utilities;

import android.util.Log;

import com.example.shivams.udacityassignmentstage1.model.MovieDetails;
import com.example.shivams.udacityassignmentstage1.model.TrailerDetails;

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
               movieDetails.setmId(jsonObject.getString("id"));
               movieDetails.setmMovieBackdrop(jsonObject.getString("poster_path"));
            }
        }
        return movieDetails;
    }

    public static String[] generateKeyForYoutube(JSONArray jsonArray) throws JSONException{
        String [] keyYoutube = new String[jsonArray.length()];
        Log.v("KEYTUBE_DETAILS",keyYoutube.length+"");
        for(int i =0;i<jsonArray.length();i++){
            JSONObject dataValues = jsonArray.getJSONObject(i);
            String dataKey=dataValues.getString("key");
            Log.v("DataKey",dataKey);
            keyYoutube[i]=dataKey;

        }

        return keyYoutube;
    }

    public static String[] generateNameForYoutube(JSONArray jsonArray) throws JSONException{
        String[] trailerName = new String[jsonArray.length()];
        for(int i =0;i<jsonArray.length();i++){
            JSONObject dataValues = jsonArray.getJSONObject(i);
            String dataName = dataValues.getString("name");
            trailerName[i]=dataName;

        }

        return trailerName;
    }

    public static String[] generateMovieReviewsTitle(JSONArray jsonArray) throws JSONException{
        String [] reviewTitle = new String[jsonArray.length()];
        for(int i =0;i<jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            reviewTitle[i] = jsonObject.getString("author");
        }

        return reviewTitle;
    }

    public static String[] generateMovieReviewsDescription(JSONArray jsonArray) throws JSONException{
        String[] reviewDescription = new String[jsonArray.length()];
        for(int i =0;i<jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            reviewDescription[i] = jsonObject.getString("content");
        }
        return reviewDescription;
    }






}
