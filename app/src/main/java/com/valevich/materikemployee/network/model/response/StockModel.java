package com.valevich.materikemployee.network.model.response;

import java.util.List;

public class StockModel extends DefaultResponse {
    private int id;
    private String address;
    private List<ProductItem> products;

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public List<ProductItem> getProducts() {
        return products;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProducts(List<ProductItem> products) {
        this.products = products;
    }
}
