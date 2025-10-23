# Firebase Database Quick Reference for Developers

This quick reference guide helps developers work with the Firebase Realtime Database in the Care Crew app.

## Common Database Operations

### User Operations

#### Get User Profile
```java
DatabaseReference userRef = FirebaseDatabase.getInstance()
    .getReference("users")
    .child(userId);

userRef.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        HelperClass user = snapshot.getValue(HelperClass.class);
        // Use user data
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.e("Database", "Error: " + error.getMessage());
    }
});
```

#### Update User Profile
```java
DatabaseReference userRef = FirebaseDatabase.getInstance()
    .getReference("users")
    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

Map<String, Object> updates = new HashMap<>();
updates.put("name", "New Name");
updates.put("bio", "Updated bio");

userRef.updateChildren(updates)
    .addOnSuccessListener(aVoid -> {
        // Success
    })
    .addOnFailureListener(e -> {
        // Error
    });
```

#### Update Leaderboard Stats
```java
String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
DatabaseReference userRef = FirebaseDatabase.getInstance()
    .getReference("users")
    .child(userId);

// Increment hours volunteered
userRef.child("hoursVolunteered").runTransaction(new Transaction.Handler() {
    @NonNull
    @Override
    public Transaction.Result doTransaction(@NonNull MutableData data) {
        Integer hours = data.getValue(Integer.class);
        if (hours == null) {
            data.setValue(1);
        } else {
            data.setValue(hours + 1);
        }
        return Transaction.success(data);
    }

    @Override
    public void onComplete(@Nullable DatabaseError error, boolean committed, 
                          @Nullable DataSnapshot snapshot) {
        if (committed) {
            updateLeaderboard(userId);
        }
    }
});
```

### Group Operations

#### Create a Group
```java
DatabaseReference groupsRef = FirebaseDatabase.getInstance()
    .getReference("groups");
DatabaseReference userGroupsRef = FirebaseDatabase.getInstance()
    .getReference("userGroups");

String groupId = groupsRef.push().getKey();
String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

Group newGroup = new Group(groupId, name, description, userId);
Map<String, Object> initialMembers = new HashMap<>();
initialMembers.put(userId, true);
newGroup.setMembers(initialMembers);

// Write to multiple locations
Map<String, Object> updates = new HashMap<>();
updates.put("groups/" + groupId, newGroup);
updates.put("userGroups/" + userId + "/" + groupId, new UserGroup(groupId, name, "admin", System.currentTimeMillis()));

FirebaseDatabase.getInstance().getReference().updateChildren(updates);
```

#### Join a Group
```java
String groupId = "group123";
String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

Map<String, Object> updates = new HashMap<>();
updates.put("groups/" + groupId + "/members/" + userId, true);
updates.put("userGroups/" + userId + "/" + groupId, true);

FirebaseDatabase.getInstance().getReference().updateChildren(updates);
```

#### Get User's Groups
```java
DatabaseReference userGroupsRef = FirebaseDatabase.getInstance()
    .getReference("userGroups")
    .child(userId);

userGroupsRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        List<String> groupIds = new ArrayList<>();
        for (DataSnapshot child : snapshot.getChildren()) {
            groupIds.add(child.getKey());
        }
        // Fetch full group details using groupIds
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.e("Database", "Error: " + error.getMessage());
    }
});
```

### Post Operations

#### Create a Post
```java
DatabaseReference postsRef = FirebaseDatabase.getInstance()
    .getReference("posts");

String postId = postsRef.push().getKey();
String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

Post newPost = new Post(userId, username, caption, imageUrl, postId);

postsRef.child(postId).setValue(newPost)
    .addOnSuccessListener(aVoid -> {
        // Success
    })
    .addOnFailureListener(e -> {
        // Error
    });
```

#### Like a Post
```java
String postId = "post123";
String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

DatabaseReference postRef = FirebaseDatabase.getInstance()
    .getReference("posts")
    .child(postId);

Map<String, Object> updates = new HashMap<>();
updates.put("likes/" + userId, true);

postRef.updateChildren(updates);

// Increment like count
postRef.child("likesCount").runTransaction(new Transaction.Handler() {
    @NonNull
    @Override
    public Transaction.Result doTransaction(@NonNull MutableData data) {
        Integer count = data.getValue(Integer.class);
        if (count == null) {
            data.setValue(1);
        } else {
            data.setValue(count + 1);
        }
        return Transaction.success(data);
    }

    @Override
    public void onComplete(@Nullable DatabaseError error, boolean committed, 
                          @Nullable DataSnapshot snapshot) {
        // Complete
    }
});
```

