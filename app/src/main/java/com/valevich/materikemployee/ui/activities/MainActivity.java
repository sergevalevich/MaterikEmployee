package com.valevich.materikemployee.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.valevich.materikemployee.MaterikEmployeeApplication_;
import com.valevich.materikemployee.R;
import com.valevich.materikemployee.network.model.response.AuthModel;
import com.valevich.materikemployee.ui.fragments.CatalogFragment_;
import com.valevich.materikemployee.ui.fragments.ClientsFragment_;
import com.valevich.materikemployee.ui.fragments.EmployeesFragment_;
import com.valevich.materikemployee.ui.fragments.OrdersFragment_;
import com.valevich.materikemployee.ui.fragments.StocksFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity
        implements FragmentManager.OnBackStackChangedListener {

    @ViewById(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.navigation_view)
    NavigationView navigationView;

    @StringRes(R.string.nav_drawer_catalog)
    String catalogTitle;

    @StringRes(R.string.nav_drawer_stock)
    String stockTitle;

    @StringRes(R.string.nav_drawer_orders)
    String ordersTitle;

    @StringRes(R.string.nav_drawer_clients)
    String clientsTitle;

    @StringRes(R.string.nav_drawer_employees)
    String employeesTitle;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            replaceFragment(new CatalogFragment_());
        }

    }

    @AfterViews
    void setupViews() {
        setupActionBar();
        setupDrawerLayout();
        setupFragmentManager();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onBackStackChanged() {

        Fragment f = fragmentManager
                .findFragmentById(R.id.main_container);

        if (f != null) {
            changeToolbarTitle(f.getClass().getName());
        }

    }

    private void clearUserData() {
        MaterikEmployeeApplication_.saveUserDataBulk(new AuthModel(0, "", 0, "", "", "", "", 0,""));
    }

    private void exit() {
        LogInActivity_.intent(MainActivity.this)
                .flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .start();
    }

    private void setupNavigationContent() {
        navigationView.setNavigationItemSelectedListener(item -> {
            if (drawerLayout != null) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.drawer_catalog:
                    replaceFragment(new CatalogFragment_());
                    break;
                case R.id.drawer_orders:
                    replaceFragment(new OrdersFragment_());
                    break;
                case R.id.drawer_stock:
                    replaceFragment(new StocksFragment_());
                    break;
                case R.id.drawer_clients:
                    replaceFragment(new ClientsFragment_());
                    break;
                case R.id.drawer_employees:
                    replaceFragment(new EmployeesFragment_());
                    break;
                case R.id.drawer_exit:
                    clearUserData();
                    exit();
                    break;
            }
            return true;
        });
        setUpHeader(navigationView.getHeaderView(0));
    }

    private void setUpHeader(View headerView) {

        TextView nameField = (TextView) headerView.findViewById(R.id.name);
        TextView emailField = (TextView) headerView.findViewById(R.id.email);
        ImageView profileImage = (ImageView) headerView.findViewById(R.id.profile_image);

        String userFullName = MaterikEmployeeApplication_.getUsername() + " " + MaterikEmployeeApplication_.getUserSurname();
        String userEmail = MaterikEmployeeApplication_.getUserEmail();

        nameField.setText(userFullName);
        emailField.setText(userEmail);

        Glide.with(this).load(R.drawable.dummy_profile).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(profileImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        profileImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    private void changeToolbarTitle(String backStackEntryName) {
        if (backStackEntryName.equals(CatalogFragment_.class.getName())) {
            setTitle(catalogTitle);
            navigationView.setCheckedItem(R.id.drawer_catalog);
        } else if (backStackEntryName.equals(StocksFragment_.class.getName())) {
            setTitle(stockTitle);
            navigationView.setCheckedItem(R.id.drawer_stock);
        } else if (backStackEntryName.equals(ClientsFragment_.class.getName())) {
            setTitle(clientsTitle);
            navigationView.setCheckedItem(R.id.drawer_clients);
        } else if (backStackEntryName.equals(EmployeesFragment_.class.getName())) {
            setTitle(employeesTitle);
            navigationView.setCheckedItem(R.id.drawer_employees);
        } else {
            setTitle(ordersTitle);
            navigationView.setCheckedItem(R.id.drawer_orders);
        }
    }

    private void setupDrawerLayout() {
        setupNavigationContent();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout
                , toolbar
                , R.string.navigation_drawer_open
                , R.string.navigation_drawer_close);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        setTitle(catalogTitle);
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void replaceFragment(Fragment fragment) {
        String backStackName = fragment.getClass().getName();

        boolean isFragmentPopped = fragmentManager.popBackStackImmediate(backStackName, 0);

        if (!isFragmentPopped && fragmentManager.findFragmentByTag(backStackName) == null) {

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, fragment, backStackName);
            transaction.addToBackStack(backStackName);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();

        }
    }

    private void setupFragmentManager() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);
    }
}
