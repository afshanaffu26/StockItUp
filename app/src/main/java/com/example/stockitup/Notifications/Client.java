package com.example.stockitup.Notifications;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class deals with retrofit
 */
public class Client {
    private static Retrofit retrofit=null;

    /**
     * This method returns Retrofit instance
     * @param url Base URL
     * @return Retrofit instance
     */
    public static Retrofit getClient(String url)
    {
        if(retrofit==null)
        {
            retrofit=new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return  retrofit;
    }
}
