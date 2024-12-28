// VolunteerExperienceAdapter.java
package com.societal.carecrew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VolunteerExperienceAdapter extends RecyclerView.Adapter<VolunteerExperienceAdapter.VolunteerExperienceViewHolder> {

    private List<VolunteerExperience> experienceList;

    public VolunteerExperienceAdapter(List<VolunteerExperience> experienceList) {
        this.experienceList = experienceList;
    }

    @NonNull
    @Override
    public VolunteerExperienceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_volunteer_experience, parent, false); // Inflate your item_volunteer_experience layout
        return new VolunteerExperienceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VolunteerExperienceViewHolder holder, int position) {
        VolunteerExperience experience = experienceList.get(position);

        holder.organizationTextView.setText(experience.getOrganization());
        holder.roleTextView.setText(experience.getRole());
        holder.durationTextView.setText(experience.getDuration());
        holder.descriptionTextView.setText(experience.getDescription());
    }

    @Override
    public int getItemCount() {
        return experienceList.size();
    }

    public static class VolunteerExperienceViewHolder extends RecyclerView.ViewHolder {
        TextView organizationTextView;
        TextView roleTextView;
        TextView durationTextView;
        TextView descriptionTextView;

        public VolunteerExperienceViewHolder(View itemView) {
            super(itemView);
            organizationTextView = itemView.findViewById(R.id.organizationTextView); // Replace with actual IDs from your layout
            roleTextView = itemView.findViewById(R.id.roleTextView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}