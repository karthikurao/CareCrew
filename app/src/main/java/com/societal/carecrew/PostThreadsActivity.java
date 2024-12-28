// PostThreadsActivity.java
package com.societal.carecrew;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.societal.carecrew.databinding.ActivityPostThreadsBinding;

import java.util.ArrayList;
import java.util.List;

public class PostThreadsActivity extends AppCompatActivity {

    private ActivityPostThreadsBinding binding;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private DatabaseReference commentsRef;
    private FirebaseUser currentUser;
    private String currentUsername;
    private String currentProfileImageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostThreadsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String postId = getIntent().getStringExtra("postId");
        if (postId == null) {
            Log.e("PostThreadsActivity", "postId is null");
            finish();
            return;
        }

        commentsRef = FirebaseDatabase.getInstance().getReference("posts").child(postId).child("comments");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Log.e("PostThreadsActivity", "currentUser is null");
            finish();
            return;
        }

        // Fetch the current username and profile image URL from the database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HelperClass user = snapshot.getValue(HelperClass.class);
                    if (user != null) {
                        currentUsername = user.getUsername();
                        currentProfileImageUrl = user.getProfileImageUrl();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PostThreadsActivity", "Failed to fetch username: " + error.getMessage());
            }
        });

        binding.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList, currentUsername, postId); // Pass currentUsername and postId
        binding.commentsRecyclerView.setAdapter(commentAdapter);

        fetchComments();

        binding.addCommentButton.setOnClickListener(v -> {
            String commentText = binding.commentEditText.getText().toString().trim();
            if (commentText.isEmpty()) {
                Toast.makeText(PostThreadsActivity.this, "Please enter a comment", Toast.LENGTH_SHORT).show();
                return;
            }

            String commentId = commentsRef.push().getKey();
            Comment newComment = new Comment(commentId, currentUser.getUid(), currentUsername, commentText, ServerValue.TIMESTAMP, postId, currentProfileImageUrl);

            commentsRef.child(commentId).setValue(newComment)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(PostThreadsActivity.this, "Comment added", Toast.LENGTH_SHORT).show();
                        binding.commentEditText.setText("");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(PostThreadsActivity.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
                        Log.e("CommentActivity", "Failed to add comment: " + e.getMessage());
                    });
        });
    }

    private void fetchComments() {
        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    if (comment != null) {
                        commentList.add(comment);
                    }
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PostThreadsActivity.this, "Failed to fetch comments: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}