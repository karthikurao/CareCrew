// OpportunityDetailActivity.java
package com.societal.carecrew;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OpportunityDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunity_details);

        TextView titleTextView = findViewById(R.id.opportunityTitleTextView);
        TextView categoryTextView = findViewById(R.id.opportunityCategoryTextView);
        // ... add other views to display the details

        // Get the Opportunity object from the intent
        Opportunity opportunity = getIntent().getParcelableExtra("opportunity");

        if (opportunity != null) {
            titleTextView.setText(opportunity.getTitle());
            categoryTextView.setText(opportunity.getCategory());
            // ... set the values in other views based on the opportunity data
        } else {
            // Handle the case where the opportunity data is not found
        }
    }
}