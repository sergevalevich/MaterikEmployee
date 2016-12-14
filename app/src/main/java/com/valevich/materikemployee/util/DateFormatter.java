package com.valevich.materikemployee.util;

import org.androidannotations.annotations.EBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@EBean
public class DateFormatter {

    private static final String DB_FORMAT = "yyyy-MM-dd";

    public String getStringFromDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DB_FORMAT, Locale.getDefault());
        return sdf.format(date);
    }
}
