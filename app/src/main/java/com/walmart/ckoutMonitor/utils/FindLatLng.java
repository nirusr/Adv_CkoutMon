package com.walmart.ckoutMonitor.utils;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

/**
 * Created by sgovind on 1/29/16.
 */
public class FindLatLng extends AsyncTask<String, Void, Boolean> {
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/";

    @Override
    protected Boolean doInBackground(String... postcodes) {
        String postcode = postcodes[0];
        OkHttpClient client = new OkHttpClient();
        //set logging level
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);


        return true;
    }
}
