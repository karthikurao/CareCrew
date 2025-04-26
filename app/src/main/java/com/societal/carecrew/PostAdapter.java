// PostAdapter.java
package com.societal.carecrew;

import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private AppCompatActivity activity;
    private HomePageActivity homePageActivity;

    public PostAdapter(List<Post> postList, AppCompatActivity activity) {
        this.postList = postList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        try {
            Post post = postList.get(position);
            if (post == null || post.getUid() == null || post.getPostId() == null) {
                Log.e("PostAdapter", "Invalid post data at position: " + position);
                new Thread(() -> {
                    postList.remove(position);
                    activity.runOnUiThread(() -> notifyItemRemoved(position));
                }).start();
                return;
            }

            String postId = post.getPostId();

            DatabaseReference postLikesRef = FirebaseDatabase.getInstance().getReference("posts")
                    .child(postId)
                    .child("likes");
            DatabaseReference postCommentsRef = FirebaseDatabase.getInstance().getReference("posts")
                    .child(postId)
                    .child("comments");

            postLikesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long numLikes = snapshot.getChildrenCount();
                    holder.numLikesTextView.setText(String.valueOf(numLikes) + " likes");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("PostAdapter", "Failed to get likes count: " + error.getMessage());
                }
            });

            postCommentsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long numComments = snapshot.getChildrenCount();
                    holder.numCommentsTextView.setText(String.valueOf(numComments) + " comments");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("PostAdapter", "Failed to get comments count: " + error.getMessage());
                }
            });

            holder.likeButton.setOnClickListener(v -> {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    DatabaseReference userLikesRef = postLikesRef.child(currentUser.getUid());
                    userLikesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // User has already liked the post, so unlike it
                                userLikesRef.removeValue();
                                holder.likeButton.setImageResource(R.drawable.ic_like_unfilled);
                                int currentLikes = Integer.parseInt(holder.numLikesTextView.getText().toString().split(" ")[0]);
                                holder.numLikesTextView.setText(String.valueOf(currentLikes - 1) + " likes");
                            } else {
                                // User hasn't liked the post, so like it
                                userLikesRef.setValue(true);
                                holder.likeButton.setImageResource(R.drawable.ic_like_filled);
                                int currentLikes = Integer.parseInt(holder.numLikesTextView.getText().toString().split(" ")[0]);
                                holder.numLikesTextView.setText(String.valueOf(currentLikes + 1) + " likes");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("PostAdapter", "Failed to like/unlike post: " + error.getMessage());
                        }
                    });
                }
            });

            holder.commentButton.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), CommentActivity.class);
                intent.putExtra("postId", postId);
                holder.itemView.getContext().startActivity(intent);
            });

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(post.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        HelperClass user = snapshot.getValue(HelperClass.class);
                        if (user != null) {
                            holder.usernameTextView.setText(user.getUsername());

                            Glide.with(holder.itemView.getContext())
                                    .load(user.getProfileImageUrl())
                                    .placeholder(R.drawable.default_profile_image)
                                    .into(holder.profileImageView);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("PostAdapter", "Failed to fetch user data: " + error.getMessage());
                }
            });

            holder.captionTextView.setText(post.getCaption());
            Glide.with(holder.itemView.getContext()).load(post.getImageUrl()).into(holder.postImageView);

            // Double-tap to like (only on the postImageView)
            GestureDetector gestureDetector = new GestureDetector(holder.itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    // Trigger like functionality here (same logic as likeButton.setOnClickListener)
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        DatabaseReference userLikesRef = postLikesRef.child(currentUser.getUid());
                        userLikesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // User has already liked the post, so unlike it
                                    userLikesRef.removeValue();
                                    holder.likeButton.setImageResource(R.drawable.ic_like_unfilled);
                                    int currentLikes = Integer.parseInt(holder.numLikesTextView.getText().toString().split(" ")[0]);
                                    holder.numLikesTextView.setText(String.valueOf(currentLikes - 1) + " likes");
                                } else {
                                    // User hasn't liked the post, so like it
                                    userLikesRef.setValue(true);
                                    holder.likeButton.setImageResource(R.drawable.ic_like_filled);
                                    int currentLikes = Integer.parseInt(holder.numLikesTextView.getText().toString().split(" ")[0]);
                                    holder.numLikesTextView.setText(String.valueOf(currentLikes + 1) + " likes");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("PostAdapter", "Failed to like/unlike post: " + error.getMessage());
                            }
                        });
                    }

                    // Show like animation
                    holder.likeAnimationView.setVisibility(View.VISIBLE);
                    holder.likeAnimationView.playAnimation();

                    return true; // Consume the double-tap event
                }
            });

            holder.postImageView.setOnTouchListener((v, event) -> {
                gestureDetector.onTouchEvent(event);
                return true; // Consume the touch event on the ImageView
            });

            // Single-tap to open PostThreadsActivity (on the entire card EXCEPT the postImageView)
            holder.postCardView.setOnClickListener(v -> {
                // Get the coordinates of the touch event
                float touchX = v.getX();
                float touchY = v.getY();

                // Get the coordinates of the postImageView
                int[] imageLocation = new int[2];
                holder.postImageView.getLocationOnScreen(imageLocation);
                int imageX = imageLocation[0];
                int imageY = imageLocation[1];
                int imageWidth = holder.postImageView.getWidth();
                int imageHeight = holder.postImageView.getHeight();

                // Check if the touch event was outside the postImageView
                if (touchX < imageX || touchX > imageX + imageWidth || touchY < imageY || touchY > imageY + imageHeight) {
                    Intent intent = new Intent(holder.itemView.getContext(), PostThreadsActivity.class);
                    intent.putExtra("postId", postId);
                    holder.itemView.getContext().startActivity(intent);
                }
            });

        } catch (Exception e) {
            Log.e("PostAdapter", "Error in onBindViewHolder at position: " + position, e);
            new Thread(() -> {
                postList.remove(position);
                // Get a reference to the HomePageActivity instance
                AppCompatActivity activity = homePageActivity;
                // Check if the activity is still valid before calling runOnUiThread
                if (activity != null && !activity.isFinishing()) {
                    activity.runOnUiThread(() -> notifyItemRemoved(position));
                }
            }).start();
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView usernameTextView;
        ImageView postImageView;
        TextView captionTextView;
        ImageView likeButton;
        ImageView commentButton;
        TextView numLikesTextView;
        TextView numCommentsTextView;
        LottieAnimationView likeAnimationView;
        MaterialCardView postCardView;

        public PostViewHolder(View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            postImageView = itemView.findViewById(R.id.postImageView);
            captionTextView = itemView.findViewById(R.id.captionTextView);
            likeButton = itemView.findViewById(R.id.likeButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            numLikesTextView = itemView.findViewById(R.id.numLikesTextView);
            numCommentsTextView = itemView.findViewById(R.id.numCommentsTextView);
            likeAnimationView = itemView.findViewById(R.id.likeAnimationView);
            postCardView = itemView.findViewById(R.id.postCardView);
        }
    }
}