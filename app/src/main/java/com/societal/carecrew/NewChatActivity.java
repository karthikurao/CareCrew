// NewChatActivity.java
package com.societal.carecrew;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.societal.carecrew.databinding.ActivityNewChatBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewChatActivity extends AppCompatActivity implements VolunteerAdapter.OnVolunteerClickListener {

    private ActivityNewChatBinding binding;
    private VolunteerAdapter volunteerAdapter;
    private List<Volunteer> volunteerList;
    private DatabaseReference usersRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        binding.volunteersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        volunteerList = new ArrayList<>();
        volunteerAdapter = new VolunteerAdapter(volunteerList, this);
        binding.volunteersRecyclerView.setAdapter(volunteerAdapter);

        binding.backButton.setOnClickListener(v -> finish());

        fetchVolunteers();
    }

    private void fetchVolunteers() {
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                volunteerList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = snapshot.getKey();
                    if (!userId.equals(currentUserId)) {
                        HelperClass user = snapshot.getValue(HelperClass.class);
                        if (user != null) {
                            Volunteer volunteer = new Volunteer(userId, user.getName(), user.getUsername());
                            volunteerList.add(volunteer);
                        }
                    }
                }
                volunteerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NewChatActivity.this, "Failed to fetch volunteers: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("NewChatActivity", "Failed to fetch volunteers: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onVolunteerClick(Volunteer volunteer) {
        createOrOpenChat(volunteer);
    }

    private void createOrOpenChat(Volunteer volunteer) {
        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference("chats");
        
        String chatRoomId = getChatRoomId(currentUserId, volunteer.getUserId());
        
        chatsRef.child(chatRoomId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Map<String, Boolean> participants = new HashMap<>();
                    participants.put(currentUserId, true);
                    participants.put(volunteer.getUserId(), true);
                    
                    ChatRoom chatRoom = new ChatRoom(
                        chatRoomId,
                        participants,
                        false,
                        null,
                        "No messages yet",
                        System.currentTimeMillis(),
                        volunteer.getName()
                    );
                    
                    chatsRef.child(chatRoomId).setValue(chatRoom);
                }
                
                Intent intent = new Intent(NewChatActivity.this, ChatActivity.class);
                intent.putExtra("chatRoomId", chatRoomId);
                intent.putExtra("chatName", volunteer.getName());
                intent.putExtra("isGroup", false);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NewChatActivity.this, "Failed to create chat", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getChatRoomId(String userId1, String userId2) {
        if (userId1.compareTo(userId2) < 0) {
            return userId1 + "_" + userId2;
        } else {
            return userId2 + "_" + userId1;
        }
    }
}
