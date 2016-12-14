package com.valevich.materikemployee.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.valevich.materikemployee.bus.EventBus;
import com.valevich.materikemployee.bus.events.ImportFinishedEvent;
import com.valevich.materikemployee.network.RestService;
import com.valevich.materikemployee.network.model.request.Credentials;
import com.valevich.materikemployee.util.ConstantsManager;
import com.valevich.materikemployee.util.ExcelHelper;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

import rx.Subscription;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@EService
public class ImportService extends Service {

    private Subscription subscription;

    @Bean
    RestService restService;

    @Bean
    ExcelHelper excelHelper;

    @Bean
    EventBus bus;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String fileName = intent.getStringExtra(ConstantsManager.FILE_EXTRA);
        Credentials credentials = new Credentials();
        subscription = excelHelper.importCatalog(fileName)
                .flatMap(model -> restService.bulkUpdate(model,credentials))
                .subscribeOn(Schedulers.io())
                .doAfterTerminate(this::stopSelf)
                .subscribe(response -> {
                    bus.post(new ImportFinishedEvent(response.getMessage()));
                }, throwable -> {
                    bus.post(new ImportFinishedEvent(throwable.getMessage()));
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
