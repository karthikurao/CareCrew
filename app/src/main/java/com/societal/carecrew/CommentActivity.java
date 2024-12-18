// CommentActivity.java
package com.societal.carecrew;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.societal.carecrew.databinding.ActivityCommentBinding;

public class CommentActivity extends AppCompatActivity {

    private ActivityCommentBinding binding;
    private DatabaseReference commentsRef;
    private FirebaseUser currentUser;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String postId = getIntent().getStringExtra("postId");
        if (postId == null) {
            Log.e("CommentActivity", "postId is null");
            finish(); // Finish the activity if postId is not found
            return;
        }

        commentsRef = FirebaseDatabase.getInstance().getReference("posts").child(postId).child("comments");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Log.e("CommentActivity", "currentUser is null");
            finish(); // Finish the activity if the user is not logged in
            return;
        }

        // Fetch the current username from the database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HelperClass user = snapshot.getValue(HelperClass.class);
                    if (user != null) {
                        currentUsername = user.getUsername();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CommentActivity", "Failed to fetch username: " + error.getMessage());
                // Handle the error appropriately (e.g., use a default username or show a message)
            }
        });

        binding.submitButton.setOnClickListener(v -> {
            String commentText = binding.commentEditText.getText().toString().trim();
            if (commentText.isEmpty()) {
                Toast.makeText(CommentActivity.this, "Please enter a comment", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new comment object (updated)
            String commentId = commentsRef.push().getKey(); // Generate a unique comment ID
            Comment newComment = new Comment(commentId, currentUser.getUid(), currentUsername, commentText, ServerValue.TIMESTAMP);

            // Push the new comment to the database (updated)
            commentsRef.child(commentId).setValue(newComment) // Use the commentId to create the child node
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(CommentActivity.this, "Comment added", Toast.LENGTH_SHORT).show();
                        binding.commentEditText.setText(""); // Clear the comment input field
                        finish(); // Close the activity after adding the comment
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(CommentActivity.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
                        Log.e("CommentActivity", "Failed to add comment: " + e.getMessage());
                    });
        });
    }
}