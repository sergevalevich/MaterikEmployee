package com.valevich.materikemployee.ui.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.valevich.materikemployee.R;
import com.valevich.materikemployee.network.model.response.ProductItem;
import com.valevich.materikemployee.network.model.response.StockModel;
import com.valevich.materikemployee.ui.activities.ProductsActivity_;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockHolder> {

    private List<StockModel> stocks;

    private Context context;

    public StockAdapter(List<StockModel> stocks, Context context) {
        this.stocks = stocks;
        this.context = context;
    }

    @Override
    public StockHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_item, parent,
                false);
        return new StockHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StockHolder holder, int position) {
        holder.bind(stocks.get(position));
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    class StockHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.stock_desc)
        TextView stockDescriptionLabel;

        StockHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(view -> showProducts());
        }

        void bind(StockModel stockModel) {
            stockDescriptionLabel.setText(String.format(
                    Locale.getDefault(),
                    "#%d %s (%d)",
                    stockModel.getId(),
                    stockModel.getAddress(),
                    stockModel.getProducts() == null ? 0 : stockModel.getProducts().size()
            ));
        }

        private void showProducts() {
            StockModel stockModel = stocks.get(getAdapterPosition());
            List<ProductItem> products = stockModel.getProducts();
            if(products != null && !products.isEmpty()) {
                ProductItem[] prs = new ProductItem[products.size()];
                prs = products.toArray(prs);
                ProductsActivity_.intent(context)
                        .parcelableProducts(prs)
                        .category(stockModel.getAddress())
                        .start();
            } else {
                Toast.makeText(context,"Нет товаров на складе",Toast.LENGTH_LONG).show();
            }
        }
    }
}
