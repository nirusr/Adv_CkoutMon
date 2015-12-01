package com.walmart.ckoutMonitor.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.walmart.ckoutMonitor.R;
import com.walmart.ckoutMonitor.adapters.ItemQtyAdapter;
import com.walmart.ckoutMonitor.model.Product;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemQtyFragment extends Fragment {
    //ArrayList<Product> products = new ArrayList<>();
    ItemQtyAdapter itemQtyAdapter ;
    List<Product> queryResults;
    TextView tvTitleProductDescription;
    TextView tvTitleProductQty;



   /* public ItemQtyFragment() {
        // Required empty public constructor
    }
*/
    public static ItemQtyFragment newInstance() {
        ItemQtyFragment itemQtyFragment = new ItemQtyFragment();
        return itemQtyFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        queryResults = new Select().from(Product.class).orderBy("Quantity DESC").limit(100).execute();
        itemQtyAdapter = new ItemQtyAdapter(queryResults);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_itemqty, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitleProductDescription = (TextView) view.findViewById(R.id.tvTitleProductDescription);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/DroidSans-Bold.ttf");
        //tvTitleProductDescription.setTypeface(typeface);
        tvTitleProductQty = (TextView) view.findViewById(R.id.tvTitleProductQty);
        //tvTitleProductQty.setTypeface(typeface);


        RecyclerView lvItems = (RecyclerView) view.findViewById(R.id.lvItems);
        Log.v("lv", lvItems.toString());
        lvItems.setAdapter(itemQtyAdapter);
        lvItems.setLayoutManager(new LinearLayoutManager(view.getContext()));

    }

    public void loadData(int count) {
        Log.v("Count=", count +"");
        itemQtyAdapter.clear();
        queryResults = new Select().from(Product.class).orderBy("Quantity DESC").limit(count).execute();
        itemQtyAdapter.addAll(queryResults);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadData(100);
    }
}
