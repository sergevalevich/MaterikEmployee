package com.valevich.materikemployee.network.model.response;

import java.util.List;

public class CatalogItem {
    private int id;
    private String name;
    private String imageUrl;
    private List<ProductItem> products;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<ProductItem> getProducts() {
        return products;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setProducts(List<ProductItem> products) {
        this.products = products;
    }
}
