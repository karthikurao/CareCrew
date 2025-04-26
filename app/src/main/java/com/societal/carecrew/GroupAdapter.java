// GroupAdapter.java
package com.societal.carecrew;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groupList.get(position);

        holder.groupNameTextView.setText(group.getName());

        // Fetch the member count from Firebase
        DatabaseReference groupMembersRef = FirebaseDatabase.getInstance().getReference("groups")
                .child(group.getGroupId())
                .child("members");

        groupMembersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long memberCount = snapshot.getChildrenCount();
                holder.groupMembersCountTextView.setText(String.valueOf(memberCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("GroupAdapter", "Failed to get member count: " + error.getMessage());
            }
        });

        Glide.with(holder.itemView.getContext())
                .load(group.getGroupImageUrl())
                .placeholder(R.drawable.ic_group)
                .into(holder.groupImageView);

        holder.itemView.setOnClickListener(v -> {
            groupsActivity.showGroupDetails(group);
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        ImageView groupImageView;
        TextView groupNameTextView;
        ImageView groupMembersCountImageView;
        TextView groupMembersCountTextView;

        public GroupViewHolder(View itemView) {
            super(itemView);
            groupImageView = itemView.findViewById(R.id.groupImageView);
            groupNameTextView = itemView.findViewById(R.id.groupNameTextView);
            groupMembersCountImageView = itemView.findViewById(R.id.groupMembersCountImageView);
            groupMembersCountTextView = itemView.findViewById(R.id.groupMembersCountTextView);
        }
    }
}