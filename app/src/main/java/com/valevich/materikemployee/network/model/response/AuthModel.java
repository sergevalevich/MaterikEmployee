package com.valevich.materikemployee.network.model.response;


public class AuthModel extends DefaultResponse{
    private int id;
    private String name;
    private int age;
    private String position;
    private String phoneNumber;
    private String surname;
    private String email;
    private int bitmask;
    private String password;

    public AuthModel(int id, String name, int age, String position, String phoneNumber, String surname, String email, int bitmask,String password) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.surname = surname;
        this.email = email;
        this.bitmask = bitmask;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getBitmask() {
        return bitmask;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBitmask(int bitmask) {
        this.bitmask = bitmask;
    }
}
