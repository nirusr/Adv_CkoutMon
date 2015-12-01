package com.walmart.ckoutMonitor.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.walmart.ckoutMonitor.R;
import com.walmart.ckoutMonitor.model.Store;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreDetailFragment extends Fragment {

    TextView tvStoreName;
    TextView tvStoreNumber;
    TextView tvStoreAddress;
    TextView tvStoreOrderValue;
    TextView tvStoreTotalOrder;
    public StoreDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_store_detail, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tvStoreName = (TextView) view.findViewById(R.id.tvStoreName);
        tvStoreNumber = (TextView) view.findViewById(R.id.tvStoreNumber);
        tvStoreAddress = (TextView) view.findViewById(R.id.tvStoreAddress);
        tvStoreTotalOrder = (TextView) view.findViewById(R.id.tvStoreTotalOrder);
        tvStoreOrderValue = (TextView) view.findViewById(R.id.tvStoreOrderValue);

        Store store = new Store();
        store = new Select().from(Store.class).where("StoreNumber = ? ", "4401").executeSingle();
        if (store != null) {
            tvStoreName.setText(store.getStoreName());
            tvStoreAddress.setText(store.getStoreAddress());
            tvStoreNumber.setText("#"+ store.getStoreNumber());

            tvStoreTotalOrder.setText(store.getTotalNumberOfOrders() + "");
            tvStoreOrderValue.setText(Math.round(store.getOrderTotal()) +"");


        }

    }
}
