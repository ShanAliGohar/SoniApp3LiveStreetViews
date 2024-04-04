package com.live.streetview.navigation.earthmap.compass.map.Wevcamapi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebcamRetrofitInstance {
    public static final String urlOpenWeather = "https://api.windy.com/api/webcams/v2/";
    private static Retrofit retrofitWeatherApi = null;

    public static Retrofit getRetrofitInstance() {
        if (retrofitWeatherApi == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.writeTimeout(30, TimeUnit.SECONDS);
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
            retrofitWeatherApi = new Retrofit.Builder()
                    .baseUrl(urlOpenWeather)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build();

        }
        return retrofitWeatherApi;
    }



}
