package com.valevich.materikemployee.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.valevich.materikemployee.R;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.DrawableRes;

@EBean
public class ViewCustomizer {

    @RootContext
    Context context;

    @ColorRes(R.color.colorPrimary)
    int colorprimary;

    @DrawableRes(R.drawable.ic_action_search)
    Drawable searchViewDrawable;

    @DrawableRes(R.drawable.ic_clear)
    Drawable closeButtonDrawable;

    public void customizeSearchView(SearchView searchView) {
        int searchPlateId = searchView
                .getContext()
                .getResources()
                .getIdentifier("android:id/search_plate", null, null);
        View searchPlateView = searchView.findViewById(searchPlateId);

        if (searchPlateView != null) {
            searchPlateView.setBackgroundColor(colorprimary);
        }

        int searchImgId = context
                .getResources()
                .getIdentifier("android:id/search_button", null, null);
        ImageView search = (ImageView) searchView.findViewById(searchImgId);

        if (search != null) {
            search.setImageDrawable(searchViewDrawable);
        }

        int closeImgId = context
                .getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView close = (ImageView) searchView.findViewById(closeImgId);

        if (close != null) {
            close.setImageDrawable(closeButtonDrawable);
            close.setAlpha(0.4f);
        }

    }

}
