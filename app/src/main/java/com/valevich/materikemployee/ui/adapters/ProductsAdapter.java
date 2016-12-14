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
import com.valevich.materikemployee.ui.activities.ProductDetailsActivity_;
import com.valevich.materikemployee.util.ConstantsManager;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductHolder>{

    private ProductItem[] products;

    private Context context;

    public ProductsAdapter(ProductItem[] products, Context context) {
        this.products = products;
        this.context = context;
    }

    @Override
    public ProductsAdapter.ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent,
                false);
        return new ProductsAdapter.ProductHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductsAdapter.ProductHolder holder, int position) {
        holder.bind(products[position]);
    }

    @Override
    public int getItemCount() {
        return products.length;
    }

    class ProductHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_image)
        ImageView productImage;

        @BindView(R.id.product_title)
        TextView productName;

        @BindView(R.id.product_price)
        TextView productPrice;


        ProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> showDetails());
        }

        private void showDetails() {
            ProductDetailsActivity_.intent(context)
                    .parcelableProduct(products[getAdapterPosition()])
                    .start();
        }

        private void bind(ProductItem product) {
            Glide.with(context)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .crossFade()
                    .into(productImage);
            productName.setText(product.getName());
            productPrice.setText(String.format(
                    Locale.getDefault(),
                    ConstantsManager.PRICE_FORMAT,
                    product.getPrice(),
                    product.getMetrics()));
        }

    }
}
