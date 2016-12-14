package com.valevich.materikemployee.ui.activities;

import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.valevich.materikemployee.R;
import com.valevich.materikemployee.network.model.response.ProductItem;
import com.valevich.materikemployee.ui.adapters.OrderProductsAdapter;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;


@EActivity(R.layout.activity_order_products)
public class OrderProductsActivity extends AppCompatActivity {

    @StringRes(R.string.order_products_title)
    String title;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.products_list)
    RecyclerView recyclerView;

    @Extra
    Parcelable[] parcelableProducts;

    private OrderProductsAdapter productsAdapter;

    @AfterExtras
    void setUpAdapter() {
        if(parcelableProducts != null) {
            ProductItem[] products = new ProductItem[parcelableProducts.length];
            for (int i = 0; i < parcelableProducts.length; i++) {
                products[i] = (ProductItem) parcelableProducts[i];
            }
            productsAdapter = new OrderProductsAdapter(products,this);
        }
    }

    @AfterViews
    void setUpViews() {
        setUpToolbar();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productsAdapter);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            setTitle(title);
        }
    }
}
