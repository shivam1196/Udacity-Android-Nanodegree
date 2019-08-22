package com.example.shivams.udacityassignmentstage1.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.example.shivams.udacityassignmentstage1.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtilities {
    public static final String BASE_URL= "http://api.themoviedb.org/3/movie/popular";
    public static final String BASE_START_URL="http://api.themoviedb.org/3/movie/";
    public static final String BASE_URL_TOP_RATED ="http://api.themoviedb.org/3/movie/top_rated";
    public static final String API_KEY = "8155a73d1352ee6551cf84eed0cef43d";
    public static final String BASE_URL_POSTER = "http://image.tmdb.org/t/p/";
    public static final String POSTER_SIZE = "w500/";
    public static final String VIDEO_URL ="/videos";
    public static final String REVIEWS_URL="/reviews";

    public static URL buildUrl() throws MalformedURLException{
        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter("api_key",API_KEY).build();

        URL webURL = new URL(buildUri.toString());

        return webURL;
    }

    public static URL buildUrlForTopRated() throws MalformedURLException{
        Uri buildUri = Uri.parse(BASE_URL_TOP_RATED).buildUpon()
                .appendQueryParameter("api_key",API_KEY).build();

        URL webURL = new URL(buildUri.toString());

        return webURL;
    }

    public static URL buildVideosUrl(String id) throws MalformedURLException{
        Uri videosUri = Uri.parse(BASE_START_URL+id+VIDEO_URL).buildUpon()
                .appendQueryParameter("api_key",API_KEY).build();

        URL videosUrl = new URL(videosUri.toString());
        return videosUrl;
    }

    public static URL buildReviewsUrl(String id) throws MalformedURLException{
        Uri reviewsUri = Uri.parse(BASE_START_URL+id+REVIEWS_URL).buildUpon()
                .appendQueryParameter("api_key",API_KEY)
                .build();

        URL reviewsUrl = new URL(reviewsUri.toString());
        return reviewsUrl;
    }


    public static String responseFromServer(URL url) throws IOException{
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        InputStream inputStream = httpURLConnection.getInputStream();
        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\A");
        boolean hasInput = scanner.hasNext();
        if(hasInput){
                return scanner.next();
            }
            else {
            httpURLConnection.disconnect();
            return null;
            }

    }


    public static String completePosterPath(String urlFromJson){
        return BASE_URL_POSTER+POSTER_SIZE+urlFromJson;

    }


    public static boolean isOnline(Context context){
        ConnectivityManager connectivityManager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnectedOrConnecting());
    }



}
