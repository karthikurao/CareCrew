// ChatListActivity.java
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
import com.societal.carecrew.databinding.ActivityChatListBinding;
import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity implements ChatRoomAdapter.OnChatRoomClickListener {

    private ActivityChatListBinding binding;
    private ChatRoomAdapter chatRoomAdapter;
    private List<ChatRoom> chatRoomList;
    private DatabaseReference chatsRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRoomList = new ArrayList<>();
        chatRoomAdapter = new ChatRoomAdapter(chatRoomList, this, this);
        binding.chatRecyclerView.setAdapter(chatRoomAdapter);

        binding.fabNewChat.setOnClickListener(v -> {
            Intent intent = new Intent(ChatListActivity.this, NewChatActivity.class);
            startActivity(intent);
        });

        binding.navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(ChatListActivity.this, HomePageActivity.class));
            } else if (itemId == R.id.navigation_maps) {
                startActivity(new Intent(ChatListActivity.this, MapsActivity.class));
            } else if (itemId == R.id.navigation_groups) {
                startActivity(new Intent(ChatListActivity.this, GroupsActivity.class));
            } else if (itemId == R.id.navigation_chat) {
                // Already on chat screen
            } else if (itemId == R.id.navigation_profile) {
                startActivity(new Intent(ChatListActivity.this, ProfileActivity.class));
            }
            return true;
        });

        binding.navView.setSelectedItemId(R.id.navigation_chat);

        fetchChatRooms();
    }

    private void fetchChatRooms() {
        chatsRef = FirebaseDatabase.getInstance().getReference("chats");
        chatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatRoomList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatRoom chatRoom = snapshot.getValue(ChatRoom.class);
                    if (chatRoom != null && chatRoom.getParticipants() != null 
                            && chatRoom.getParticipants().containsKey(currentUserId)) {
                        chatRoomList.add(chatRoom);
                    }
                }
                chatRoomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatListActivity.this, "Failed to fetch chats: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ChatListActivity", "Failed to fetch chats: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onChatRoomClick(ChatRoom chatRoom) {
        Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
        intent.putExtra("chatRoomId", chatRoom.getChatRoomId());
        intent.putExtra("chatName", chatRoom.getChatName());
        intent.putExtra("isGroup", chatRoom.isGroup());
        startActivity(intent);
    }
}
