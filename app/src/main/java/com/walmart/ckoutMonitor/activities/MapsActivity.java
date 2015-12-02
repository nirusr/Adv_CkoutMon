package com.walmart.ckoutMonitor.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.BounceInterpolator;

import com.activeandroid.query.Select;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Postcode> postcodes;
    public static final int MAPS_REQUEST_CODE= 900;
    int iterations = 0;

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


        /*
        //getting exception Synchronous ResponseHandler used in AsyncHttpClient. You should create your response handler in a looper thread or use SyncHttpClient instead
        final ScheduledThreadPoolExecutor sch = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);

        Runnable periodicMapRefresh = new Runnable() {
            @Override
            public void run() {
                try {
                    if ( iterations <= 10) {
                        Thread.sleep(5*1000);
                        mapAllDeliveryPoints();
                        iterations++;


                    } else {
                        sch.shutdown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        sch.scheduleWithFixedDelay(periodicMapRefresh, 0, 1, TimeUnit.SECONDS);

*/

        Thread t = new Thread() {
            @Override
            public void run() {
                try {


                        while (!isInterrupted()) {
                            Log.v("#of Map Refresh=", iterations + "");
                            if (iterations <= 5) { //for demo purpose
                                Thread.sleep(7000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mapAllDeliveryPoints();
                                        iterations++;
                                    }
                                });
                            } else {
                                Thread.currentThread().interrupt();
                            }
                        }


                } catch (InterruptedException e) {
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
        BitmapDescriptor asdaMarker = BitmapDescriptorFactory.fromResource(R.mipmap.ic_asda);
        mMap.addMarker(new MarkerOptions().position(asdaStore).title("ASDA Edmonton Super Store\nDelivery Store").icon(asdaMarker));
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
                        Log.v("Response=", response.toString());
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
                            BitmapDescriptor defaultMarker =
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);

                            BitmapDescriptor customMarker = BitmapDescriptorFactory.fromResource(R.mipmap.house);


                            //Marker marker = mMap.addMarker(new MarkerOptions().position(delPoint).title("#Of Orders:"+ postcode.getOrdersPerPostCode()));
                            mMap.addMarker(new MarkerOptions().position(delPoint).title("#Of Orders:" + postcode.getOrdersPerPostCode()).icon(customMarker));
                            //dropPinEffect(marker);




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

    private void dropPinEffect(final Marker marker) {
        // Handler allows us to repeat a code block after a specified delay
        final android.os.Handler handler = new android.os.Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        // Use the bounce interpolator
        final android.view.animation.Interpolator interpolator =
                new BounceInterpolator();

        // Animate marker with a bounce updating its position every 15ms
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                // Calculate t for bounce based on elapsed time
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                // Set the anchor
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post this event again 15ms from now.
                    handler.postDelayed(this, 15);
                } else { // done elapsing, show window
                    marker.showInfoWindow();
                }
            }
        });
    }


}
