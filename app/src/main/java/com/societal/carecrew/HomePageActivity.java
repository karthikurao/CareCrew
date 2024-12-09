// HomePageActivity.java
package com.societal.carecrew;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.societal.carecrew.databinding.ActivityHomePageBinding;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    private ActivityHomePageBinding binding;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private DatabaseReference postsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize RecyclerView
        binding.postRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList, this); // Pass the activity instance to the adapter
        binding.postRecyclerView.setAdapter(postAdapter);

        // Fetch posts from Firebase
        postsRef = FirebaseDatabase.getInstance().getReference("posts");
        fetchPosts();

        // Handle create post button click
        binding.createPostButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, CreatePostActivity.class);
            startActivity(intent);
        });
    }

    // Method to show opportunity details (called from the adapter)
    public void showOpportunityDetails(Opportunity opportunity) {
        Intent intent = new Intent(HomePageActivity.this, OpportunityDetailsActivity.class);
        intent.putExtra("opportunity", opportunity);
        startActivity(intent);
    }

    private void fetchPosts() {
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post != null) {
                        postList.add(post);
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomePageActivity.this, "Failed to fetch posts: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HomePageActivity", "Failed to fetch posts: " + databaseError.getMessage());
            }
        });
    }
}