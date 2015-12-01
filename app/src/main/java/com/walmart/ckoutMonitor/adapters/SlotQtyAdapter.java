package com.walmart.ckoutMonitor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.walmart.ckoutMonitor.R;
import com.walmart.ckoutMonitor.model.Slot;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by sgovind on 11/23/15.
 */
public class SlotQtyAdapter extends RecyclerView.Adapter<SlotQtyAdapter.ViewHolder> {
    public List<Slot> mSlots;

    public SlotQtyAdapter(List<Slot> mSlots) {
        this.mSlots = mSlots;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSlotWindow;
        public TextView tvSlotQty;
        public TextView tvSlotOrderValue;


        public ViewHolder(View itemView) {
            super(itemView);
            tvSlotWindow = (TextView) itemView.findViewById(R.id.tvSlotWindow);
            tvSlotQty = (TextView) itemView.findViewById(R.id.tvSlotQty);
            tvSlotOrderValue = (TextView) itemView.findViewById(R.id.tvSlotOrderValue);


        }
    }

    @Override
    public SlotQtyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View slotListView = inflater.inflate(R.layout.slot_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(slotListView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(SlotQtyAdapter.ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Slot slot = mSlots.get(position);
        holder.tvSlotWindow.setText(slot.getSlotTime());
        String ordersPerSlot = String.valueOf(slot.getOrdersPerSlot());
        holder.tvSlotQty.setText(ordersPerSlot);


        String currencySymbol = "£";
        //float orderTotal = Math.round(slot.getValuePerSlot());

        float orderTotal = precision(2, slot.getValuePerSlot());
        String orderTotalSep = String.format("%,.2f", orderTotal);
        String displayOrderTotal = currencySymbol + orderTotal;


        holder.tvSlotOrderValue.setText(displayOrderTotal);
        if ( position % 2 == 1 ) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.AliceBlue));

        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.Cornsilk));
        }
        //holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.highlight));

    }

    @Override
    public int getItemCount() {
        return mSlots.size();
    }

    public void clear() {
        mSlots.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Slot> slots) {
        mSlots.clear();
        mSlots.addAll(slots);
        notifyDataSetChanged();

    }

    public static Float precision(int decimalPlace, Float d) {

        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
