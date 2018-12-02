package com.driveu.driveutest.NetworkApi;

import com.driveu.driveutest.Helper.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by akhil on 19/11/18.
 */

public class RetroClientService {


    private static ApiServices apiServices;
    private static class HeaderInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            request = request.newBuilder()
                    .header("User-Agent", "Android")
                    .build();
            okhttp3.Response response = chain.proceed(request);
            return response;
        }
    }

    public static void configService(String endpoint) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor())
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpoint).client(client).addConverterFactory(GsonConverterFactory.create())
                .build();

        setService(retrofit.create(ApiServices.class));
    }
    public static void setService(ApiServices service) {
        apiServices = service;
    }

    public static ApiServices getService() {
        if (apiServices == null) {
            configService(Constants.BASE_URL);
        }
        return apiServices;
    }


}