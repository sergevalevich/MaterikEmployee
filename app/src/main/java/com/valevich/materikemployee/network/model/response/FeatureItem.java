package com.valevich.materikemployee.network.model.response;

import android.os.Parcel;
import android.os.Parcelable;

public class FeatureItem implements Parcelable{
    private String name;
    private String value;

    protected FeatureItem(Parcel in) {
        name = in.readString();
        value = in.readString();
    }

    public static final Creator<FeatureItem> CREATOR = new Creator<FeatureItem>() {
        @Override
        public FeatureItem createFromParcel(Parcel in) {
            return new FeatureItem(in);
        }

        @Override
        public FeatureItem[] newArray(int size) {
            return new FeatureItem[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(value);
    }
}
