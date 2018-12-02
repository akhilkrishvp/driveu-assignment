package com.driveu.driveutest.UI.Home;

import android.util.Log;

import com.driveu.driveutest.Model.LocationModel;
import com.driveu.driveutest.NetworkApi.RetroClientService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akhil on 30/11/18.
 */

public class LocationInteractorImpl implements LocationInteractor{
    @Override
    public void getLatestLocation(final LocationListner locationListner) {
        RetroClientService.getService().getLatestLocation().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    Gson gson = new Gson();
                    LocationModel locationModel = new LocationModel();
                    locationModel = gson.fromJson(response.body(),LocationModel.class);
                    locationListner.onResponseSuccess(locationModel);
                }
                else {
                    try {
                        JSONObject errorObj = new JSONObject(response.errorBody().string());
                        Log.e("error.response..", errorObj.toString());
                        locationListner.onResponseFailure(errorObj.getString("status"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
