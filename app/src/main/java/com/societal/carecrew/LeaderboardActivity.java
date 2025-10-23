// LeaderboardActivity.java
package com.societal.carecrew;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private static final String TAG = "LeaderboardActivity";
    private RecyclerView leaderboardRecyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private List<LeaderboardItem> leaderboardItems;
    private View emptyStateLayout;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize views
        leaderboardRecyclerView = findViewById(R.id.leaderboardRecyclerView);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        BottomNavigationView navView = findViewById(R.id.navView);

        // Setup RecyclerView
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        leaderboardItems = new ArrayList<>();
        leaderboardAdapter = new LeaderboardAdapter(leaderboardItems, this);
        leaderboardRecyclerView.setAdapter(leaderboardAdapter);

        // Setup bottom navigation
        navView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                startActivity(new Intent(LeaderboardActivity.this, HomePageActivity.class));
            } else if (item.getItemId() == R.id.navigation_maps) {
                startActivity(new Intent(LeaderboardActivity.this, MapsActivity.class));
            } else if (item.getItemId() == R.id.navigation_groups) {
                startActivity(new Intent(LeaderboardActivity.this, GroupsActivity.class));
            } else if (item.getItemId() == R.id.navigation_leaderboard) {
                // Already on leaderboard page
            } else if (item.getItemId() == R.id.navigation_profile) {
                startActivity(new Intent(LeaderboardActivity.this, ProfileActivity.class));
            }
            return true;
        });

        navView.setSelectedItemId(R.id.navigation_leaderboard);

        // Load leaderboard data
        loadLeaderboard();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(LeaderboardActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void loadLeaderboard() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                leaderboardItems.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        HelperClass user = snapshot.getValue(HelperClass.class);
                        if (user != null && user.getName() != null) {
                            LeaderboardItem item = new LeaderboardItem(
                                    snapshot.getKey(),
                                    user.getName(),
                                    user.getProfileImageUrl(),
                                    user.getHoursVolunteered(),
                                    user.getOpportunitiesParticipated(),
                                    user.getGroupsJoined()
                            );
                            leaderboardItems.add(item);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing user data: " + e.getMessage());
                    }
                }

                // Sort by total score (descending)
                Collections.sort(leaderboardItems, new Comparator<LeaderboardItem>() {
                    @Override
                    public int compare(LeaderboardItem o1, LeaderboardItem o2) {
                        return Integer.compare(o2.getTotalScore(), o1.getTotalScore());
                    }
                });

                // Assign ranks
                for (int i = 0; i < leaderboardItems.size(); i++) {
                    leaderboardItems.get(i).setRank(i + 1);
                }

                // Show/hide empty state
                if (leaderboardItems.isEmpty()) {
                    emptyStateLayout.setVisibility(View.VISIBLE);
                    leaderboardRecyclerView.setVisibility(View.GONE);
                } else {
                    emptyStateLayout.setVisibility(View.GONE);
                    leaderboardRecyclerView.setVisibility(View.VISIBLE);
                }

                leaderboardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LeaderboardActivity.this,
                        "Failed to load leaderboard: " + databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Database error: " + databaseError.getMessage());
            }
        });
    }
}
