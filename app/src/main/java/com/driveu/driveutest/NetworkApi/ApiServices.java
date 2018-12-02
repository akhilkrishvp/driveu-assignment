package com.driveu.driveutest.NetworkApi;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

/**
 * Created by akhil on 30/11/18.b0x8a
 */

public interface ApiServices {
    @GET("explore")
    Call<JsonObject> getLatestLocation();

}
