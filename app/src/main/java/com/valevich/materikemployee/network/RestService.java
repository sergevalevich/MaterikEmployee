package com.valevich.materikemployee.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.valevich.materikemployee.network.model.request.Credentials;
import com.valevich.materikemployee.network.model.response.AuthModel;
import com.valevich.materikemployee.network.model.response.CatalogItem;
import com.valevich.materikemployee.network.model.response.ClientModel;
import com.valevich.materikemployee.network.model.response.EmployeeModel;
import com.valevich.materikemployee.network.model.response.StatusItem;
import com.valevich.materikemployee.network.model.response.StockModel;
import com.valevich.materikemployee.util.ConstantsManager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

@EBean
public class RestService {

    private KursachApi mGameApi;

    @AfterInject
    void setUpClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsManager.BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mGameApi = retrofit.create(KursachApi.class);
    }

    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(getInterceptor())
                .connectTimeout(ConstantsManager.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(ConstantsManager.READ_TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    private HttpLoggingInterceptor getInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    private Gson getGson() {
        return new GsonBuilder().create();
    }

    public Observable<AuthModel> logIn(Credentials credentials) {
        return mGameApi.logIn(credentials);
    }

    public Observable<List<EmployeeModel>> getEmployees(Credentials credentials) {
        return mGameApi.getEmployees(credentials.getUserEmail(),credentials.getUserPassword());
    }

    public Observable<List<CatalogItem>> getCatalog(Credentials credentials, String filter) {
        return mGameApi.getCatalog(credentials.getUserEmail(),credentials.getUserPassword(),filter);
    }

    public Observable<List<StockModel>> getStocks(Credentials credentials) {
        return mGameApi.getStocks(credentials.getUserEmail(),credentials.getUserPassword());
    }

    public Observable<List<ClientModel>> getClients(Credentials credentials) {
        return mGameApi.getClients(credentials.getUserEmail(),credentials.getUserPassword());
    }

    public Observable<List<StatusItem>> getOrders(Credentials credentials) {
        return mGameApi.getOrders(credentials.getUserEmail(),credentials.getUserPassword());
    }

}
