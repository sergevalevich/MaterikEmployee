package com.valevich.materikemployee.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.valevich.materikemployee.R;
import com.valevich.materikemployee.network.model.response.CatalogItem;
import com.valevich.materikemployee.network.model.response.ProductItem;
import com.valevich.materikemployee.ui.activities.ProductsActivity_;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogHolder> {

    private List<CatalogItem> categories;

    private Context context;

    public CatalogAdapter(List<CatalogItem> items, Context context) {
        categories = items;
        this.context = context;
    }

    @Override
    public CatalogAdapter.CatalogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_item, parent,
                false);
        return new CatalogHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CatalogHolder holder, int position) {
        holder.bind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CatalogHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.category_image)
        ImageView categoryImage;

        @BindView(R.id.category_name)
        TextView categoryName;


        CatalogHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> showProducts());
        }

        private void showProducts() {
            CatalogItem catalogItem = categories.get(getAdapterPosition());
            List<ProductItem> products = catalogItem.getProducts();
            if(products != null && !products.isEmpty()) {
                ProductItem[] prs = new ProductItem[products.size()];
                prs = products.toArray(prs);
                ProductsActivity_.intent(context)
                        .parcelableProducts(prs)
                        .category(catalogItem.getName())
                        .start();
            } else {
                Toast.makeText(context,"Нет товаров в категории",Toast.LENGTH_LONG).show();
            }
        }

        private void bind(CatalogItem category) {
            categoryName.setText(category.getName());
            Glide.with(context)
                    .load(category.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .crossFade()
                    .into(categoryImage);
        }

    }
}
