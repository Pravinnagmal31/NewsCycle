package com.pravin.newscyclejava.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient
{
    private static final String URL = "https://newsapi.org/v2/";
    public static Retrofit retrofit = null;

    public static APIInterface getApiClient()
    {
        if(retrofit==null)
        {
            retrofit = new Retrofit.Builder().
                    baseUrl(URL).
                    addConverterFactory(GsonConverterFactory.create()).
                    build();
        }
        return retrofit.create(APIInterface.class);
    }

}
