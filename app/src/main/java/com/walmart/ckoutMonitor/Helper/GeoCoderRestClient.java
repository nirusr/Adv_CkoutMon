package com.walmart.ckoutMonitor.Helper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.*;

/**
 * Created by sgovind on 11/24/15.
 */
public class GeoCoderRestClient {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        System.out.println("url=" + getAbsoluteUrl(url));
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    public static String getAbsoluteUrl ( String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
