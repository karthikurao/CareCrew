// VolunteerAdapter.java
package com.societal.carecrew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VolunteerAdapter extends RecyclerView.Adapter<VolunteerAdapter.VolunteerViewHolder> {

    private List<Volunteer> volunteers;
    private OnVolunteerClickListener listener;

    public interface OnVolunteerClickListener {
        void onVolunteerClick(Volunteer volunteer);
    }

    public VolunteerAdapter(List<Volunteer> volunteers, OnVolunteerClickListener listener) {
        this.volunteers = volunteers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VolunteerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_volunteer, parent, false);
        return new VolunteerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VolunteerViewHolder holder, int position) {
        Volunteer volunteer = volunteers.get(position);
        holder.volunteerName.setText(volunteer.getName());
        holder.volunteerUsername.setText("@" + volunteer.getUsername());
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onVolunteerClick(volunteer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return volunteers.size();
    }

    static class VolunteerViewHolder extends RecyclerView.ViewHolder {
        TextView volunteerName;
        TextView volunteerUsername;

        VolunteerViewHolder(@NonNull View itemView) {
            super(itemView);
            volunteerName = itemView.findViewById(R.id.volunteerName);
            volunteerUsername = itemView.findViewById(R.id.volunteerUsername);
        }
    }
}
