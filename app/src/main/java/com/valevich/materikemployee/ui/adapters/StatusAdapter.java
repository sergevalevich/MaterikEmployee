package com.valevich.materikemployee.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.valevich.materikemployee.R;
import com.valevich.materikemployee.network.model.response.OrderItem;
import com.valevich.materikemployee.network.model.response.StatusItem;
import com.valevich.materikemployee.ui.activities.OrdersByStatusActivity_;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusHolder> {

    private List<StatusItem> statuses;

    private Context context;

    public StatusAdapter(List<StatusItem> statuses, Context context) {
        this.statuses = statuses;
        this.context = context;
    }

    @Override
    public StatusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_item, parent,
                false);
        return new StatusHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StatusHolder holder, int position) {
        holder.bind(statuses.get(position));
    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }

    class StatusHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.status_name)
        TextView statusNameLabel;

        StatusHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> showOrders());
        }

        private void showOrders() {
            StatusItem item = statuses.get(getAdapterPosition());
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

        void bind(StatusItem item) {
            statusNameLabel.setText(String.format(Locale.getDefault(),
                    "%s (%d)",
                    item.getDescription(),
                    item.getOrders()==null ? 0 : item.getOrders().size()));
        }
    }
}
