package com.valevich.materikemployee.ui.activities;

import android.os.Build;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;


import com.valevich.materikemployee.R;
import com.valevich.materikemployee.network.model.response.ProductItem;
import com.valevich.materikemployee.ui.adapters.ProductsAdapter;
import com.valevich.materikemployee.util.ConstantsManager;
import com.valevich.materikemployee.util.ViewCustomizer;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.api.BackgroundExecutor;

import java.util.ArrayList;
import java.util.List;

@OptionsMenu(R.menu.search_menu)
@EActivity(R.layout.activity_products)
public class ProductsActivity extends AppCompatActivity {

    @Extra
    Parcelable[] parcelableProducts;

    @Extra
    String category;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.products_list)
    RecyclerView recyclerView;

    @StringRes(R.string.search_hint)
    String searchHint;

    @OptionsMenuItem(R.id.action_search)
    MenuItem searchMenuItem;

    @Bean
    ViewCustomizer viewCustomizer;

    private ProductsAdapter productsAdapter;

    private ProductItem[] products;

    @AfterExtras
    void setUpAdapter() {
        if(parcelableProducts != null) {
            products = new ProductItem[parcelableProducts.length];
            for (int i = 0; i < parcelableProducts.length; i++) {
                products[i] = (ProductItem) parcelableProducts[i];
            }
            productsAdapter = new ProductsAdapter(products,this);
        }
    }

    @AfterViews
    void setUpViews() {
        setUpToolbar();
        setUpRecyclerView();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            viewCustomizer.customizeSearchView(searchView);
        }

        searchView.setQueryHint(searchHint);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                BackgroundExecutor.cancelAll(ConstantsManager.SEARCH_ID, true);
                searchProducts(newText);
                return false;
            }
        });
        return true;
    }

    @Background(delay = 700, id = ConstantsManager.SEARCH_ID)
    void searchProducts(String filter) {
        List<ProductItem> filteredItems = new ArrayList<>();
        for(ProductItem item:products) {
            if(item.getName().toLowerCase().contains(filter.toLowerCase())) filteredItems.add(item);
        }
        ProductItem[] filteredArray = new ProductItem[filteredItems.size()];
        filteredArray = filteredItems.toArray(filteredArray);
        productsAdapter = new ProductsAdapter(filteredArray,this);
        setAdapter();
    }

    @UiThread
    void setAdapter() {
        recyclerView.setAdapter(productsAdapter);
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setAdapter();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            setTitle(category);
        }
    }
}
