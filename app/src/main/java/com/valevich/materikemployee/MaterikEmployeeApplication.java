package com.valevich.materikemployee;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.valevich.materikemployee.network.model.response.AuthModel;
import com.valevich.materikemployee.util.Preferences_;

import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.sharedpreferences.Pref;

import timber.log.Timber;


@EApplication
public class MaterikEmployeeApplication extends Application {
    @Pref
    static Preferences_ preferences;

    @Override
    public void onCreate() {
        super.onCreate();

        //DbFlow
        FlowManager.init(new FlowConfig.Builder(this)
                .openDatabasesOnInit(true).build());

        //Timber
        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });
        }
    }

    public static boolean hasUserLoggedIn() {
        return !preferences.email().get().isEmpty();
    }

    public static void saveUserDataBulk(AuthModel authModel) {
        preferences.name().put(authModel.getName());
        preferences.surname().put(authModel.getSurname());
        preferences.email().put(authModel.getEmail());
        preferences.bitmask().put(authModel.getBitmask());
        preferences.password().put(authModel.getPassword());
    }

    public static String getUserEmail() {
        return preferences.email().get();
    }

    public static int getUserBitMask() {
        return preferences.bitmask().get();
    }

    public static String getUsername() {
        return preferences.name().get();
    }

    public static String getUserSurname() {
        return preferences.surname().get();
    }

    public static String getUserPassword() {
        return preferences.password().get();
    }
}
