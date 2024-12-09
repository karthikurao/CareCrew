// MapsActivity.java
package com.societal.carecrew;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;
import com.societal.carecrew.databinding.ActivityMapsBinding;

public class MapsActivity extends AppCompatActivity {

    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Add tabs to TabLayout
        binding.mapViewSwitch.addTab(binding.mapViewSwitch.newTab().setText("Map"));
        binding.mapViewSwitch.addTab(binding.mapViewSwitch.newTab().setText("List"));

        binding.mapViewSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Intent intent;
                if (tab.getPosition() == 0) {
                    intent = new Intent(MapsActivity.this, MapViewActivity.class);
                } else {
                    intent = new Intent(MapsActivity.this, OpportunityDetailsActivity.class);
                }
                startActivity(intent);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        binding.addOpportunityButton.setOnClickListener(v -> {
            // Handle adding a new opportunity (e.g., start AddOpportunityActivity)
            Intent intent = new Intent(MapsActivity.this, AddOpportunityActivity.class);
            startActivity(intent);
        });
    }
}