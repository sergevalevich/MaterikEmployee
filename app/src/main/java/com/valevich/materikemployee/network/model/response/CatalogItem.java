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
}
