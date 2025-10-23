package com.societal.carecrew;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddOpportunityActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private TextInputEditText titleInput, descriptionInput, dateInput, locationInput;
    private Button useCurrentLocationButton, submitButton;
    private TextView coordinatesText;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private DatabaseReference opportunitiesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_opportunity);

        // Initialize Firebase reference
        opportunitiesRef = FirebaseDatabase.getInstance().getReference("opportunities");

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize views
        titleInput = findViewById(R.id.opportunity_title);
        descriptionInput = findViewById(R.id.opportunity_description);
        dateInput = findViewById(R.id.opportunity_date);
        locationInput = findViewById(R.id.opportunity_location);
        useCurrentLocationButton = findViewById(R.id.use_current_location_button);
        submitButton = findViewById(R.id.submit_button);
        coordinatesText = findViewById(R.id.coordinates_text);

        // Set up button listeners
        useCurrentLocationButton.setOnClickListener(v -> getCurrentLocation());
        submitButton.setOnClickListener(v -> createOpportunity());
    }

    private void getCurrentLocation() {
        if (checkLocationPermission()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            coordinatesText.setText(String.format(Locale.getDefault(),
                                    "Coordinates: %.6f, %.6f", latitude, longitude));

                            // Try to get address from coordinates
                            getAddressFromLocation(latitude, longitude);
                        } else {
                            Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to get location: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
        } else {
            requestLocationPermission();
        }
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressText = address.getAddressLine(0);
                locationInput.setText(addressText);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to get address", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void createOpportunity() {
        String title = titleInput.getText() != null ? titleInput.getText().toString().trim() : "";
        String description = descriptionInput.getText() != null ? descriptionInput.getText().toString().trim() : "";
        String date = dateInput.getText() != null ? dateInput.getText().toString().trim() : "";
        String location = locationInput.getText() != null ? locationInput.getText().toString().trim() : "";

        // Validate inputs
        if (title.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (latitude == 0.0 || longitude == 0.0) {
            Toast.makeText(this, "Please set location coordinates", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create opportunity object
        Opportunity opportunity = new Opportunity(title, description, date, location, latitude, longitude);

        // Generate a unique key for the opportunity
        String opportunityId = opportunitiesRef.push().getKey();

        if (opportunityId != null) {
            // Save to Firebase
            opportunitiesRef.child(opportunityId).setValue(opportunity)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddOpportunityActivity.this,
                                "Opportunity created successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddOpportunityActivity.this,
                                "Failed to create opportunity: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                          @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}