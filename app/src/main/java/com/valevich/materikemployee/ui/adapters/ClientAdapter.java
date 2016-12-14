package com.valevich.materikemployee.ui.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.valevich.materikemployee.R;
import com.valevich.materikemployee.network.model.response.ClientModel;
import com.valevich.materikemployee.network.model.response.OrderItem;
import com.valevich.materikemployee.ui.activities.OrdersByStatusActivity_;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientHolder> {

    private List<ClientModel> clients;

    private Context context;

    public ClientAdapter(List<ClientModel> clients, Context context) {
        this.clients = clients;
        this.context = context;
    }

    @Override
    public ClientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item, parent,
                false);
        return new ClientHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClientHolder holder, int position) {
        holder.bind(clients.get(position));
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    class ClientHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.client_fullName)
        TextView clientNameLabel;

        @BindView(R.id.client_phone)
        TextView clientPhoneLabel;

        @BindView(R.id.client_address)
        TextView clientAddressLabel;

        @BindView(R.id.client_email)
        TextView clientEmailLabel;

        ClientHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> showOrders());
        }

        void bind(ClientModel clientModel) {
            Locale l = Locale.getDefault();
            String name = clientModel.getName() == null ? "" : clientModel.getName();
            String surName = clientModel.getSurname() == null ? "" :clientModel.getSurname();
            String phone = clientModel.getPhoneNumber();
            String address = clientModel.getAddress();
            String fullName = name + " " + surName;
            clientNameLabel.setText(String.format(l, "%s", fullName.trim().isEmpty() ? "Имя_не_указано" : fullName));
            clientEmailLabel.setText(String.format(l, "Электронный адрес: %s", clientModel.getEmail()));
            clientPhoneLabel.setText(String.format(l, "Телефон: %s", phone == null ? "не указан" : phone));
            clientAddressLabel.setText(String.format(l, "Адрес: %s",address == null ? "не указан" : address));
        }

        private void showOrders() {
            ClientModel item = clients.get(getAdapterPosition());
            List<OrderItem> orderItems = item.getOrders();
            if(orderItems == null || orderItems.isEmpty()) {
                Toast.makeText(context,"Нет заказов",Toast.LENGTH_SHORT).show();
            } else {
                OrderItem[] ors = new OrderItem[orderItems.size()];
                ors = orderItems.toArray(ors);
                OrdersByStatusActivity_.intent(context)
                        .parcelableOrders(ors)
                        .start();
            }
        }
    }
}
