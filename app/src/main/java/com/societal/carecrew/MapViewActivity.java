package com.societal.carecrew;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference usersRef;
    private DatabaseReference opportunitiesRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        mAuth = FirebaseAuth.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        
        // Initialize Firebase references
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        opportunitiesRef = FirebaseDatabase.getInstance().getReference("opportunities");

        // Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Enable location if permissions granted
        if (checkLocationPermission()) {
            enableMyLocation();
            loadVolunteerLocations();
            loadOpportunityLocations();
        } else {
            requestLocationPermission();
        }

        // Set default location (example: San Francisco)
        LatLng defaultLocation = new LatLng(37.7749, -122.4194);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
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

    private void enableMyLocation() {
        if (checkLocationPermission()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            
            // Get current location and update it in Firebase
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null && mAuth.getCurrentUser() != null) {
                            // Update user's location in Firebase
                            String userId = mAuth.getCurrentUser().getUid();
                            usersRef.child(userId).child("latitude").setValue(location.getLatitude());
                            usersRef.child(userId).child("longitude").setValue(location.getLongitude());
                            
                            // Move camera to current location
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
                        }
                    });
        }
    }

    private void loadVolunteerLocations() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    HelperClass user = userSnapshot.getValue(HelperClass.class);
                    if (user != null && user.getLatitude() != 0.0 && user.getLongitude() != 0.0) {
                        LatLng volunteerLocation = new LatLng(user.getLatitude(), user.getLongitude());
                        
                        // Add marker for volunteer
                        mMap.addMarker(new MarkerOptions()
                                .position(volunteerLocation)
                                .title(user.getName() != null ? user.getName() : "Volunteer")
                                .snippet("Available volunteer")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MapViewActivity.this, "Failed to load volunteers", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadOpportunityLocations() {
        opportunitiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot opportunitySnapshot : snapshot.getChildren()) {
                    Opportunity opportunity = opportunitySnapshot.getValue(Opportunity.class);
                    if (opportunity != null && opportunity.getLatitude() != 0.0 && opportunity.getLongitude() != 0.0) {
                        LatLng opportunityLocation = new LatLng(opportunity.getLatitude(), opportunity.getLongitude());
                        
                        // Add marker for opportunity
                        mMap.addMarker(new MarkerOptions()
                                .position(opportunityLocation)
                                .title(opportunity.getTitle() != null ? opportunity.getTitle() : "Opportunity")
                                .snippet(opportunity.getDescription() != null ? opportunity.getDescription() : "Community need")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MapViewActivity.this, "Failed to load opportunities", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                          @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
                loadVolunteerLocations();
                loadOpportunityLocations();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}