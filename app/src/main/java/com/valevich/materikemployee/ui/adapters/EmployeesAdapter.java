package com.valevich.materikemployee.ui.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.valevich.materikemployee.R;
import com.valevich.materikemployee.network.model.response.EmployeeModel;
import com.valevich.materikemployee.network.model.response.OrderItem;
import com.valevich.materikemployee.ui.activities.OrdersByStatusActivity_;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.EmployeeHolder>{

    private List<EmployeeModel> employees;

    private Context context;

    public EmployeesAdapter(List<EmployeeModel> employees, Context context) {
        this.employees = employees;
        this.context = context;
    }

    @Override
    public EmployeeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_item, parent,
                false);
        return new EmployeeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EmployeeHolder holder, int position) {
        holder.bind(employees.get(position));
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    class EmployeeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.employee_fullName)
        TextView nameLabel;

        @BindView(R.id.employee_age)
        TextView ageLabel;

        @BindView(R.id.employee_position)
        TextView positionLabel;

        @BindView(R.id.employee_email)
        TextView emailLabel;

        @BindView(R.id.employee_phone)
        TextView phoneLabel;

        EmployeeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(view -> showOrders());
        }

        void bind(EmployeeModel employeeModel) {
            Locale l = Locale.getDefault();
            nameLabel.setText(String.format(l,"%s %s",employeeModel.getName(),employeeModel.getSurname()));
            ageLabel.setText(String.format(l, "Возраст: %d",employeeModel.getAge()));
            positionLabel.setText(String.format(l,"Должность: %s",employeeModel.getPosition()));
            emailLabel.setText(String.format(l,"Электронный адрес: %s",employeeModel.getEmail()));
            phoneLabel.setText(String.format(l,"Телефон: %s",employeeModel.getPhoneNumber()));
        }

        private void showOrders() {
            EmployeeModel item = employees.get(getAdapterPosition());
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
