package com.societal.carecrew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private Context context;

    public EventAdapter(List<Event> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_admin, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.titleText.setText(event.getTitle());
        holder.descriptionText.setText(event.getDescription());
        holder.dateText.setText(event.getDate());
        holder.locationText.setText(event.getLocation());
        holder.categoryText.setText(event.getCategory() != null ? event.getCategory() : "General");
        holder.participantsText.setText("Participants: " + event.getParticipantCount());

        holder.deleteButton.setOnClickListener(v -> {
            if (context instanceof AdminDashboardActivity) {
                ((AdminDashboardActivity) context).deleteEvent(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, descriptionText, dateText, locationText, categoryText, participantsText;
        ImageButton deleteButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.event_title);
            descriptionText = itemView.findViewById(R.id.event_description);
            dateText = itemView.findViewById(R.id.event_date);
            locationText = itemView.findViewById(R.id.event_location);
            categoryText = itemView.findViewById(R.id.event_category);
            participantsText = itemView.findViewById(R.id.event_participants);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
