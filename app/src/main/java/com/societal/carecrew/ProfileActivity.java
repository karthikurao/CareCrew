// ProfileActivity.java
package com.societal.carecrew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.societal.carecrew.databinding.ActivityProfileBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String IMG_BB_API_KEY = "fe3ec739386bdf08dec5cae269f7cb10"; // Replace with your actual API key
    private Uri imageUri;
    private ProgressDialog progressDialog;
    private boolean isBioEditing = false;
    private List<Post> postList;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initially disable editing and hide the done button
        binding.bioEditText.setEnabled(false);
        binding.bioDoneButton.setVisibility(View.GONE);

        binding.bioEditIcon.setOnClickListener(v -> {
            if (isBioEditing) {
                // Save the bio to Firebase
                String newBio = binding.bioEditText.getText().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                    usersRef.child("bio").setValue(newBio)
                            .addOnSuccessListener(aVoid -> Toast.makeText(ProfileActivity.this, "Bio updated", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Failed to update bio", Toast.LENGTH_SHORT).show());
                }

                // Disable editing and hide the done button
                binding.bioEditText.setEnabled(false);
                binding.bioDoneButton.setVisibility(View.GONE);
                binding.bioEditIcon.setImageResource(R.drawable.ic_edit); // Change icon to edit
            } else {
                // Enable editing and show the done button
                binding.bioEditText.setEnabled(true);
                binding.bioDoneButton.setVisibility(View.VISIBLE);
                binding.bioEditIcon.setImageResource(R.drawable.ic_close); // Change icon to close
            }
            isBioEditing = !isBioEditing; // Toggle the editing state
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        HelperClass helperClass = snapshot.getValue(HelperClass.class);
                        if (helperClass != null) {
                            binding.usernameTextView.setText(helperClass.getUsername());
                            binding.nameTextView.setText(helperClass.getName());
                            binding.emailTextView.setText(helperClass.getEmail());
                            binding.bioEditText.setText(helperClass.getBio());

                            // Load profile image using Glide
                            Glide.with(ProfileActivity.this)
                                    .load(helperClass.getProfileImageUrl())
                                    .placeholder(R.drawable.default_profile_image)
                                    .into(binding.profileImageView);

                            // Load cover image using Glide
                            Glide.with(ProfileActivity.this)
                                    .load(helperClass.getCoverImageUrl())
                                    .placeholder(R.drawable.default_cover_image)
                                    .into(binding.coverImageView);

                            // Set volunteer statistics
                            binding.hoursVolunteeredTextView.setText(helperClass.getHoursVolunteered() + " hours");
                            binding.opportunitiesParticipatedTextView.setText(helperClass.getOpportunitiesParticipated() + " opportunities");
                            binding.groupsJoinedTextView.setText(helperClass.getGroupsJoined() + " groups");

                            // Add skills to ChipGroup
                            if (helperClass.getSkills() != null) {
                                for (String skill : helperClass.getSkills()) {
                                    Chip chip = new Chip(ProfileActivity.this);
                                    chip.setText(skill);
                                    binding.skillsChipGroup.addView(chip);
                                }
                            }

                            // Add interests to ChipGroup
                            if (helperClass.getInterests() != null) {
                                for (String interest : helperClass.getInterests()) {
                                    Chip chip = new Chip(ProfileActivity.this);
                                    chip.setText(interest);
                                    binding.interestsChipGroup.addView(chip);
                                }
                            }

                            // Add availability to UI
                            if (helperClass.getAvailability() != null) {
                                binding.weekdaysTextView.setText("Weekdays: " + (helperClass.getAvailability().isWeekdays() ? "Available" : "Not Available"));
                                binding.weekendsTextView.setText("Weekends: " + (helperClass.getAvailability().isWeekends() ? "Available" : "Not Available"));
                                // ... and so on for other availability fields
                            }

                            // Add causes to UI
                            if (helperClass.getCauses() != null) {
                                for (String cause : helperClass.getCauses()) {
                                    Chip chip = new Chip(ProfileActivity.this);
                                    chip.setText(cause);
                                    binding.causesChipGroup.addView(chip);
                                }
                            }

                            // Add location to UI
                            if (helperClass.getLocation() != null) {
                                binding.locationTextView.setText(helperClass.getLocation());
                            }

                            // Add social links to UI (you might need to design this part based on your requirements)
                            // Example:
                            // Map<String, String> socialLinks = helperClass.getSocialLinks();
                            // if (socialLinks != null) {
                            //     for (Map.Entry<String, String> entry : socialLinks.entrySet()) {
                            //         String platform = entry.getKey();
                            //         String link = entry.getValue();
                            //         // ... create a TextView or ImageView for the social link and add it to the layout ...
                            //     }
                            // }

                            // Add aboutMe to UI
                            if (helperClass.getAboutMe() != null) {
                                binding.aboutMeTextView.setText(helperClass.getAboutMe());
                            }

                            // Add volunteer experience to UI
                            if (helperClass.getVolunteerExperience() != null) {
                                // Initialize RecyclerView for volunteer experience
                                binding.volunteerExperienceRecyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
                                VolunteerExperienceAdapter experienceAdapter = new VolunteerExperienceAdapter(helperClass.getVolunteerExperience());
                                binding.volunteerExperienceRecyclerView.setAdapter(experienceAdapter);
                            } else {
                                // Handle the case where volunteerExperience is null
                                Log.e("ProfileActivity", "volunteerExperience is null");
                                // You might want to show a message or hide the RecyclerView
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProfileActivity.this, "Failed to fetch profile data", Toast.LENGTH_SHORT).show();
                }
            });

            // Fetch and display user's posts
            DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");
            postsRef.orderByChild("uid").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
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
                    Toast.makeText(ProfileActivity.this, "Failed to fetch posts: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Initialize RecyclerView for posts
        binding.postsRecyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList, this);
        binding.postsRecyclerView.setAdapter(postAdapter);

        binding.profileImageCard.setOnClickListener(v -> {
            // Get the URL of the current profile image
            // (You'll need to implement the logic to get the profile image URL)
            String profileImageUrl = ""; // Replace with the actual logic

            // Start FullScreenImageActivity and pass the image URL
            Intent intent = new Intent(ProfileActivity.this, FullScreenImageActivity.class);
            intent.putExtra("imageUrl", profileImageUrl);
            startActivity(intent);
        });

        binding.editProfileButton.setOnClickListener(v -> {
            // Start EditProfileActivity
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        binding.logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            getSharedPreferences("app_prefs", MODE_PRIVATE).edit()
                    .putBoolean("is_logged_in", false)
                    .apply();

            // Start LoginActivity
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Finish ProfileActivity
        });

        // Set up BottomNavigationView
        binding.navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(ProfileActivity.this, HomePageActivity.class));
            } else if (itemId == R.id.navigation_maps) {
                startActivity(new Intent(ProfileActivity.this, MapsActivity.class));
            } else if (itemId == R.id.navigation_groups) {
                startActivity(new Intent(ProfileActivity.this, GroupsActivity.class));
            } else if (itemId == R.id.navigation_profile) {
                // Already on ProfileActivity, no action needed
            }
            return true;
        });

        // Set the selected item in the BottomNavigationView
        binding.navView.setSelectedItemId(R.id.navigation_profile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadImageToImgBB(imageUri);
        }
    }

    private void uploadImageToImgBB(Uri imageUri) {
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading image...");
            progressDialog.show();

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            OkHttpClient client = new OkHttpClient().newBuilder().build();
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("key", IMG_BB_API_KEY)
                    .addFormDataPart("image", "image.jpg",
                            RequestBody.create(byteArray, MediaType.parse("image/jpeg")))
                    .build();

            Request request = new Request.Builder()
                    .url("https://api.imgbb.com/1/upload")
                    .method("POST", requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                        Log.e("ProfileActivity", "ImgBB upload failed: " + e.getMessage());
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String responseBody = response.body().string();
                            String imageUrl = parseImageUrlFromJsonResponse(responseBody);
                            if (imageUrl != null) {
                                runOnUiThread(() -> {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user != null) {
                                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                                        usersRef.child("profileImageUrl").setValue(imageUrl)
                                                .addOnSuccessListener(aVoid -> {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(ProfileActivity.this, "Profile image updated", Toast.LENGTH_SHORT).show();
                                                    loadProfileImage(imageUrl);
                                                })
                                                .addOnFailureListener(e -> {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(ProfileActivity.this, "Failed to update profile image", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                });
                            } else {
                                runOnUiThread(() -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(ProfileActivity.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                                });
                            }
                        } catch (Exception e) {
                            runOnUiThread(() -> {
                                progressDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                                Log.e("ProfileActivity", "Error parsing ImgBB response: " + e.getMessage());
                            });
                        }
                    } else {
                        runOnUiThread(() -> {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                            Log.e("ProfileActivity", "ImgBB upload failed: " + response.code());
                        });
                    }
                }
            });

        } catch (IOException e) {
            runOnUiThread(() -> {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                Log.e("ProfileActivity", "Failed to get bitmap from URI: " + e.getMessage());
            });
        }
    }

    private String parseImageUrlFromJsonResponse(String responseBody) {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(responseBody);
            org.json.JSONObject dataObject = jsonObject.getJSONObject("data");
            return dataObject.getString("url");
        } catch (org.json.JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loadProfileImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.default_profile_image)
                .into(binding.profileImageView);
    }
}