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
import com.walmart.ckoutMonitor.adapters.SlotQtyAdapter;
import com.walmart.ckoutMonitor.model.Slot;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ODBySlotFragment extends Fragment {

    List<Slot> queryResults;
    SlotQtyAdapter slotQtyAdapter;
    RecyclerView lvSlots;

    public ODBySlotFragment() {
        // Required empty public constructor
    }

    public static ODBySlotFragment newInstance() {
        return new ODBySlotFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queryResults = new Select().from(Slot.class).orderBy("OrdersPerSlot DESC").limit(100).execute();
        slotQtyAdapter = new SlotQtyAdapter(queryResults);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_odby_slot, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvSlots = (RecyclerView) view.findViewById(R.id.lvSlots);
        lvSlots.setAdapter(slotQtyAdapter);
        lvSlots.setLayoutManager(new LinearLayoutManager(view.getContext()));


    }

    public void loadData(int count) {
        slotQtyAdapter.clear();
        queryResults = new Select().from(Slot.class).orderBy("OrdersPerSlot DESC").limit(count).execute();
        slotQtyAdapter.addAll(queryResults);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadData(100);
    }
}
