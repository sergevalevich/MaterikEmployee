package com.valevich.materikemployee.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.valevich.materikemployee.R;
import com.valevich.materikemployee.network.model.response.OrderItem;
import com.valevich.materikemployee.network.model.response.ProductItem;
import com.valevich.materikemployee.ui.activities.OrderProductsActivity_;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersHolder> {

    private List<OrderItem> orders;

    private Context context;

    public OrdersAdapter(List<OrderItem> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @Override
    public OrdersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent,
                false);
        return new OrdersHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrdersHolder holder, int position) {
        holder.bind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrdersHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.id_label)
        TextView idLabel;

        @BindView(R.id.date_label)
        TextView dateLabel;

        @BindView(R.id.status_label)
        TextView statusLabel;

        @BindView(R.id.sum_label)
        TextView sumLabel;

        @BindView(R.id.client_name_label)
        TextView clientNameLabel;

        @BindView(R.id.client_phone_label)
        TextView clientPhoneLabel;

        @BindView(R.id.employee_name_label)
        TextView employeeNameLabel;

        @BindView(R.id.employee_phone_label)
        TextView employeePhoneLabel;

        @BindView(R.id.delivery_address_label)
        TextView deliveryAddressLabel;

        OrdersHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> showProducts());
        }

        private void showProducts() {
            OrderItem item = orders.get(getAdapterPosition());
            List<ProductItem> products = item.getProducts();
            if(products != null && !products.isEmpty()) {
                ProductItem[] prs = new ProductItem[products.size()];
                prs = products.toArray(prs);
                OrderProductsActivity_.intent(context)
                        .parcelableProducts(prs)
                        .start();
            }
        }

        void bind(OrderItem item) {
            Locale l = Locale.getDefault();
            idLabel.setText(String.format(l, "Заказ #%d", item.getId()));
            dateLabel.setText(String.format(l, "Добавлен: %s", item.getDate()));
            statusLabel.setText(String.format(l, "Статус: %s", item.getStatus()));
            sumLabel.setText(String.format(l, "Сумма: %.2f Р.", item.getSum()));
            clientNameLabel.setText(String.format(l, "Клиент: %s", item.getClientFullName()));
            clientPhoneLabel.setText(String.format(l, "Телефон клиента: %s", item.getClientPhone()));
            String employeePhone = item.getEmployeePhone();
            String employeeName = item.getEmployeeName();
            String employeeSurName = item.getEmployeeSurname();
            employeeNameLabel.setText(String.format(l, "Консультант: %s", (employeeName == null && employeeSurName == null
                    ? "Не назначен"
                    : employeeName + " " + employeeSurName)));
            employeePhoneLabel.setText(String.format(l, "Телефон консультанта: %s", employeePhone == null
                    ? "Не назначен"
                    : employeePhone));
            deliveryAddressLabel.setText(String.format(l, "Куда: %s", item.getDeliveryAddress()));
        }
    }
}
