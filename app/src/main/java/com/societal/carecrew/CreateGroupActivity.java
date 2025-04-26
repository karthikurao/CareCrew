// CreateGroupActivity.java
package com.societal.carecrew;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.societal.carecrew.databinding.ActivityCreateGroupBinding;

import java.util.HashMap;
import java.util.Map;

public class CreateGroupActivity extends AppCompatActivity {

    private ActivityCreateGroupBinding binding;
    private DatabaseReference groupsRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        groupsRef = FirebaseDatabase.getInstance().getReference("groups");

        binding.createGroupButton.setOnClickListener(v -> {
            String groupName = binding.groupNameEditText.getText().toString().trim();
            String groupDescription = binding.groupDescriptionEditText.getText().toString().trim();

            // Basic validation
            if (groupName.isEmpty() || groupDescription.isEmpty()) {
                Toast.makeText(CreateGroupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new group object
            String groupId = groupsRef.push().getKey();
            Group newGroup = new Group(groupId, groupName, groupDescription, currentUser.getUid());

            // Create the "members" node and add the creator as the first member
            Map<String, Object> initialMembers = new HashMap<>();
            initialMembers.put(currentUser.getUid(), true);
            newGroup.setMembers(initialMembers);

            // Add the new group to the database
            groupsRef.child(groupId).setValue(newGroup)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(CreateGroupActivity.this, "Group created successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(CreateGroupActivity.this, "Failed to create group", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}