#### Add Comment
```java
DatabaseReference commentsRef = FirebaseDatabase.getInstance()
    .getReference("comments")
    .child(postId);

String commentId = commentsRef.push().getKey();
Comment newComment = new Comment(commentId, userId, username, 
    commentText, ServerValue.TIMESTAMP, postId, profileImageUrl);

commentsRef.child(commentId).setValue(newComment);

// Increment comment count
DatabaseReference postRef = FirebaseDatabase.getInstance()
    .getReference("posts")
    .child(postId)
    .child("commentsCount");

postRef.runTransaction(new Transaction.Handler() {
    @NonNull
    @Override
    public Transaction.Result doTransaction(@NonNull MutableData data) {
        Integer count = data.getValue(Integer.class);
        if (count == null) {
            data.setValue(1);
        } else {
            data.setValue(count + 1);
        }
        return Transaction.success(data);
    }

    @Override
    public void onComplete(@Nullable DatabaseError error, boolean committed, 
                          @Nullable DataSnapshot snapshot) {
        // Complete
    }
});
```

### Opportunity Operations

#### Create an Opportunity
```java
DatabaseReference opportunitiesRef = FirebaseDatabase.getInstance()
    .getReference("opportunities");

String opportunityId = opportunitiesRef.push().getKey();

Opportunity opportunity = new Opportunity();
opportunity.setOpportunityId(opportunityId);
opportunity.setTitle(title);
opportunity.setDescription(description);
opportunity.setDate(date);
opportunity.setLocation(location);
opportunity.setCreatedBy(userId);
opportunity.setCreatedAt(System.currentTimeMillis());
opportunity.setStatus("active");

opportunitiesRef.child(opportunityId).setValue(opportunity);
```

#### Join an Opportunity
```java
String opportunityId = "opp123";
String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

DatabaseReference oppRef = FirebaseDatabase.getInstance()
    .getReference("opportunities")
    .child(opportunityId);

Map<String, Object> updates = new HashMap<>();
updates.put("participants/" + userId, true);

oppRef.updateChildren(updates);

// Increment participant count
oppRef.child("currentVolunteers").runTransaction(new Transaction.Handler() {
    @NonNull
    @Override
    public Transaction.Result doTransaction(@NonNull MutableData data) {
        Integer count = data.getValue(Integer.class);
        if (count == null) {
            data.setValue(1);
        } else {
            data.setValue(count + 1);
        }
        return Transaction.success(data);
    }

    @Override
    public void onComplete(@Nullable DatabaseError error, boolean committed, 
                          @Nullable DataSnapshot snapshot) {
        // Complete
    }
});
```

### Chat Operations

#### Create a Group Chat
```java
DatabaseReference conversationsRef = FirebaseDatabase.getInstance()
    .getReference("chat/conversations");

String conversationId = conversationsRef.push().getKey();

Map<String, Object> conversation = new HashMap<>();
conversation.put("conversationId", conversationId);
conversation.put("type", "group");
conversation.put("groupId", groupId);
conversation.put("name", groupName);
conversation.put("createdAt", ServerValue.TIMESTAMP);

Map<String, Object> participants = new HashMap<>();
for (String memberId : memberIds) {
    participants.put(memberId, true);
}
conversation.put("participants", participants);

conversationsRef.child(conversationId).setValue(conversation);
```

#### Send a Message
```java
DatabaseReference messagesRef = FirebaseDatabase.getInstance()
    .getReference("chat/messages")
    .child(conversationId);

String messageId = messagesRef.push().getKey();

Map<String, Object> message = new HashMap<>();
message.put("messageId", messageId);
message.put("conversationId", conversationId);
message.put("senderId", userId);
message.put("senderName", username);
message.put("senderImageUrl", profileImageUrl);
message.put("text", messageText);
message.put("timestamp", ServerValue.TIMESTAMP);
message.put("type", "text");

messagesRef.child(messageId).setValue(message);

// Update conversation's last message
DatabaseReference conversationRef = FirebaseDatabase.getInstance()
    .getReference("chat/conversations")
    .child(conversationId);

Map<String, Object> lastMessage = new HashMap<>();
lastMessage.put("text", messageText);
lastMessage.put("senderId", userId);
lastMessage.put("timestamp", ServerValue.TIMESTAMP);

conversationRef.child("lastMessage").setValue(lastMessage);
conversationRef.child("updatedAt").setValue(ServerValue.TIMESTAMP);
```

