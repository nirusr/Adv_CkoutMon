package com.walmart.ckoutMonitor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.walmart.ckoutMonitor.R;
import com.walmart.ckoutMonitor.model.Postcode;

import java.util.List;

/**
 * Created by sgovind on 11/18/15.
 */
public class PostCodeQtyAdapter extends RecyclerView.Adapter<PostCodeQtyAdapter.ViewHolder> {


    private List<Postcode> mPostcode;

    public PostCodeQtyAdapter(List<Postcode> mPostcode) {
        this.mPostcode = mPostcode;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPostCode;
        public TextView tvPostCodeCity;
        public TextView tvPostCodeQty;
        public ViewHolder(View itemView) {
            super(itemView);
            tvPostCode = (TextView) itemView.findViewById(R.id.tvPostCode);
            tvPostCodeCity = (TextView) itemView.findViewById(R.id.tvPostCodeCity);
            tvPostCodeQty  = (TextView) itemView.findViewById(R.id.tvPostCodeQty);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View postCodeList = inflater.inflate(R.layout.postcode_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(postCodeList);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Context context = holder.itemView.getContext();
        Postcode postcode = mPostcode.get(position);
        holder.tvPostCode.setText(postcode.getPostCode());

        String tCity  = postcode.getCity();
        //String upperString = myString.substring(0,1).toUpperCase() + myString.substring(1);
        String upCity = tCity.substring(0,1).toUpperCase() + tCity.substring(1).toLowerCase();
        holder.tvPostCodeCity.setText(upCity );
        holder.tvPostCodeQty.setText(postcode.getOrdersPerPostCode() + "");

        if ( position % 2 == 1) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.AliceBlue));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.Cornsilk));
        }

        //holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.highlight));

    }

    @Override
    public int getItemCount() {
        return mPostcode.size();
    }

    public void clear() {
        mPostcode.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Postcode> postcodes) {
        mPostcode.addAll(postcodes);
        notifyDataSetChanged();

    }
}
