package com.societal.carecrew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.societal.carecrew.databinding.ActivityEditProfileBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String IMG_BB_API_KEY = "fe3ec739386bdf08dec5cae269f7cb10"; // Replace with your actual API key
    private Uri imageUri;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        usersRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());

        loadCurrentProfileData();

        binding.updateButton.setOnClickListener(v -> {
            String newName = binding.nameEditText.getText().toString().trim();
            String newEmail = binding.emailEditText.getText().toString().trim();
            String newBio = binding.bioEditText.getText().toString().trim();
            String currentPassword = binding.currentPasswordEditText.getText().toString().trim();
            String newPassword = binding.newPasswordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(newName) || TextUtils.isEmpty(newEmail) || TextUtils.isEmpty(newBio)) {
                Toast.makeText(EditProfileActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            updateProfileData(newName, newEmail, newBio, currentPassword, newPassword);
        });

        binding.profileImageCard.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    private void loadCurrentProfileData() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HelperClass helperClass = snapshot.getValue(HelperClass.class);
                    if (helperClass != null) {
                        binding.nameEditText.setText(helperClass.getName());
                        binding.emailEditText.setText(helperClass.getEmail());
                        binding.bioEditText.setText(helperClass.getBio()); // Assuming you've added getBio() to HelperClass

                        Glide.with(EditProfileActivity.this)
                                .load(helperClass.getProfileImageUrl())
                                .placeholder(R.drawable.default_profile_image)
                                .into(binding.profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Failed to fetch profile data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfileData(String newName, String newEmail, String newBio, String currentPassword, String newPassword) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating profile...");
        progressDialog.show();

        // Update email and password if necessary
        if (!newPassword.isEmpty() && !currentPassword.isEmpty()) {
            AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(currentUser.getEmail()), currentPassword);

            currentUser.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            currentUser.updateEmail(newEmail)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            currentUser.updatePassword(newPassword)
                                                    .addOnCompleteListener(task2 -> {
                                                        if (task2.isSuccessful()) {
                                                            updateDatabase(newName, newEmail, newBio);
                                                        } else {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(EditProfileActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                                            Log.e("EditProfileActivity", "Password update failed: " + task2.getException().getMessage());
                                                        }
                                                    });
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(EditProfileActivity.this, "Failed to update email", Toast.LENGTH_SHORT).show();
                                            Log.e("EditProfileActivity", "Reauthentication failed: " + task.getException().getMessage());
                                        }
                                    });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, "Incorrect current password", Toast.LENGTH_SHORT).show();
                            Log.e("EditProfileActivity", "Reauthentication failed: " + task.getException().getMessage()); // Log the error

                        }
                    });
        } else {
            // If password not being updated
            updateDatabase(newName, newEmail, newBio);
        }
        usersRef.child("name").setValue(newName);
        usersRef.child("email").setValue(newEmail);
        usersRef.child("bio").setValue(newBio)
                .addOnSuccessListener(aVoid -> { // Make sure this line is correct
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateDatabase(String newName, String newEmail, String newBio) {
        usersRef.child("name").setValue(newName);
        usersRef.child("email").setValue(newEmail);
        usersRef.child("bio").setValue(newBio) // Assuming you have a "bio" field in your database
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                });
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
                        Toast.makeText(EditProfileActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                        Log.e("EditProfileActivity", "ImgBB upload failed: " + e.getMessage());
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
                                    // Update the profileImageUrl in Firebase Realtime Database
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user != null) {
                                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                                        usersRef.child("profileImageUrl").setValue(imageUrl)
                                                .addOnSuccessListener(aVoid -> {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditProfileActivity.this, "Profile image updated", Toast.LENGTH_SHORT).show();
                                                    loadProfileImage(imageUrl); // Load the new image
                                                })
                                                .addOnFailureListener(e -> {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditProfileActivity.this, "Failed to update profile image", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                });
                            } else {
                                runOnUiThread(() -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(EditProfileActivity.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                                });
                            }
                        } catch (Exception e) {
                            runOnUiThread(() -> {
                                progressDialog.dismiss();
                                Toast.makeText(EditProfileActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                                Log.e("EditProfileActivity", "Error parsing ImgBB response: " + e.getMessage());
                            });
                        }
                    } else {
                        runOnUiThread(() -> {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                            Log.e("EditProfileActivity", "ImgBB upload failed: " + response.code());
                        });
                    }
                }
            });

        } catch (IOException e) {
            runOnUiThread(() -> {
                progressDialog.dismiss();
                Toast.makeText(EditProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                Log.e("EditProfileActivity", "Failed to get bitmap from URI: " + e.getMessage());
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