package com.pravin.newscyclejava.API;


import com.pravin.newscyclejava.API.Models.Root;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {




    @GET("top-headlines")
    Call<Root> getNews(
            @Query("country") String country,
            @Query("category") String category,
            @Query("apiKey") String apiKey
    );
}
