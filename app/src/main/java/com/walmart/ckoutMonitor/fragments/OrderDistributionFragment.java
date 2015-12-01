package com.walmart.ckoutMonitor.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;
import com.walmart.ckoutMonitor.R;
import com.walmart.ckoutMonitor.adapters.PostCodeQtyAdapter;
import com.walmart.ckoutMonitor.model.Postcode;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDistributionFragment extends Fragment {

    List<Postcode> queryResultsPostCode;
    RecyclerView lvOrderDistribution;
    PostCodeQtyAdapter postCodeQtyAdapter ;

   /* public OrderDistributionFragment() {
        // Required empty public constructor
    }*/

    public static OrderDistributionFragment newInstance() {
        OrderDistributionFragment orderDistributionFragment = new OrderDistributionFragment();
        return orderDistributionFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queryResultsPostCode = new Select().from(Postcode.class).orderBy("DeliveryCity ASC").limit(100).execute();
        postCodeQtyAdapter = new PostCodeQtyAdapter(queryResultsPostCode);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_distribution, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvOrderDistribution = (RecyclerView) view.findViewById(R.id.lvOrderDistribution);
        lvOrderDistribution.setAdapter(postCodeQtyAdapter);
        lvOrderDistribution.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void loadData(int count) {
        postCodeQtyAdapter.clear();
        queryResultsPostCode = new Select().from(Postcode.class).orderBy("DeliveryCity ASC").limit(count).execute();

        postCodeQtyAdapter.addAll(queryResultsPostCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadData(100);
    }
}
