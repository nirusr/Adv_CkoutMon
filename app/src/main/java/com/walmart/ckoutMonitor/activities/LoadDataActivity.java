package com.walmart.ckoutMonitor.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.activeandroid.Model;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.walmart.ckoutMonitor.Helper.GeoCoderRestClient;
import com.walmart.ckoutMonitor.R;
import com.walmart.ckoutMonitor.model.Postcode;
import com.walmart.ckoutMonitor.model.Product;
import com.walmart.ckoutMonitor.model.Slot;
import com.walmart.ckoutMonitor.model.Store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.acl.LastOwnerException;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class LoadDataActivity extends AppCompatActivity {
    HashMap<String, String> gpsLocation;
    HashMap<String, String> delLocation;

    public static final String KEY_LAT = "LAT";
    public static final String KEY_LNG = "LNG";

    public String assetFilename = "";
    public static final String FILENAME = "FILENAME";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_data);

       /* new Delete().from(Product.class).execute();
        new Delete().from(Postcode.class).execute();
        new Delete().from(Slot.class).execute();
        new Delete().from(Store.class).execute();*/
        readAssetDataFile();
        delLocation = new HashMap<>();

        Intent intent = new Intent();
        intent.putExtra("Results", "OK");
        setResult(RESULT_OK, intent);
        finish();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //1. Read JSON input data from Asset directory
    public void readAssetDataFile() {
        assetFilename = getIntent().getStringExtra(FILENAME);
        if (assetFilename != null) {
            try {
                InputStream is = getAssets().open(assetFilename);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String json = new String(buffer, "UTF-8");
                //Log.v("ORDER JSON=", json);
                parseJSON(json);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    //2. Parse JSON
    public void parseJSON(String inputStr) {

        try {

            JSONObject jsonObject = new JSONObject(inputStr);
            JSONObject orderObject = jsonObject.getJSONObject("TSOrder");
            //Log.v("Order=", orderObject.toString());
            String storeNo = orderObject.getJSONObject("RecordInfo").getString("StoreNo");
            String orderTotal = orderObject.getJSONObject("OrderInfo").getString("OrderTotal");
            String orderNumber = orderObject.getJSONObject("OrderInfo").getString("OrderNumber");
            String scheduleTimeSlot = orderObject.getJSONObject("OrderInfo").getString("ScheduleTimeSlot");
            final String location = orderObject.getJSONObject("CustomerInfo").getJSONObject("CustomerAddress").getString("PostCode");
            String city = orderObject.getJSONObject("CustomerInfo").getJSONObject("CustomerAddress").getString("City");
            Log.v("OrderData=>", String.format("StoreNo=%s  OrderTotal=%s OrderNumber=%s Slot=%s Postcode=%s\n",
                    storeNo, orderTotal, orderNumber, scheduleTimeSlot, location));


            //Store Details

            Store store = new Store();
            store.setRemoteId(Long.parseLong(orderNumber));
            store.setStoreNumber(storeNo.trim());
            store.setStoreName("ASDA Edmonton Super Store");
            store.setStoreAddress("1 West Mall,Edmonton,N9 0AL, United Kingdom");
            //store.setOrderTotal(Float.parseFloat(orderTotal));
            store.setDeliveryDateStr("2015-11-12");
            store.setTotalNumberOfOrders(1);


            Store existingStore = Store.getStoreByStoreNo(store);

            if (existingStore != null) {
                float sumOrderTotal = existingStore.getOrderTotal() + Float.parseFloat(orderTotal);
                int totalNumOrder = existingStore.getTotalNumberOfOrders() + store.getTotalNumberOfOrders();

                store.setOrderTotal(sumOrderTotal);
                existingStore.setTotalNumberOfOrders(totalNumOrder);
                existingStore.setOrderTotal(sumOrderTotal);
                existingStore.save();

            } else {
                store.setOrderTotal(Float.parseFloat(orderTotal));
                store.save();

            }


            //Insert PostCode
            Postcode postCode = new Postcode();
            postCode.setPostCode(location);
         /*   if (gpsLocation != null) {
                postCode.setGpsLat(gpsLocation.get(KEY_LAT));
                postCode.setGpsLng(gpsLocation.get(KEY_LNG));
            } else {
                postCode.setGpsLat("");
                postCode.setGpsLng("");

            }
*/


            postCode.setOrdersPerPostCode(1);
            postCode.setOrderNumber(orderNumber);
            postCode.setCity(city);
            postCode.setGpsLat("");
            postCode.setGpsLng("");
            //Log.v("PostCode=", postCode.getPostCode());

            Postcode existingPostCode = Postcode.getPostCodebyPostcd(postCode);
            if (existingPostCode != null) {
                existingPostCode.setOrdersPerPostCode(existingPostCode.getOrdersPerPostCode() + 1);
                existingPostCode.save();
            } else {
                postCode.save();

            }


            //update lat and lng
  /*          getJSONObj(new OnJSONResponseCallback() {
                @Override

                public void onJSONResponse(boolean success, JSONObject response) {
                    Log.v("Google Res=", response.toString());

                    if (success) {

                        try {
                            String lng = response.getString("lng");
                            String lat = response.getString("lat");
                            Log.v("lat/lng=", String.format("%s/%s", lat, lng));
                            Log.v("location=", location);
                            Postcode updPostCode = new Postcode();
                            updPostCode = Postcode.selectField("Postcd", location);
                            if ( updPostCode != null) {
                                updPostCode.setGpsLat(lat);
                                updPostCode.setGpsLng(lng);
                                updPostCode.save();
                                Postcode testUpdPcd = Postcode.selectField("Postcd", location);
                                Log.v("updpostcode=", testUpdPcd.getGpsLat());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
            }, location);

            Postcode testUpdPcd = Postcode.selectField("Postcd", location);
*/
            //Insert Slot
            Slot slot = new Slot();
            slot.setOrdersPerSlot(1);
            slot.setSlotTime(scheduleTimeSlot);
            slot.setOrderNumber(orderNumber);
            slot.setValuePerSlot(Float.parseFloat(orderTotal));
            Log.v("Slot=", slot.getSlotTime());

            Slot existingSlot = Slot.getSlotbySlotTime(slot);
            if (existingSlot != null) {
                existingSlot.setOrdersPerSlot(existingSlot.getOrdersPerSlot() + 1);
                float orderValue = existingSlot.getValuePerSlot() + (Float.parseFloat(orderTotal));
                existingSlot.setValuePerSlot(orderValue);
                existingSlot.save();

            } else {
                slot.save();
            }

            //Product Items
            JSONArray itemList = orderObject.getJSONObject("OrderInfo").getJSONArray("OrderLine");
            //Log.v("Item Array=",  itemArray.toString());

            for (int i = 0; i < itemList.length(); i++) {
                JSONObject itemInfo = itemList.getJSONObject(i);
                //Log.v("ProductId=", itemInfo.getString("ProdID"));
                //Create OrderLine
                String itemId = itemInfo.getString("ProdID");
                String itemUPCDescription = itemInfo.getString("UPCDescription");
                String itemQuantity = itemInfo.getString("Quantity");


                //Insert data into Product
                Product product = new Product();
                product.setProdId(itemId);
                int quantity = Integer.parseInt(itemQuantity);
                product.setQuantity(quantity);
                product.setProdDesc(itemUPCDescription);
                product.setOrderNumber(orderNumber);

                //check duplicate product
                Product existingProduct = new Product();
                existingProduct = Product.getProdByProdID(product);
                if (existingProduct != null) {
                    int qty = existingProduct.getQuantity() + quantity;
                    product.setQuantity(qty);
                    existingProduct.save();
                    // Log.v("Duplicate Prod Id Qty=", itemId + ":" + qty + "");

                } else {

                    //Log.v("Prod Qty=", itemId +":" + quantity + "");
                    product.save();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "LoadData Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.walmart.ckoutMonitor.activities/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "LoadData Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.walmart.ckoutMonitor.activities/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public interface OnJSONResponseCallback {
        public void onJSONResponse(boolean success, JSONObject response);
    }

    public void getJSONObj(final OnJSONResponseCallback callback, String postcode) {


        RequestParams params = new RequestParams();
        params.put("address", postcode);


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
                    //Log.v("lat/lng=", String.format("%s/%s", lat, lng));
                    callback.onJSONResponse(true, location);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                try {
                    JSONObject jObj = new JSONObject(responseString);
                    callback.onJSONResponse(false, jObj);
                    Log.v("getLatLng Failed ", responseString.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });

    }


}
