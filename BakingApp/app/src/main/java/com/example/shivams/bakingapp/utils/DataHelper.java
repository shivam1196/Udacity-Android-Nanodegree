package com.example.shivams.bakingapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.shivams.bakingapp.internet.Access;
import com.example.shivams.bakingapp.internet.BakingApi;
import com.example.shivams.bakingapp.model.internetpojo.BakingResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataHelper {

    public static boolean checkIsOnline(Context context ){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnectedOrConnecting());

    }
}
