package com.valevich.materikemployee.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.valevich.materikemployee.MaterikEmployeeApplication_;
import com.valevich.materikemployee.R;
import com.valevich.materikemployee.network.RestService;
import com.valevich.materikemployee.network.model.request.Credentials;
import com.valevich.materikemployee.network.model.response.AuthModel;
import com.valevich.materikemployee.util.InputFieldValidator;
import com.valevich.materikemployee.util.NetworkStateChecker;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_auth)
public class LogInActivity extends AppCompatActivity {

    @ViewById(R.id.root)
    RelativeLayout rootView;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.emailField)
    AppCompatEditText emailField;

    @ViewById(R.id.passwordField)
    AppCompatEditText passwordField;

    @StringRes(R.string.invalid_password_msg)
    String invalidPasswordMessage;

    @StringRes(R.string.invalid_email_msg)
    String invalidEmailMessage;

    @StringRes(R.string.network_unavailable)
    String networkUnavailableMessage;

    @StringRes(R.string.auth_successfull)
    String authSuccessMessage;

    @StringRes(R.string.log_in)
    String title;

    @Bean
    InputFieldValidator inputFieldValidator;

    @Bean
    NetworkStateChecker networkStateChecker;

    @Bean
    RestService restService;

    private Subscription subscription;

    private boolean hasLoadingFinished = true;


    @AfterViews
    void setUpViews() {
        setUpToolbar();
    }

    @Override
    protected void onDestroy() {
        unSubscribe();
        super.onDestroy();
    }

    @Click(R.id.authButton)
    protected void submitAccountInfo() {

        if (hasLoadingFinished) {
            hasLoadingFinished = false;
            hideKeyBoard();

            String password = getPassword();
            String email = getEmail();

            if (isInputValid(password, email) && isNetworkAvailable()) {
                auth(email, password);
            } else {
                hasLoadingFinished = true;
            }
        }

    }

    protected void notifyUserWith(String message) {
        if (rootView != null)
            Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }

    private String getPassword() {
        return passwordField.getText().toString().trim();
    }

    private String getEmail() {
        return emailField.getText().toString().trim();
    }

    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean isNetworkAvailable() {
        boolean isAvailable = networkStateChecker.isNetworkAvailable();
        if (!isAvailable) notifyUserWith(networkUnavailableMessage);
        return isAvailable;
    }

    private boolean isInputValid(String password, String email) {
        if (!inputFieldValidator.isPasswordValid(password)) {
            notifyUserWith(invalidPasswordMessage);
            return false;
        } else if (!inputFieldValidator.isEmailValid(email)) {
            notifyUserWith(invalidEmailMessage);
            return false;
        }
        return true;
    }

    private void unSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
    }

    private void auth(String email, String password) {
        subscription = restService.logIn(new Credentials(email,password))
                .doAfterTerminate(() -> hasLoadingFinished = true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAuth, this::onError);
    }

    private void onAuth(AuthModel authModel) {
        if (authModel.getMessage() != null) notifyUserWith(authModel.getMessage());
        else {
            Toast.makeText(this, authSuccessMessage, Toast.LENGTH_SHORT).show();
            authModel.setPassword(getPassword());
            MaterikEmployeeApplication_.saveUserDataBulk(authModel);
            enter();
        }
    }

    private void onError(Throwable t) {
        notifyUserWith(t.getMessage());
    }

    private void enter() {
        MainActivity_.intent(LogInActivity.this)
                .flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .start();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            setTitle(title);
        }
    }

}
