package com.walmart.ckoutMonitor.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.activeandroid.query.Select;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walmart.ckoutMonitor.Helper.GeoCoderRestClient;
import com.walmart.ckoutMonitor.R;
import com.walmart.ckoutMonitor.model.Postcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Postcode> postcodes;
    public static final int MAPS_REQUEST_CODE= 900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //mapAllDeliveryPoints();
        /*Intent data  = new Intent();
        data.putExtra("OK", "OK");
        setResult(RESULT_OK, data);
        finish();*/

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mapAllDeliveryPoints();
                            }
                        });
                    }
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();

    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in asda store and move the camera
        LatLng asdaStore = new LatLng(53.7888009, -1.5405516);
        mMap.addMarker(new MarkerOptions().position(asdaStore).title("ASDA Edmonton Super Store\nDelivery Store"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(asdaStore)
                .zoom(10).build();
        //Zoom in and animate the camera.
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    private void mapAllDeliveryPoints() {
        postcodes = new Select().from(Postcode.class).orderBy("OrderNumber DESC").limit(15).execute();

        if (postcodes != null) {
            for (final Postcode postcode : postcodes) {


                RequestParams params = new RequestParams();
                params.put("address", postcode.getPostCode());
                final String city = postcode.getCity();

                GeoCoderRestClient.get("json", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        //Log.v("Response=", response.toString());
                        try {
                            JSONArray results = response.getJSONArray("results");
                            JSONObject geometry = results.getJSONObject(0).getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            String lat = location.getString("lat");
                            String lng = location.getString("lng");
                            Log.v("lat/lng=", String.format("%s/%s/%s", lat, lng, city));

                            double Dlat = Double.parseDouble(lat);
                            double Dlng = Double.parseDouble(lng);
                            LatLng delPoint = new LatLng(Dlat, Dlng);
                            mMap.addMarker(new MarkerOptions().position(delPoint).title("#Of Orders:"+ postcode.getOrdersPerPostCode()));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        try {
                            JSONObject jObj = new JSONObject(responseString);

                            Log.v("getLatLng Failed ", responseString.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                });
            }

        }

    }


}
