package com.valevich.materikemployee.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.valevich.materikemployee.R;
import com.valevich.materikemployee.bus.EventBus;
import com.valevich.materikemployee.bus.events.ExportFinishedEvent;
import com.valevich.materikemployee.network.RestService;
import com.valevich.materikemployee.network.model.request.Credentials;
import com.valevich.materikemployee.util.ExcelHelper;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.res.StringRes;


import java.util.Date;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;
import timber.log.Timber;


@EService
public class ExportService extends Service {

    private Subscription subscription;

    @Bean
    RestService restService;

    @Bean
    ExcelHelper excelHelper;

    @Bean
    EventBus bus;

    @StringRes(R.string.stats_format)
    String statsFormat;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Credentials credentials = new Credentials();
        subscription = Observable.zip(restService.getStocks(credentials),
                restService.getCatalog(credentials, ""),
                (stockModels, catalogItems) -> excelHelper.exportCatalog(new Date().toString() + statsFormat,stockModels,catalogItems))
                .subscribeOn(Schedulers.io())
                .doAfterTerminate(this::stopSelf)
                .subscribe(isSuccessful -> {
                    bus.post(new ExportFinishedEvent(true,null));
                }, throwable -> {
                    bus.post(new ExportFinishedEvent(false,throwable.getMessage()));
                });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("on service destroy");
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
