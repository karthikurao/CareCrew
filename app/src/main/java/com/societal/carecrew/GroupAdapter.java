// GroupAdapter.java
package com.societal.carecrew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private List<Group> groupList;
    private GroupsActivity groupsActivity;

    public GroupAdapter(List<Group> groupList, GroupsActivity groupsActivity) {
        this.groupList = groupList;
        this.groupsActivity = groupsActivity;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false); // Inflate your item_group layout
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groupList.get(position);
        holder.groupNameTextView.setText(group.getName()); // Assuming your Group class has a getName() method

        // Set other views in the holder based on your Group object

        holder.itemView.setOnClickListener(v -> {
            // Call the showGroupDetails method in your GroupsActivity
            groupsActivity.showGroupDetails(group);
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView groupNameTextView;
        // Add other views here as needed (e.g., ImageView for group image, etc.)

        public GroupViewHolder(View itemView) {
            super(itemView);

            // Initialize other views here
        }
    }
}