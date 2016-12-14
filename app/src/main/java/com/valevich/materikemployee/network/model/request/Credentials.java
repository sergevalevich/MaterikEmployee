package com.valevich.materikemployee.network.model.request;

import com.valevich.materikemployee.MaterikEmployeeApplication_;

public class Credentials {
    private String userEmail;
    private String userPassword;

    public Credentials() {
        this.userEmail = MaterikEmployeeApplication_.getUserEmail();
        this.userPassword = MaterikEmployeeApplication_.getUserPassword();
    }

    public Credentials(String email, String password) {
        this.userEmail = email;
        this.userPassword = password;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
