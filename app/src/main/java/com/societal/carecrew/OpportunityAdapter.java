package com.societal.carecrew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OpportunityAdapter extends RecyclerView.Adapter<OpportunityAdapter.OpportunityViewHolder> {

    private List<Opportunity> opportunityList;
    private OpportunityDetailsActivity opportunityDetailsActivity;
    public OpportunityAdapter(List<Opportunity> opportunityList, OpportunityDetailsActivity opportunityDetailsActivity) {
        this.opportunityList = opportunityList;
        this.opportunityDetailsActivity = opportunityDetailsActivity;
    }

    @NonNull
    @Override
    public OpportunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_opportunity, parent, false);
        return new OpportunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OpportunityViewHolder holder, int position) {
        Opportunity opportunity = opportunityList.get(position);
        holder.titleTextView.setText(opportunity.getTitle()); // Assuming Opportunity has a getTitle() method
        holder.categoryTextView.setText(opportunity.getCategory()); // Assuming Opportunity has a getCategory() method

        // You can add an OnClickListener here to handle clicks on opportunity items
        // and call the showOpportunityDetails() method in the activity
    }

    @Override
    public int getItemCount() {
        return opportunityList.size();
    }

    public static class OpportunityViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView categoryTextView;

        public OpportunityViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.opportunityTitleTextView);
            categoryTextView = itemView.findViewById(R.id.opportunityCategoryTextView);
        }
    }
}