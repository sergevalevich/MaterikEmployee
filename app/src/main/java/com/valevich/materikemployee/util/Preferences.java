package com.valevich.materikemployee.util;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;


@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface Preferences {

    @DefaultString("no_name")
    String name();

    @DefaultString("no_surname")
    String surname();

    @DefaultString("")
    String email();

    @DefaultString("")
    String password();

    @DefaultInt(value = 0)
    int bitmask();
}