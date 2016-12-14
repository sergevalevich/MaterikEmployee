package com.valevich.materikemployee.network.model.response;


import java.util.List;

public class ClientModel extends DefaultResponse {
    private int id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String address;
    private String email;
    private String token;
    private String password;
    private List<OrderItem> orders;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public String getPassword() {
        return password;
    }

    public List<OrderItem> getOrders() {
        return orders;
    }
}
