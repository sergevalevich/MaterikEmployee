package com.valevich.materikemployee.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.otto.Subscribe;
import com.valevich.materikemployee.R;
import com.valevich.materikemployee.bus.EventBus;
import com.valevich.materikemployee.bus.events.ExportFinishedEvent;
import com.valevich.materikemployee.network.RestService;
import com.valevich.materikemployee.network.model.request.Credentials;
import com.valevich.materikemployee.network.model.response.StockModel;
import com.valevich.materikemployee.services.ExportService_;
import com.valevich.materikemployee.ui.adapters.StockAdapter;
import com.valevich.materikemployee.util.ConstantsManager;
import com.valevich.materikemployee.util.FileHelper;
import com.valevich.materikemployee.util.NetworkStateChecker;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
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
import timber.log.Timber;

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

    @StringRes(R.string.error_export)
    String exportErrorMessage;

    @StringRes(R.string.error_import)
    String importErrorMessage;

    @StringRes(R.string.no_file_explorer)
    String noFileExplorerMessage;

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
        showDialog();
        ExportService_.intent(getContext()).start();
    }

    @OptionsItem(R.id.action_import)
    void importSelected() {
        List<String> files = fileHelper.findFiles(new File(Environment.getExternalStorageDirectory(), storageDir), ".xls", new ArrayList<>());
        for (String f : files) Timber.d(f);
    }

    @Subscribe
    public void onExportFinished(ExportFinishedEvent exportFinishedEvent) {
        hideDialog();
        if (exportFinishedEvent.isSuccessFull()) {
            showStatsFolder();
        } else {
            String message = exportFinishedEvent.getMessage();
            notifyUserWith(message == null || message.isEmpty()
                    ? exportErrorMessage
                    : message);
        }
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

    private void showStatsFolder() {
        Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory()
                + File.separator + storageDir + File.separator);
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setDataAndType(selectedUri, "resource/folder");
//
//        if (intent.resolveActivityInfo(getContext().getPackageManager(), 0) != null) {
//            startActivity(intent);
//        } else {
//            notifyUserWith(noFileExplorerMessage);
//        }
        Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
                .setType("file/")
                .setStream(selectedUri)
                .getIntent();
        startActivity(shareIntent);

    }

    private void showDialog() {
        dialog = new MaterialDialog.Builder(getContext())
                .title(R.string.exporting)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }
}
