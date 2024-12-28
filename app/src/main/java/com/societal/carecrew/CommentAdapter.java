// CommentAdapter.java
package com.societal.carecrew;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> commentList;
    private String currentUsername;
    private String postId;

    public CommentAdapter(List<Comment> commentList, String currentUsername, String postId) {
        this.commentList = commentList;
        this.currentUsername = currentUsername;
        this.postId = postId;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        holder.usernameTextView.setText(comment.getUsername());
        holder.commentTextView.setText(comment.getCommentText());

        // Format the timestamp to show only hours
        long timestampMillis = (long) comment.getTimestamp();
        CharSequence relativeTimestamp = DateUtils.getRelativeTimeSpanString(timestampMillis);
        holder.timestampTextView.setText(relativeTimestamp);

        Glide.with(holder.itemView.getContext())
                .load(comment.getProfileImageUrl())
                .placeholder(R.drawable.default_profile_image)
                .into(holder.profileImageView);

        // Reply functionality
        holder.replyButton.setOnClickListener(v -> {
            if (holder.replyLayout.getVisibility() == View.GONE) {
                holder.replyLayout.setVisibility(View.VISIBLE);
                holder.postReplyButton.setVisibility(View.VISIBLE);
            } else {
                holder.replyLayout.setVisibility(View.GONE);
                holder.postReplyButton.setVisibility(View.GONE);
            }
        });

        holder.postReplyButton.setOnClickListener(v -> {
            String replyText = holder.replyEditText.getText().toString().trim();
            if (replyText.isEmpty()) {
                Toast.makeText(holder.itemView.getContext(), "Please enter a reply", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get a reference to the comments node for this post
            DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference("posts").child(postId).child("comments");

            // Create a new comment object for the reply
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            // Fetch the profile image URL of the current user
            if (currentUser != null) {
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            HelperClass user = snapshot.getValue(HelperClass.class);
                            if (user != null) {
                                String profileImageUrl = user.getProfileImageUrl();

                                String commentId = commentsRef.push().getKey(); // Generate a unique comment ID
                                Comment newComment = new Comment(commentId, currentUser.getUid(), currentUsername, replyText, ServerValue.TIMESTAMP, postId, profileImageUrl);

                                // Push the new comment (reply) to the database
                                commentsRef.child(commentId).setValue(newComment)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(holder.itemView.getContext(), "Reply added", Toast.LENGTH_SHORT).show();
                                            holder.replyEditText.setText(""); // Clear the reply input field
                                            holder.replyLayout.setVisibility(View.GONE); // Hide the reply layout
                                            holder.postReplyButton.setVisibility(View.GONE); // Hide the post button
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(holder.itemView.getContext(), "Failed to add reply", Toast.LENGTH_SHORT).show();
                                            Log.e("CommentAdapter", "Failed to add reply: " + e.getMessage());
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the error appropriately
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView usernameTextView;
        TextView commentTextView;
        TextView timestampTextView;
        MaterialButton replyButton;
        LinearLayout replyLayout;
        TextView replyEditText;
        MaterialButton postReplyButton;

        public CommentViewHolder(View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            replyButton = itemView.findViewById(R.id.replyButton);
            replyLayout = itemView.findViewById(R.id.replyLayout);
            replyEditText = itemView.findViewById(R.id.replyEditText);
            postReplyButton = itemView.findViewById(R.id.postReplyButton);
        }
    }
}