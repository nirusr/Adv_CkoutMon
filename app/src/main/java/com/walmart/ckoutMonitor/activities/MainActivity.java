package com.walmart.ckoutMonitor.activities;

import android.content.Intent;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.astuetz.PagerSlidingTabStrip;


import com.walmart.ckoutMonitor.R;
import com.walmart.ckoutMonitor.adapters.OPMPageFragmentAdapter;
import com.walmart.ckoutMonitor.model.Postcode;
import com.walmart.ckoutMonitor.model.Product;
import com.walmart.ckoutMonitor.model.Slot;
import com.walmart.ckoutMonitor.model.Store;


import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;




public class MainActivity extends AppCompatActivity {
    TextView toolbar_StoreNumber;
    TextView toolbar_StoreTotalValue;
    TextView toolbar_StoreTotalOrders;
    TextView toolbar_StoreAddress;
    int totalFileCount = 0;
    int currentFileIndex = 0;
    String[] assetFiles = null;
    OPMPageFragmentAdapter adapterViewPager ;
    ViewPager vpPager;
    PagerSlidingTabStrip tabStrip;

    final static DateFormat fmt = DateFormat.getTimeInstance(DateFormat.LONG);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mainLayout = (ViewGroup) findViewById(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        toolbar_StoreNumber = (TextView) toolbar.findViewById(R.id.toolbar_StoreNumber);
        toolbar_StoreTotalValue = (TextView) toolbar.findViewById(R.id.toolbar_StoreTotalValue);
        toolbar_StoreTotalOrders = (TextView) toolbar.findViewById(R.id.toolbar_StoreTotalOrders);
        toolbar_StoreAddress = (TextView) toolbar.findViewById(R.id.toolbar_StoreAddress);
        vpPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new OPMPageFragmentAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);

        //
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String dt = android.text.format.DateFormat.format("MM/dd/yy", new java.util.Date()).toString();
        Log.v("date=", dt);


        cleanUpDatabase();
        runScheduledTask();
       /* Intent intent  = new Intent(this, MapsActivity.class);
        startActivity(intent);*/
    }

    public void cleanUpDatabase() {
        //Cleanup DB
        new Delete().from(Product.class).execute();
        new Delete().from(Postcode.class).execute();
        new Delete().from(Slot.class).execute();
        new Delete().from(Store.class).execute();



    }

    public void runScheduledTask() {
        try {
            assetFiles = getAssets().list("data");
            totalFileCount = assetFiles.length;

        } catch (IOException e) {
            e.printStackTrace();
        }

        final  ScheduledThreadPoolExecutor sch = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);

        Runnable periodicDataLoad = new Runnable() {
            String filename;
            @Override
            public void run() {
                try {

                    if ( currentFileIndex < totalFileCount) {
                        filename = "data/" + assetFiles[currentFileIndex];
                        System.out.println(filename);
                        System.out.println("\t delayTask Execution Time: "
                                + fmt.format(new Date()));
                        Thread.sleep(5 * 1000);
                        System.out.println("\t delayTask End Time: "
                                + fmt.format(new Date()));
                        loadData(filename);
                        currentFileIndex++;
                    }
                    if (currentFileIndex >= totalFileCount) {

                        //sch.awaitTermination(30, TimeUnit.SECONDS);
                        sch.shutdown();
                        Log.v("Sch coming down after ", filename);

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        };
        sch.scheduleWithFixedDelay(periodicDataLoad, 0, 1, TimeUnit.SECONDS);

    }

    public void loadData(String filename) {
        Log.v(" Call Load filename ==>", filename);
        Intent intent = new Intent(this, LoadDataActivity.class);
        intent.putExtra(LoadDataActivity.FILENAME, filename);
        startActivityForResult(intent, 100);


    }

    private void populateToolbar() {

        Store store = new Select().from(Store.class).where("StoreNumber = ? ", "4401").executeSingle();
        if (store != null) {
            toolbar_StoreNumber.setText(store.getStoreNumber());
            String currencySymbol = "£";
            float orderTotal = precision(2, store.getOrderTotal());
            String orderTotalSep = String.format("%,.2f", orderTotal);
            //float orderTotal = Math.round(store.getOrderTotal());
            String displayOrderTotal = currencySymbol + orderTotalSep;
            toolbar_StoreTotalValue.setText(displayOrderTotal);
            toolbar_StoreTotalOrders.setText(store.getTotalNumberOfOrders() + "");
            toolbar_StoreAddress.setText(store.getStoreName());

            Random random = new Random();

            int R = (int) (Math.random() * 256);
            int G = (int) (Math.random() * 256);
            int B = (int) (Math.random() * 256);
            toolbar_StoreTotalValue.setTextColor(Color.rgb(R, G, B));
            toolbar_StoreTotalOrders.setTextColor(Color.rgb(R, G, B));


        }
    }

    public static Float precision(int decimalPlace, Float d) {

        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        populateToolbar();


        Fragment itemPage = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 0);
        if ( itemPage != null) {
            itemPage.onActivityResult(requestCode, resultCode, data);

        } else {
            Toast.makeText(getApplicationContext(), "Rendering issue in TOP ITEMS", Toast.LENGTH_SHORT);
        }




        Fragment orderDistPage = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 1);
        if ( orderDistPage != null) {
            orderDistPage.onActivityResult(requestCode, resultCode, data);

        } else {
            Toast.makeText(getApplicationContext(), "Rendering issue in DEL LOC", Toast.LENGTH_SHORT);
        }



        Fragment slotDistPage = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 2);
        if ( slotDistPage != null) {
            slotDistPage.onActivityResult(requestCode, resultCode, data);
        } else {
            Toast.makeText(getApplicationContext(), "Rendering issue in DEL TIME", Toast.LENGTH_SHORT);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        // this.onCreate(null);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }



    public void btnViewMapClicked(View v) {
        Log.v("Button Clicked", v.findViewById(R.id.btnViewMap) + "");
        Intent intent  = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, MapsActivity.MAPS_REQUEST_CODE);
        //startActivity(intent);

    }


}


