package com.live.streetview.navigation.earthmap.compass.map.Wevcamapi;



import com.live.streetview.navigation.earthmap.compass.map.Webcam.WebCamModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebCamApi {

    @GET("list/country={country}/limit={limit}")
    public Call<WebCamModel> getWebCamFromApi(@Path("country") String country,
                                              @Path("limit")  int limit,
                                              @Query("key")   String key,
                                              @Query("show") String show);

}

/*interface WebCamApi {
    @GET("list/country={country}/limit={limit}")
    fun getWebCamFromApi(@Path("country") country: String,
                         @Path("limit")limit: Int,
                         @Query("key")key: String,
                         @Query("show")show: String): Call<WebCameListModel>*/



