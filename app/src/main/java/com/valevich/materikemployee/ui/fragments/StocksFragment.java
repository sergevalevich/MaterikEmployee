package com.valevich.materikemployee.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.otto.Subscribe;
import com.valevich.materikemployee.R;
import com.valevich.materikemployee.bus.EventBus;
import com.valevich.materikemployee.bus.events.ExportFinishedEvent;
import com.valevich.materikemployee.bus.events.ImportFinishedEvent;
import com.valevich.materikemployee.network.RestService;
import com.valevich.materikemployee.network.model.request.Credentials;
import com.valevich.materikemployee.network.model.response.StockModel;
import com.valevich.materikemployee.services.ExportService_;
import com.valevich.materikemployee.services.ImportService_;
import com.valevich.materikemployee.ui.adapters.StockAdapter;
import com.valevich.materikemployee.util.ConstantsManager;
import com.valevich.materikemployee.util.FileHelper;
import com.valevich.materikemployee.util.NetworkStateChecker;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@OptionsMenu(R.menu.stock_menu)
@EFragment(R.layout.fragment_stocks)
public class StocksFragment extends Fragment {

    @ViewById(R.id.root)
    LinearLayout rootView;

    @ViewById(R.id.stock_list)
    RecyclerView recyclerView;

    @ViewById(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @StringRes(R.string.network_unavailable)
    String networkUnavailableMessage;

    @StringRes(R.string.stats_storage)
    String storageDir;

    @StringRes(R.string.stats_format)
    String statsFormat;

    @StringRes(R.string.error_export)
    String exportErrorMessage;

    @StringRes(R.string.error_import)
    String importErrorMessage;

    @StringRes(R.string.no_file_explorer)
    String noFileExplorerMessage;

    @StringRes(R.string.exporting)
    String exportMessage;

    @StringRes(R.string.importing)
    String importMessage;

    @Bean
    RestService restService;

    @Bean
    FileHelper fileHelper;

    @Bean
    NetworkStateChecker networkStateChecker;

    @Bean
    EventBus bus;

    private MaterialDialog dialog;

    private Subscription subscription;

    @AfterViews
    void onViewsReady() {
        setUpRecyclerView();
        setUpSwipe();
        toggleSwipe(true);
        register();
        loadStocks();
    }

    @Override
    public void onDestroy() {
        unSubscribe();
        unRegister();
        super.onDestroy();
    }

    @OptionsItem(R.id.action_export)
    void exportSelected() {
        showDialog(exportMessage);
        ExportService_.intent(getContext()).start();
    }

    @OptionsItem(R.id.action_import)
    void importSelected() {
        List<String> files = fileHelper.findFiles(new File(Environment.getExternalStorageDirectory(), storageDir), statsFormat, new ArrayList<>());
        new MaterialDialog.Builder(getContext())
                .items(fileHelper.formatFiles(files))
                .itemsCallbackSingleChoice(-1, (dialog1, view, which, text) -> {
                    if (which != -1) {
                        showDialog(importMessage);
                        ImportService_.intent(getContext())
                                .extra(ConstantsManager.FILE_EXTRA, files.get(which))
                                .start();
                    }
                    return true;
                }).positiveText(android.R.string.ok).show();
    }

    @Subscribe
    public void onExportFinished(ExportFinishedEvent event) {
        hideDialog();
        if (event.isSuccessFull()) {
            shareFile(event.getMessage());
        } else {
            String message = event.getMessage();
            notifyUserWith(message == null || message.isEmpty()
                    ? exportErrorMessage
                    : message);
        }
    }

    @Subscribe
    public void onImportFinished(ImportFinishedEvent event) {
        hideDialog();
        String message = event.getMessage();
        notifyUserWith(message == null || message.isEmpty() ? exportErrorMessage : message);
    }

    private void unSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
    }

    private void register() {
        bus.register(this);
    }

    private void unRegister() {
        bus.unregister(this);
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setUpSwipe() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(this::loadStocks);
    }

    private void loadStocks() {
        if (networkStateChecker.isNetworkAvailable()) {
            subscription = restService.getStocks(new Credentials())
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

    private void setAdapter(List<StockModel> stockModels) {
        recyclerView.setAdapter(new StockAdapter(stockModels, getContext()));
    }

    private void notifyUserWith(String message) {
        if (rootView != null)
            Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }

    private void toggleSwipe(boolean isRefreshing) {
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(isRefreshing);
    }

    private void shareFile(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + storageDir + File.separator + fileName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
        startActivity(intent);
    }

    private void showDialog(String title) {
        dialog = new MaterialDialog.Builder(getContext())
                .title(title)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }
}
