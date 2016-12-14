package com.valevich.materikemployee.ui.activities;

import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.valevich.materikemployee.R;
import com.valevich.materikemployee.network.model.response.ProductItem;
import com.valevich.materikemployee.ui.adapters.FeaturesAdapter;
import com.valevich.materikemployee.util.ConstantsManager;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.Locale;

@EActivity(R.layout.activity_product_details)
public class ProductDetailsActivity extends AppCompatActivity {

    @ViewById(R.id.root)
    CoordinatorLayout rootView;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.features_list)
    RecyclerView featuresList;

    @ViewById(R.id.product_articul)
    TextView articulLabel;

    @ViewById(R.id.product_amount)
    TextView amountLabel;

    @ViewById(R.id.product_price)
    TextView priceLabel;

    @ViewById(R.id.stock_address)
    TextView stockAddressLabel;

    @ViewById(R.id.product_image)
    ImageView productImage;

    @StringRes(R.string.price_label)
    String priceLabelTitle;

    @StringRes(R.string.articul_label)
    String articulLabelTitle;

    @StringRes(R.string.amount_label)
    String amountLabelTitle;

    @StringRes(R.string.stock_address_title)
    String stockAddressLabelTitle;

    @StringRes(R.string.stock_empty)
    String stockEmpty;

    @Extra
    Parcelable parcelableProduct;

    private ProductItem product;

    @AfterExtras
    void setUpDetails() {
        if (parcelableProduct != null) {
            product = (ProductItem) parcelableProduct;
        }
    }

    @AfterViews
    void setUpViews() {
        setUpToolbar();
        setUpRecyclerView();
        setUpImage();
        setUpArticul();
        setUpAmount();
        setUpPrice();
        setUpStock();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            setTitle(product.getName());
        }
    }

    private void setUpPrice() {
        priceLabel.setText(String.format(Locale.getDefault(),
                priceLabelTitle + " " + ConstantsManager.PRICE_FORMAT,
                product.getPrice(), product.getMetrics()));
    }

    private void setUpStock() {
        stockAddressLabel.setText(String.format(Locale.getDefault(), "%s %s", stockAddressLabelTitle, product.getStockAddress()));
    }

    private void setUpAmount() {
        amountLabel.setText(product.getStockAmount() == 0
                ? String.format(Locale.getDefault(), "%s %s", amountLabelTitle, stockEmpty)
                : String.format(Locale.getDefault(), "%s %d %s", amountLabelTitle, product.getStockAmount(), product.getMetrics()));
    }

    private void setUpArticul() {
        articulLabel.setText(String.format(Locale.getDefault(), "%s %s", articulLabelTitle, product.getArticul()));
    }

    private void setUpRecyclerView() {
        featuresList.setLayoutManager(new LinearLayoutManager(this));
        featuresList.setAdapter(new FeaturesAdapter(product.getFeatureList()));
    }

    private void setUpImage() {
        Glide.with(this).load(product.getImageUrl()).into(productImage);
    }

}
