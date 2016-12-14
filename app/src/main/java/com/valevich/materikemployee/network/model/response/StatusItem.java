package com.valevich.materikemployee.network.model.response;

import java.util.List;

public class StatusItem extends DefaultResponse {
    private int id;
    private String description;
    private List<OrderItem> orders;

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public List<OrderItem> getOrders() {
        return orders;
    }
}
