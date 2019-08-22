package com.example.shivams.bakingapp.internet;

import com.example.shivams.bakingapp.model.internetpojo.BakingResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Access {
    private static final String BASE_URL="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";


    public static BakingApi getResponse(){
        Retrofit builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BakingApi bakingResponse = builder.create(BakingApi.class);

        return bakingResponse;
    }
}
