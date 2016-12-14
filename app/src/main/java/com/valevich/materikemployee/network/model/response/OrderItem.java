package com.valevich.materikemployee.network.model.response;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class OrderItem implements Parcelable {
    private int id;
    private String date;
    private String clientFullName;
    private String clientPhone;
    private String status;
    private String employeeName;
    private String employeeSurname;
    private String employeePhone;
    private String deliveryAddress;
    private double sum;
    private List<ProductItem> products;

    protected OrderItem(Parcel in) {
        id = in.readInt();
        date = in.readString();
        clientFullName = in.readString();
        clientPhone = in.readString();
        status = in.readString();
        employeeName = in.readString();
        employeeSurname = in.readString();
        employeePhone = in.readString();
        deliveryAddress = in.readString();
        sum = in.readDouble();
        products = in.createTypedArrayList(ProductItem.CREATOR);
    }

    public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel in) {
            return new OrderItem(in);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };

    public String getClientFullName() {
        return clientFullName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmployeeSurname() {
        return employeeSurname;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public double getSum() {
        return sum;
    }

    public String getStatus() {
        return status;
    }

    public List<ProductItem> getProducts() {
        return products;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(date);
        parcel.writeString(clientFullName);
        parcel.writeString(clientPhone);
        parcel.writeString(status);
        parcel.writeString(employeeName);
        parcel.writeString(employeeSurname);
        parcel.writeString(employeePhone);
        parcel.writeString(deliveryAddress);
        parcel.writeDouble(sum);
        parcel.writeTypedList(products);
    }
}
