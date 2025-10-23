// ChatActivity.java
package com.societal.carecrew;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.societal.carecrew.databinding.ActivityChatBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private ChatMessageAdapter chatMessageAdapter;
    private List<ChatMessage> messageList;
    private DatabaseReference chatRef;
    private DatabaseReference messagesRef;
    private String chatRoomId;
    private String chatName;
    private boolean isGroup;
    private String currentUserId;
    private String currentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatRoomId = getIntent().getStringExtra("chatRoomId");
        chatName = getIntent().getStringExtra("chatName");
        isGroup = getIntent().getBooleanExtra("isGroup", false);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        
        binding.chatTitle.setText(chatName);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        binding.messagesRecyclerView.setLayoutManager(layoutManager);
        messageList = new ArrayList<>();
        chatMessageAdapter = new ChatMessageAdapter(messageList);
        binding.messagesRecyclerView.setAdapter(chatMessageAdapter);

        binding.backButton.setOnClickListener(v -> finish());

        binding.sendButton.setOnClickListener(v -> sendMessage());

        fetchCurrentUserName();
        fetchMessages();
    }

    private void fetchCurrentUserName() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HelperClass user = dataSnapshot.getValue(HelperClass.class);
                if (user != null) {
                    currentUserName = user.getName();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ChatActivity", "Failed to fetch user name: " + databaseError.getMessage());
            }
        });
    }

    private void fetchMessages() {
        messagesRef = FirebaseDatabase.getInstance().getReference("chats").child(chatRoomId).child("messages");
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage message = snapshot.getValue(ChatMessage.class);
                    if (message != null) {
                        messageList.add(message);
                    }
                }
                chatMessageAdapter.notifyDataSetChanged();
                if (messageList.size() > 0) {
                    binding.messagesRecyclerView.scrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, "Failed to fetch messages: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ChatActivity", "Failed to fetch messages: " + databaseError.getMessage());
            }
        });
    }

    private void sendMessage() {
        String messageText = binding.messageInput.getText().toString().trim();
        if (TextUtils.isEmpty(messageText)) {
            return;
        }

        String messageId = messagesRef.push().getKey();
        long timestamp = System.currentTimeMillis();

        ChatMessage message = new ChatMessage(messageId, currentUserId, currentUserName, messageText, timestamp);

        messagesRef.child(messageId).setValue(message).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                binding.messageInput.setText("");
                updateChatRoom(messageText, timestamp);
            } else {
                Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateChatRoom(String lastMessage, long timestamp) {
        chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatRoomId);
        Map<String, Object> updates = new HashMap<>();
        updates.put("lastMessage", lastMessage);
        updates.put("lastMessageTime", timestamp);
        chatRef.updateChildren(updates);
    }
}
