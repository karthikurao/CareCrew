// GroupsActivity.java
package com.societal.carecrew;

import android.content.Intent;
import android.os.Bundle;
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
        groupAdapter = new GroupAdapter(groupList, this); // Pass the activity instance to the adapter
        binding.groupsRecyclerView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged(); // Call on the 'groupAdapter' instance

        // Fetch groups from Firebase
        groupsRef = FirebaseDatabase.getInstance().getReference("groups");
        fetchGroups();

        // Handle create group button click
        binding.createGroupButton.setOnClickListener(v -> {
            Intent intent = new Intent(GroupsActivity.this, CreateGroupActivity.class); // Replace with your CreateGroupActivity
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

    // Method to show group details (you'll need to implement this)
    public void showGroupDetails(Group group) {
        // Implement logic to open GroupDetailActivity and pass the group data
        // For example, using an Intent:
        // Intent intent = new Intent(GroupsActivity.this, GroupDetailActivity.class);
        // intent.putExtra("group", group);
        // startActivity(intent);
    }

    private void fetchGroups() {
        groupsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Group group = snapshot.getValue(Group.class); // Assuming you have a Group class
                    if (group != null) {
                        groupList.add(group);
                    }
                }
                groupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(GroupsActivity.this, "Failed to fetch groups: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}