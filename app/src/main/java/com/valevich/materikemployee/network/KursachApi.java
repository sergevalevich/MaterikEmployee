package com.valevich.materikemployee.network;


import com.valevich.materikemployee.network.model.request.Credentials;
import com.valevich.materikemployee.network.model.response.AuthModel;
import com.valevich.materikemployee.network.model.response.CatalogItem;
import com.valevich.materikemployee.network.model.response.ClientModel;
import com.valevich.materikemployee.network.model.response.EmployeeModel;
import com.valevich.materikemployee.network.model.response.StatusItem;
import com.valevich.materikemployee.network.model.response.StockModel;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

interface KursachApi {

    @POST("employee/login")
    Observable<AuthModel> logIn(@Body Credentials credentials);

    @GET("employees/get")
    Observable<List<EmployeeModel>> getEmployees(@Header("email") String email,
                                                 @Header("password") String password);

    @GET("stocks/get")
    Observable<List<StockModel>> getStocks(@Header("email") String email,
                                           @Header("password") String password);

    @GET("clients/get")
    Observable<List<ClientModel>> getClients(@Header("email") String email,
                                             @Header("password") String password);

    @GET("employee/catalog")
    Observable<List<CatalogItem>> getCatalog(@Header("email") String email,
                                             @Header("password") String password,
                                             @Header("filter") String filter);

    @GET("orders/get")
    Observable<List<StatusItem>> getOrders(@Header("email") String email,
                                           @Header("password") String password);


}
