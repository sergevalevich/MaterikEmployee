package com.valevich.materikemployee.network.model.request;


import com.valevich.materikemployee.network.model.response.CatalogItem;
import com.valevich.materikemployee.network.model.response.StockModel;

import java.util.List;

public class BulkUpdateModel {
    private List<CatalogItem> catalog;
    private List<StockModel> stocks;

    public BulkUpdateModel(List<CatalogItem> catalog, List<StockModel> stocks) {
        this.catalog = catalog;
        this.stocks = stocks;
    }
}
