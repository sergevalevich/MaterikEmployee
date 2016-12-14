package com.valevich.materikemployee.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.valevich.materikemployee.R;
import com.valevich.materikemployee.network.model.response.FeatureItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeaturesAdapter extends RecyclerView.Adapter<FeaturesAdapter.FeaturesHolder> {

    private List<FeatureItem> features;

    public FeaturesAdapter(List<FeatureItem> features) {
        this.features = features;
    }

    @Override
    public FeaturesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feature_item, parent,
                false);
        return new FeaturesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeaturesHolder holder, int position) {
        holder.bind(features.get(position));
    }

    @Override
    public int getItemCount() {
        return features.size();
    }

    class FeaturesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.feature_name)
        TextView featureNameLabel;

        @BindView(R.id.feature_value)
        TextView featureValueLabel;

        FeaturesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        private void bind(FeatureItem feature) {
            featureNameLabel.setText(feature.getName());
            featureValueLabel.setText(feature.getValue());
        }
    }
}
