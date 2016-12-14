package com.valevich.materikemployee.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.valevich.materikemployee.R;
import com.valevich.materikemployee.network.model.response.ProductItem;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderProductsAdapter extends RecyclerView.Adapter<OrderProductsAdapter.ProductHolder>{

    private ProductItem[] items;

    private Context context;

    public OrderProductsAdapter(ProductItem[] items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_product_item, parent,
                false);
        return new ProductHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        holder.bind(items[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    class ProductHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_image)
        ImageView productImage;

        @BindView(R.id.product_title)
        TextView productName;

        @BindView(R.id.amount_label)
        TextView amountLabel;

        ProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(ProductItem item) {
            Glide.with(context)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .crossFade()
                    .into(productImage);
            productName.setText(item.getName());
            amountLabel.setText(String.format(Locale.getDefault(), "%d %s", item.getOrderAmount(), item.getMetrics()));
        }
    }
}
