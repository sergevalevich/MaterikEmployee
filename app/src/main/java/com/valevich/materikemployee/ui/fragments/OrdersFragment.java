package com.valevich.materikemployee.ui.fragments;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.valevich.materikemployee.MaterikEmployeeApplication_;
import com.valevich.materikemployee.R;
import com.valevich.materikemployee.network.RestService;
import com.valevich.materikemployee.network.model.request.Credentials;
import com.valevich.materikemployee.network.model.response.StatusItem;
import com.valevich.materikemployee.ui.adapters.StatusAdapter;
import com.valevich.materikemployee.util.NetworkStateChecker;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.fragment_orders)
public class OrdersFragment extends Fragment {

    @ViewById(R.id.root)
    LinearLayout rootView;

    @ViewById(R.id.order_list)
    RecyclerView recyclerView;

    @ViewById(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @StringRes(R.string.network_unavailable)
    String networkUnavailableMessage;

    @Bean
    RestService restService;

    @Bean
    NetworkStateChecker networkStateChecker;

    private Subscription subscription;

    @AfterViews
    void onViewsReady() {
        setUpRecyclerView();
        setUpSwipe();
        toggleSwipe(true);
        loadOrders();
    }

    @Override
    public void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        super.onDestroy();
    }

    private void notifyUserWith(String message) {
        if (rootView != null)
            Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setUpSwipe() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(this::loadOrders);
    }

    private void loadOrders() {
        if (networkStateChecker.isNetworkAvailable()) {
            subscription = restService.getOrders(new Credentials())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setAdapter, throwable -> {
                        toggleSwipe(false);
                        notifyUserWith(throwable.getMessage());
                    }, () -> toggleSwipe(false));
        } else {
            toggleSwipe(false);
            notifyUserWith(networkUnavailableMessage);
        }
    }

    private void setAdapter(List<StatusItem> statusItems) {
        recyclerView.setAdapter(new StatusAdapter(statusItems, getContext()));
    }

    private void toggleSwipe(boolean isRefreshing) {
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(isRefreshing);
    }
}
