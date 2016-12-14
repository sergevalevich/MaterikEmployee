package com.valevich.materikemployee.network.model.response;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ProductItem implements Parcelable {
    private int id;
    private String name;
    private double price;
    private String metrics;
    private String imageUrl;
    private String description;
    private String articul;
    private int stockAmount;
    private int orderAmount;
    private String stockAddress;
    private List<FeatureItem> featureList;

    public ProductItem() {

    }

    protected ProductItem(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readDouble();
        metrics = in.readString();
        imageUrl = in.readString();
        description = in.readString();
        articul = in.readString();
        stockAmount = in.readInt();
        orderAmount = in.readInt();
        stockAddress = in.readString();
        featureList = new ArrayList<>();
        in.readTypedList(featureList, FeatureItem.CREATOR);
    }

    public static final Creator<ProductItem> CREATOR = new Creator<ProductItem>() {
        @Override
        public ProductItem createFromParcel(Parcel in) {
            return new ProductItem(in);
        }

        @Override
        public ProductItem[] newArray(int size) {
            return new ProductItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getMetrics() {
        return metrics;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getArticul() {
        return articul;
    }

    public int getStockAmount() {
        return stockAmount;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public String getStockAddress() {
        return stockAddress;
    }

    public List<FeatureItem> getFeatureList() {
        return featureList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setArticul(String articul) {
        this.articul = articul;
    }

    public void setStockAmount(int stockAmount) {
        this.stockAmount = stockAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setStockAddress(String stockAddress) {
        this.stockAddress = stockAddress;
    }

    public void setFeatureList(List<FeatureItem> featureList) {
        this.featureList = featureList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeDouble(price);
        parcel.writeString(metrics);
        parcel.writeString(imageUrl);
        parcel.writeString(description);
        parcel.writeString(articul);
        parcel.writeInt(stockAmount);
        parcel.writeInt(orderAmount);
        parcel.writeString(stockAddress);
        parcel.writeTypedList(featureList);
    }
}