#### Listen to New Messages
```java
DatabaseReference messagesRef = FirebaseDatabase.getInstance()
    .getReference("chat/messages")
    .child(conversationId);

messagesRef.orderByChild("timestamp")
    .limitToLast(50)
    .addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, 
                                @Nullable String previousChildName) {
            Message message = snapshot.getValue(Message.class);
            // Add message to chat UI
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, 
                                  @Nullable String previousChildName) {
            // Handle message update
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            // Handle message deletion
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, 
                                @Nullable String previousChildName) {
            // Handle message move
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e("Chat", "Error: " + error.getMessage());
        }
    });
```

### Leaderboard Operations

#### Update Leaderboard Entry
```java
String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

// Calculate score
int score = (hoursVolunteered * 10) + (opportunitiesParticipated * 20) + (groupsJoined * 15);

DatabaseReference leaderboardRef = FirebaseDatabase.getInstance()
    .getReference("leaderboard/allTime")
    .child(userId);

Map<String, Object> entry = new HashMap<>();
entry.put("userId", userId);
entry.put("username", username);
entry.put("name", name);
entry.put("profileImageUrl", profileImageUrl);
entry.put("hoursVolunteered", hoursVolunteered);
entry.put("opportunitiesParticipated", opportunitiesParticipated);
entry.put("groupsJoined", groupsJoined);
entry.put("score", score);
entry.put("updatedAt", ServerValue.TIMESTAMP);

leaderboardRef.setValue(entry);
```

#### Get Top 10 Volunteers
```java
DatabaseReference leaderboardRef = FirebaseDatabase.getInstance()
    .getReference("leaderboard/allTime");

leaderboardRef.orderByChild("score")
    .limitToLast(10)
    .addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            List<LeaderboardEntry> topVolunteers = new ArrayList<>();
            for (DataSnapshot child : snapshot.getChildren()) {
                LeaderboardEntry entry = child.getValue(LeaderboardEntry.class);
                topVolunteers.add(0, entry); // Reverse order (highest first)
            }
            // Display leaderboard
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e("Leaderboard", "Error: " + error.getMessage());
        }
    });
```

## Query Patterns

### Pagination
```java
// First page
query.limitToFirst(20);

// Next pages using the last item's key
query.startAfter(lastKey).limitToFirst(20);
```

### Filtering
```java
// Get active opportunities
DatabaseReference ref = FirebaseDatabase.getInstance()
    .getReference("opportunities");

ref.orderByChild("status")
    .equalTo("active")
    .addListenerForSingleValueEvent(/* listener */);
```

### Sorting
```java
// Get recent posts
DatabaseReference ref = FirebaseDatabase.getInstance()
    .getReference("posts");

ref.orderByChild("timestamp")
    .limitToLast(20)
    .addListenerForSingleValueEvent(/* listener */);
```

## Offline Support

Enable offline persistence (call once when app starts):
```java
FirebaseDatabase.getInstance().setPersistenceEnabled(true);
```

Keep data synced even when offline:
```java
DatabaseReference ref = FirebaseDatabase.getInstance()
    .getReference("users")
    .child(userId);

ref.keepSynced(true);
```

## Best Practices

1. **Use Transactions for Counters**: Always use transactions when incrementing/decrementing counts
2. **Limit Listeners**: Remove listeners when no longer needed to prevent memory leaks
3. **Use Single Value Events**: When you only need data once, use `addListenerForSingleValueEvent()`
4. **Batch Updates**: Use multi-path updates for atomic operations across multiple locations
5. **Server Timestamps**: Use `ServerValue.TIMESTAMP` for consistent timestamps
6. **Null Checks**: Always check if snapshot exists before reading values
7. **Error Handling**: Always implement `onCancelled()` to handle errors

## Common Pitfalls

1. **Not removing listeners**: Always call `removeEventListener()` in `onDestroy()` or `onStop()`
2. **Reading entire lists**: Use queries with limits to avoid reading too much data
3. **Not handling offline mode**: Plan for offline scenarios
4. **Forgetting null constructors**: Firebase requires no-argument constructors in model classes
5. **Complex nested queries**: Firebase doesn't support complex queries - denormalize data instead

## Testing Tips

1. Use Firebase Emulator Suite for local testing
2. Create separate database instances for development and production
3. Use Firebase Rules Simulator to test security rules
4. Monitor database operations in Firebase Console

## Reference

- [DATABASE_STRUCTURE.md](DATABASE_STRUCTURE.md) - Complete database schema
- [FIREBASE_SETUP.md](FIREBASE_SETUP.md) - Setup instructions
- [Firebase Documentation](https://firebase.google.com/docs/database)
