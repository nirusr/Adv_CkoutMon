package com.walmart.ckoutMonitor.interfaces;

import com.walmart.ckoutMonitor.utils.GoogleLatLng;


import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by sgovind on 1/29/16.
 */
public interface ILatLngService {
    @GET("json")
    Observable<GoogleLatLng> getLatLng(@Query("address") String postcode);

}
