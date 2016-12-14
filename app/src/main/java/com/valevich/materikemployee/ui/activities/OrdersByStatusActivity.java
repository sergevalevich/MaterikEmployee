package com.valevich.materikemployee.ui.activities;

import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.valevich.materikemployee.R;
import com.valevich.materikemployee.network.model.response.OrderItem;
import com.valevich.materikemployee.ui.adapters.OrdersAdapter;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_orders)
public class OrdersByStatusActivity extends AppCompatActivity {

    @ViewById(R.id.order_list)
    RecyclerView orderList;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @StringRes(R.string.order_title)
    String title;

    @Extra
    Parcelable[] parcelableOrders;

    private OrdersAdapter ordersAdapter;

    @AfterExtras
    void setUpAdapter() {
        if(parcelableOrders != null) {
            List<OrderItem> orders = new ArrayList<>(parcelableOrders.length);
            for (Parcelable parcelableOrder : parcelableOrders) {
                orders.add((OrderItem) parcelableOrder);
            }
            ordersAdapter = new OrdersAdapter(orders,this);
        }
    }

    @AfterViews
    void setUpViews() {
        setUpToolbar();
        setUpList();
    }

    private void setUpList() {
        orderList.setLayoutManager(new LinearLayoutManager(this));
        orderList.setAdapter(ordersAdapter);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            setTitle(title);
        }
    }
}
