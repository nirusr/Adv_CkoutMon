package com.walmart.ckoutMonitor.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.walmart.ckoutMonitor.R;
import com.walmart.ckoutMonitor.model.Product;

import java.util.List;

/**
 * Created by sgovind on 11/18/15.
 */
public class ItemQtyAdapter extends RecyclerView.Adapter<ItemQtyAdapter.ViewHolder> {

    private List<Product> mProducts;

    public ItemQtyAdapter(List<Product> products) {
        this.mProducts = products;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvProdDesc;
        public TextView tvProdQty;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProdDesc = (TextView) itemView.findViewById(R.id.tvProdDesc);
            tvProdQty = (TextView) itemView.findViewById(R.id.tvProdQty);


        }
    }

    @Override
    public ItemQtyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemqtyView = inflater.inflate(R.layout.itemqty_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemqtyView);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context;
        context = holder.itemView.getContext();
        Product product = mProducts.get(position);
        TextView vTvProdDesc = holder.tvProdDesc;
        TextView vTvProdQty = holder.tvProdQty;
        vTvProdDesc.setText(product.getProdDesc());
        vTvProdQty.setText(product.getQuantity()+"");
        if ( position % 2 == 1) {
           holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.AliceBlue));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.Cornsilk));
        }
        //holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.highlight));

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/DroidSans.ttf");
        //vTvProdDesc.setTypeface(typeface);
        //vTvProdQty.setTypeface(typeface);


    }

    @Override
    public int getItemCount() {
        return mProducts.size();

    }

    public void clear() {
        mProducts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Product> products) {
        mProducts.addAll(products);
        notifyDataSetChanged();
    }


}
