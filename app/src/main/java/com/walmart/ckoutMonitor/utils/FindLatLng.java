package com.walmart.ckoutMonitor.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.walmart.ckoutMonitor.interfaces.ILatLngService;
import com.walmart.ckoutMonitor.model.Postcode;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sgovind on 1/29/16.
 */
public class FindLatLng extends AsyncTask<String, Void, Boolean> {
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/";

    @Override
    protected Boolean doInBackground(String... params) {
        final String delPostcode = params[0];
        final String delOrderNumber = params[1];
        final String delCity = params[2];

        Postcode existingPostCode = Postcode.selectField("Postcd", delPostcode);
        if (existingPostCode != null) {
            existingPostCode.setOrdersPerPostCode(existingPostCode.getOrdersPerPostCode() + 1 );
            existingPostCode.save();
        } else {
           OkHttpClient client = new OkHttpClient();
            //set logging level
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.interceptors().add(interceptor);

            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).
                    addCallAdapterFactory(RxJavaCallAdapterFactory.create()).client(client).build();
            ILatLngService apiService = retrofit.create(ILatLngService.class);
            Observable<GoogleLatLng> call = apiService.getLatLng(delPostcode);
            call.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread()).subscribe(
                    new Subscriber<GoogleLatLng>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(GoogleLatLng googleLatLng) {
                            double lat = googleLatLng.getResults().get(0).getGeometry().getLocation().getLat();
                            double lng = googleLatLng.getResults().get(0).getGeometry().getLocation().getLng();

                            Log.v("Lat for ", delPostcode + "==" + lat);
                            Log.v("lng for ", delPostcode + "==" + lng);
                            //Save Postcode Data
                            Postcode postcodeObj = new Postcode();
                            postcodeObj.setPostCode(delPostcode);
                            postcodeObj.setOrdersPerPostCode(1);
                            postcodeObj.setOrderNumber(delOrderNumber);
                            postcodeObj.setCity(delCity);
                            postcodeObj.setGpsLat(lat + "");
                            postcodeObj.setGpsLng(lng + "");
                            postcodeObj.save();



                        }
                    }
            );

        }

        return true;
    }
}
