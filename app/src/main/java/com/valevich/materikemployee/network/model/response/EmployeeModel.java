package com.valevich.materikemployee.network.model.response;

import java.util.List;

public class EmployeeModel extends AuthModel {
    private List<OrderItem> orders;

    public EmployeeModel(int id, String name, int age, String position, String phoneNumber, String surname, String email, int bitmask,String password) {
        super(id, name, age, position, phoneNumber, surname, email, bitmask,password);
    }

    public List<OrderItem> getOrders() {
        return orders;
    }
}
