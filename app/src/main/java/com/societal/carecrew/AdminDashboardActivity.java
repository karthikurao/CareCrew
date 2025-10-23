package com.societal.carecrew;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.societal.carecrew.databinding.ActivityAdminDashboardBinding;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private ActivityAdminDashboardBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef, eventsRef, groupsRef, postsRef;
    private EventAdapter eventAdapter;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Check if user is admin
        checkAdminAccess(currentUser.getUid());

        // Initialize Firebase references
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        eventsRef = FirebaseDatabase.getInstance().getReference("events");
        groupsRef = FirebaseDatabase.getInstance().getReference("groups");
        postsRef = FirebaseDatabase.getInstance().getReference("posts");

        // Setup RecyclerView for events
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList, this);
        binding.eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.eventsRecyclerView.setAdapter(eventAdapter);

        // Load analytics
        loadAnalytics();

        // Load events
        loadEvents();

        // Setup button listeners
        binding.backButton.setOnClickListener(v -> finish());

        binding.refreshButton.setOnClickListener(v -> {
            loadAnalytics();
            loadEvents();
        });
    }

    private void checkAdminAccess(String userId) {
        usersRef.child(userId).child("isAdmin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean isAdmin = snapshot.getValue(Boolean.class);
                if (isAdmin == null || !isAdmin) {
                    Toast.makeText(AdminDashboardActivity.this, "Access Denied: Admin privileges required", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminDashboardActivity.this, "Error checking admin access", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void loadAnalytics() {
        // Load total users count
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long userCount = snapshot.getChildrenCount();
                binding.totalUsersCount.setText(String.valueOf(userCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AdminDashboard", "Failed to load users count: " + error.getMessage());
            }
        });

        // Load total events count
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long eventCount = snapshot.getChildrenCount();
                binding.totalEventsCount.setText(String.valueOf(eventCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AdminDashboard", "Failed to load events count: " + error.getMessage());
            }
        });

        // Load total groups count
        groupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long groupCount = snapshot.getChildrenCount();
                binding.totalGroupsCount.setText(String.valueOf(groupCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AdminDashboard", "Failed to load groups count: " + error.getMessage());
            }
        });

        // Load total posts count
        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long postCount = snapshot.getChildrenCount();
                binding.totalPostsCount.setText(String.valueOf(postCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AdminDashboard", "Failed to load posts count: " + error.getMessage());
            }
        });
    }

    private void loadEvents() {
        binding.progressBar.setVisibility(View.VISIBLE);
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear();
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null) {
                        eventList.add(event);
                    }
                }
                eventAdapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.GONE);

                if (eventList.isEmpty()) {
                    binding.noEventsText.setVisibility(View.VISIBLE);
                } else {
                    binding.noEventsText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminDashboardActivity.this, "Failed to load events: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AdminDashboard", "Failed to load events: " + error.getMessage());
            }
        });
    }

    public void deleteEvent(Event event) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    eventsRef.child(event.getEventId()).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(AdminDashboardActivity.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                                loadAnalytics(); // Refresh analytics
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(AdminDashboardActivity.this, "Failed to delete event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
