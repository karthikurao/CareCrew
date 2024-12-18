// HomePageActivity.java
package com.societal.carecrew;

import android.content.Intent;
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
import com.google.firebase.database.ValueEventListener;
import com.societal.carecrew.databinding.ActivityHomePageBinding;
import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    private ActivityHomePageBinding binding;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private DatabaseReference postsRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.postRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList, this);
        binding.postRecyclerView.setAdapter(postAdapter);

        binding.createPostButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, CreatePostActivity.class);
            startActivity(intent);
        });

        binding.navView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                // Already on the home page, no action needed
            } else if (item.getItemId() == R.id.navigation_maps) {
                startActivity(new Intent(HomePageActivity.this, MapsActivity.class));
            } else if (item.getItemId() == R.id.navigation_groups) {
                startActivity(new Intent(HomePageActivity.this, GroupsActivity.class));
            } else if (item.getItemId() == R.id.navigation_profile) {
                startActivity(new Intent(HomePageActivity.this, ProfileActivity.class));
            }
            return true;
        });

        binding.navView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(HomePageActivity.this, LoginActivity.class));
            finish();
        } else {
            fetchPosts();
        }
    }

    public void showOpportunityDetails(Opportunity opportunity) {
        Intent intent = new Intent(HomePageActivity.this, OpportunityDetailsActivity.class);
        intent.putExtra("opportunity", opportunity);
        startActivity(intent);
    }

    private void fetchPosts() {
        postsRef = FirebaseDatabase.getInstance().getReference("posts");
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post != null &&
                            post.getUid() != null && !post.getUid().isEmpty() &&
                            post.getPostId() != null && !post.getPostId().isEmpty() &&
                            post.getCaption() != null && !post.getCaption().isEmpty() &&
                            post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
                        postList.add(post);
                    } else {
                        Log.e("HomePageActivity", "Invalid post data for snapshot: " + snapshot.getKey());
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