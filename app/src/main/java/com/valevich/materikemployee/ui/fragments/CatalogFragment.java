package com.valevich.materikemployee.ui.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.jakewharton.rxbinding.widget.RxSearchView;
import com.jakewharton.rxbinding.widget.SearchViewQueryTextEvent;
import com.valevich.materikemployee.MaterikEmployeeApplication_;
import com.valevich.materikemployee.R;
import com.valevich.materikemployee.network.RestService;
import com.valevich.materikemployee.network.model.request.Credentials;
import com.valevich.materikemployee.network.model.response.CatalogItem;
import com.valevich.materikemployee.ui.adapters.CatalogAdapter;
import com.valevich.materikemployee.util.ExcelHelper;
import com.valevich.materikemployee.util.FileHelper;
import com.valevich.materikemployee.util.NetworkStateChecker;
import com.valevich.materikemployee.util.ViewCustomizer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


@OptionsMenu(R.menu.search_menu)
@EFragment(R.layout.fragment_catalog)
public class CatalogFragment extends Fragment {

    @ViewById(R.id.root)
    CoordinatorLayout rootView;

    @ViewById(R.id.categories_list)
    RecyclerView recyclerView;

    @ViewById(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @StringRes(R.string.network_unavailable)
    String networkUnavailableMessage;

    @StringRes(R.string.search_hint)
    String searchHint;

    @OptionsMenuItem(R.id.action_search)
    MenuItem searchMenuItem;

    @Bean
    RestService restService;

    @Bean
    NetworkStateChecker networkStateChecker;

    @Bean
    FileHelper fileHelper;

    @Bean
    ExcelHelper excelReader;

    @Bean
    ViewCustomizer viewCustomizer;

    private CompositeSubscription subscription;

    @AfterViews
    void onViewsReady() {
        setUpRecyclerView();
        setUpSwipe();
        toggleSwipe(true);
        loadCatalog("");
    }

    @Override
    public void onDestroy() {
        unSubscribe();
        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscription = new CompositeSubscription();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        //customize default searchView style for pre L devices because it looks ugly
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            viewCustomizer.customizeSearchView(searchView);
        }
        searchView.setQueryHint(searchHint);
        subscription.add(RxSearchView.queryTextChangeEvents(searchView)
                .skip(1)
                .debounce(700, TimeUnit.MILLISECONDS)
                .map(SearchViewQueryTextEvent::queryText)
                .map(CharSequence::toString)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .flatMap(this::getCatalogObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setAdapter, throwable -> {
                    toggleSwipe(false);
                    notifyUserWith(throwable.getMessage());
                }, () -> toggleSwipe(false)));


    }

    private void unSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setUpSwipe() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(() -> loadCatalog(""));
    }

    private void loadCatalog(String filter) {
        if (networkStateChecker.isNetworkAvailable()) {
            subscription.add(getCatalogObservable(filter)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setAdapter, throwable -> {
                        toggleSwipe(false);
                        notifyUserWith(throwable.getMessage());
                    }, () -> toggleSwipe(false)));
        } else {
            toggleSwipe(false);
            notifyUserWith(networkUnavailableMessage);
        }
    }

    private Observable<List<CatalogItem>> getCatalogObservable(String filter) {
        return restService.getCatalog(
                new Credentials(),
                filter);
    }

    private void setAdapter(List<CatalogItem> catalogItems) {
        recyclerView.setAdapter(new CatalogAdapter(catalogItems, getContext()));
    }

    private void notifyUserWith(String message) {
        if (rootView != null)
            Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }

    private void toggleSwipe(boolean isRefreshing) {
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(isRefreshing);
    }
}
