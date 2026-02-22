package com.societal.carecrew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OpportunityAdapter extends RecyclerView.Adapter<OpportunityAdapter.OpportunityViewHolder> {

    private final List<Opportunity> opportunityList;
    private final OpportunityDetailsActivity opportunityDetailsActivity;

    public OpportunityAdapter(List<Opportunity> opportunityList, OpportunityDetailsActivity opportunityDetailsActivity) {
        this.opportunityList = opportunityList;
        this.opportunityDetailsActivity = opportunityDetailsActivity;
        this.useHorizontalLayout = false;
    }

    public OpportunityAdapter(List<Opportunity> opportunityList, OpportunityDetailsActivity opportunityDetailsActivity, boolean useHorizontalLayout) {
        this.opportunityList = opportunityList;
        this.opportunityDetailsActivity = opportunityDetailsActivity;
        this.useHorizontalLayout = useHorizontalLayout;
    }

    @NonNull
    @Override
    public OpportunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = useHorizontalLayout ? R.layout.item_opportunity_horizontal : R.layout.item_opportunity;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new OpportunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OpportunityViewHolder holder, int position) {
        Opportunity opportunity = opportunityList.get(position);
        holder.titleTextView.setText(opportunity.getTitle());
        holder.categoryTextView.setText(opportunity.getCategory());

        // Show location if available
        if (holder.locationTextView != null) {
            String location = opportunity.getLocation();
            if (location != null && !location.isEmpty()) {
                holder.locationTextView.setText(location);
                holder.locationTextView.setVisibility(View.VISIBLE);
                if (holder.locationRow != null) holder.locationRow.setVisibility(View.VISIBLE);
            } else {
                if (holder.locationRow != null) holder.locationRow.setVisibility(View.GONE);
            }
        }

        // Show urgent badge if applicable
        if (holder.urgentBadge != null) {
            holder.urgentBadge.setVisibility(opportunity.isUrgent() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return opportunityList.size();
    }

    public static class OpportunityViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView categoryTextView;
        TextView locationTextView;
        View locationRow;
        TextView urgentBadge;

        public OpportunityViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.opportunityTitleTextView);
            categoryTextView = itemView.findViewById(R.id.opportunityCategoryTextView);
            locationTextView = itemView.findViewById(R.id.opportunityLocationTextView);
            locationRow = itemView.findViewById(R.id.locationRow);
            urgentBadge = itemView.findViewById(R.id.urgentBadge);
        }
    }
}
