package com.societal.carecrew;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.societal.carecrew.databinding.ActivityGroupDetailsBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupDetailsActivity extends AppCompatActivity {

    private ActivityGroupDetailsBinding binding;
    private DatabaseReference groupRef;
    private DatabaseReference userRef;
    private String groupId;
    private String currentUserId;
    private MemberAdapter memberAdapter;
    private List<String> memberIds;
    private boolean isMember = false;
    private String creatorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get group ID from intent
        groupId = getIntent().getStringExtra("groupId");
        if (groupId == null) {
            Toast.makeText(this, "Invalid group", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Get current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        }

        // Initialize Firebase references
        groupRef = FirebaseDatabase.getInstance().getReference("groups").child(groupId);
        userRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize RecyclerView for members
        binding.membersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        memberIds = new ArrayList<>();
        memberAdapter = new MemberAdapter(memberIds);
        binding.membersRecyclerView.setAdapter(memberAdapter);

        // Load group details
        loadGroupDetails();

        // Set up button listeners
        binding.joinLeaveButton.setOnClickListener(v -> handleJoinLeave());
        binding.editGroupButton.setOnClickListener(v -> {
            // TODO: Implement edit group functionality
            Toast.makeText(this, "Edit functionality coming soon", Toast.LENGTH_SHORT).show();
        });
        binding.inviteMembersButton.setOnClickListener(v -> {
            // TODO: Implement invite members functionality
            Toast.makeText(this, "Invite functionality coming soon", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadGroupDetails() {
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Group group = snapshot.getValue(Group.class);
                    if (group != null) {
                        // Set group details
                        binding.groupNameTextView.setText(group.getName());
                        binding.groupDescriptionTextView.setText(group.getDescription());
                        
                        creatorId = group.getCreatedBy();
                        
                        // Load creator name
                        if (creatorId != null) {
                            userRef.child(creatorId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                    String creatorName = userSnapshot.child("name").getValue(String.class);
                                    if (creatorName != null) {
                                        binding.createdByTextView.setText("Created by: " + creatorName);
                                    } else {
                                        binding.createdByTextView.setText("Created by: Unknown");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    binding.createdByTextView.setText("Created by: Unknown");
                                }
                            });
                        }

                        // Load members
                        loadMembers(snapshot);

                        // Update button visibility and text
                        updateUI();
                    }
                } else {
                    // Group has been deleted
                    Toast.makeText(GroupDetailsActivity.this, "This group no longer exists", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GroupDetailsActivity.this, "Failed to load group details", Toast.LENGTH_SHORT).show();
                Log.e("GroupDetailsActivity", "Error: " + error.getMessage());
            }
        });
    }

    private void loadMembers(DataSnapshot groupSnapshot) {
        memberIds.clear();
        isMember = false; // Reset membership status
        
        DataSnapshot membersSnapshot = groupSnapshot.child("members");
        if (membersSnapshot.exists()) {
            for (DataSnapshot memberSnapshot : membersSnapshot.getChildren()) {
                String memberId = memberSnapshot.getKey();
                if (memberId != null) {
                    memberIds.add(memberId);
                    
                    // Check if current user is a member
                    if (memberId.equals(currentUserId)) {
                        isMember = true;
                    }
                }
            }
        }
        
        memberAdapter.notifyDataSetChanged();
    }

    private void updateUI() {
        // Update join/leave button
        if (isMember) {
            // If user is the creator, hide the leave button
            if (currentUserId != null && currentUserId.equals(creatorId)) {
                binding.joinLeaveButton.setVisibility(View.GONE);
            } else {
                binding.joinLeaveButton.setVisibility(View.VISIBLE);
                binding.joinLeaveButton.setText("Leave Group");
            }
        } else {
            binding.joinLeaveButton.setVisibility(View.VISIBLE);
            binding.joinLeaveButton.setText("Join Group");
        }

        // Show edit button only for the creator
        if (currentUserId != null && currentUserId.equals(creatorId)) {
            binding.editGroupButton.setVisibility(View.VISIBLE);
        } else {
            binding.editGroupButton.setVisibility(View.GONE);
        }
    }

    private void handleJoinLeave() {
        if (currentUserId == null) {
            Toast.makeText(this, "Please log in to join groups", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prevent creator from leaving their own group
        if (isMember && currentUserId.equals(creatorId)) {
            Toast.makeText(this, "Group creator cannot leave the group", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference membersRef = groupRef.child("members");

        if (isMember) {
            // Leave group
            membersRef.child(currentUserId).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(GroupDetailsActivity.this, "Left group successfully", Toast.LENGTH_SHORT).show();
                        isMember = false;
                        updateUI();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(GroupDetailsActivity.this, "Failed to leave group", Toast.LENGTH_SHORT).show();
                        Log.e("GroupDetailsActivity", "Error leaving group: " + e.getMessage());
                    });
        } else {
            // Join group
            Map<String, Object> memberData = new HashMap<>();
            memberData.put(currentUserId, true);

            membersRef.updateChildren(memberData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(GroupDetailsActivity.this, "Joined group successfully", Toast.LENGTH_SHORT).show();
                        isMember = true;
                        updateUI();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(GroupDetailsActivity.this, "Failed to join group", Toast.LENGTH_SHORT).show();
                        Log.e("GroupDetailsActivity", "Error joining group: " + e.getMessage());
                    });
        }
    }
}
