package com.example.shivams.bakingapp.internet;

import com.example.shivams.bakingapp.model.internetpojo.BakingResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingApi {

    @GET("baking.json")
    Call<List<BakingResponse>> getBakingAppResponse();
}
