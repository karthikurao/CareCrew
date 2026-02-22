// GroupDetailActivity.java
package com.societal.carecrew;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.societal.carecrew.databinding.ActivityGroupDetailsBinding;
import java.util.HashMap;
import java.util.Map;

public class GroupDetailActivity extends AppCompatActivity {

    private ActivityGroupDetailsBinding binding;
    private String groupId;
    private Group currentGroup;
    private DatabaseReference groupRef;
    private DatabaseReference chatsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        groupId = getIntent().getStringExtra("groupId");
        
        if (groupId == null) {
            Toast.makeText(this, "Invalid group", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        groupRef = FirebaseDatabase.getInstance().getReference("groups").child(groupId);
        chatsRef = FirebaseDatabase.getInstance().getReference("chats");

        loadGroupDetails();
        
        binding.groupChatButton.setOnClickListener(v -> openGroupChat());
    }

    private void loadGroupDetails() {
        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentGroup = dataSnapshot.getValue(Group.class);
                if (currentGroup != null) {
                    binding.groupNameTextView.setText(currentGroup.getName());
                    binding.groupDescriptionTextView.setText(currentGroup.getDescription());
                    binding.createdByTextView.setText("Created by: " + currentGroup.getCreatedBy());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(GroupDetailActivity.this, "Failed to load group details", Toast.LENGTH_SHORT).show();
                Log.e("GroupDetailActivity", "Failed to load group: " + databaseError.getMessage());
            }
        });
    }

    private void openGroupChat() {
        if (currentGroup == null) {
            Toast.makeText(this, "Group not loaded yet", Toast.LENGTH_SHORT).show();
            return;
        }

        String chatRoomId = "group_" + groupId;
        
        chatsRef.child(chatRoomId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Map<String, Boolean> participants = new HashMap<>();
                    if (currentGroup.getMembers() != null) {
                        for (String memberId : currentGroup.getMembers().keySet()) {
                            participants.put(memberId, true);
                        }
                    }
                    
                    ChatRoom chatRoom = new ChatRoom(
                        chatRoomId,
                        participants,
                        true,
                        groupId,
                        "No messages yet",
                        System.currentTimeMillis(),
                        currentGroup.getName() + " Chat"
                    );
                    
                    chatsRef.child(chatRoomId).setValue(chatRoom);
                }
                
                Intent intent = new Intent(GroupDetailActivity.this, ChatActivity.class);
                intent.putExtra("chatRoomId", chatRoomId);
                intent.putExtra("chatName", currentGroup.getName() + " Chat");
                intent.putExtra("isGroup", true);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(GroupDetailActivity.this, "Failed to create group chat", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
