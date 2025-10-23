// LeaderboardAdapter.java
package com.societal.carecrew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private List<LeaderboardItem> leaderboardItems;
    private Context context;

    public LeaderboardAdapter(List<LeaderboardItem> leaderboardItems, Context context) {
        this.leaderboardItems = leaderboardItems;
        this.context = context;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        LeaderboardItem item = leaderboardItems.get(position);

        holder.rankTextView.setText(String.valueOf(item.getRank()));
        holder.nameTextView.setText(item.getName());
        holder.scoreTextView.setText(String.valueOf(item.getTotalScore()));

        // Format stats text
        String statsText = item.getHoursVolunteered() + " hrs • " +
                item.getOpportunitiesParticipated() + " opportunities";
        holder.statsTextView.setText(statsText);

        // Load profile image using Glide
        if (item.getProfileImageUrl() != null && !item.getProfileImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(item.getProfileImageUrl())
                    .placeholder(R.drawable.ic_person)
                    .circleCrop()
                    .into(holder.profileImageView);
        } else {
            holder.profileImageView.setImageResource(R.drawable.ic_person);
        }

        // Highlight top 3 with different colors
        if (item.getRank() == 1) {
            holder.rankTextView.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        } else if (item.getRank() == 2) {
            holder.rankTextView.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        } else if (item.getRank() == 3) {
            holder.rankTextView.setTextColor(context.getResources().getColor(android.R.color.holo_orange_light));
        } else {
            holder.rankTextView.setTextColor(context.getResources().getColor(android.R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return leaderboardItems.size();
    }

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        TextView rankTextView, nameTextView, statsTextView, scoreTextView;
        ImageView profileImageView;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            rankTextView = itemView.findViewById(R.id.rankTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            statsTextView = itemView.findViewById(R.id.statsTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
        }
    }
}
