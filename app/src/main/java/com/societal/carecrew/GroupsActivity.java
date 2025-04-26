// GroupsActivity.java
package com.societal.carecrew;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.societal.carecrew.databinding.ActivityGroupsBinding;

import java.util.ArrayList;
import java.util.List;

public class GroupsActivity extends AppCompatActivity {

    private ActivityGroupsBinding binding;
    private GroupAdapter groupAdapter;
    private List<Group> groupList;
    private DatabaseReference groupsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize RecyclerView
        binding.groupsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupList = new ArrayList<>();
        groupAdapter = new GroupAdapter(groupList, this);
        binding.groupsRecyclerView.setAdapter(groupAdapter);

        // Fetch groups from Firebase
        groupsRef = FirebaseDatabase.getInstance().getReference("groups");
        fetchGroups();

        // Handle create group button click
        binding.createGroupButton.setOnClickListener(v -> {
            Intent intent = new Intent(GroupsActivity.this, CreateGroupActivity.class);
            startActivity(intent);
        });

        // Set up BottomNavigationView
        binding.navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(GroupsActivity.this, HomePageActivity.class));
            } else if (itemId == R.id.navigation_maps) {
                startActivity(new Intent(GroupsActivity.this, MapsActivity.class));
            } else if (itemId == R.id.navigation_groups) {
                // Already on GroupsActivity, no action needed
            } else if (itemId == R.id.navigation_profile) {
                startActivity(new Intent(GroupsActivity.this, ProfileActivity.class));
            }
            return true;
        });

        // Set the selected item in the BottomNavigationView
        binding.navView.setSelectedItemId(R.id.navigation_groups);
    }

    // Method to show group details
    public void showGroupDetails(Group group) {
        Intent intent = new Intent(GroupsActivity.this, GroupDetailActivity.class);
        intent.putExtra("groupId", group.getGroupId());
        startActivity(intent);
    }

    private void fetchGroups() {
        groupsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Group group = snapshot.getValue(Group.class);
                    if (group != null) {
                        groupList.add(group);
                    }
                }
                groupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(GroupsActivity.this, "Failed to fetch groups: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("GroupsActivity", "Failed to fetch groups: " + databaseError.getMessage());
            }
        });
    }
}