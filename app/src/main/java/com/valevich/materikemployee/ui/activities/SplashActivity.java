package com.valevich.materikemployee.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.valevich.materikemployee.R;

import org.androidannotations.annotations.EActivity;

import static com.valevich.materikemployee.MaterikEmployeeApplication.hasUserLoggedIn;


@EActivity
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(hasUserLoggedIn()) enter();
        else login();
    }

    private void enter() {
        MainActivity_.intent(this).start();
        finish();
    }

    private void  login() {
        LogInActivity_.intent(this).start();
        finish();
    }
}
