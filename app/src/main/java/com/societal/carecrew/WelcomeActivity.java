// WelcomeActivity.java
package com.societal.carecrew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button btnGetStarted = findViewById(R.id.btnGetStarted);
        
        btnGetStarted.setOnClickListener(v -> {
            // Mark that user has seen the welcome screen
            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            prefs.edit().putBoolean("has_seen_welcome", true).apply();
            
            // Navigate to signup/login
            Intent intent = new Intent(WelcomeActivity.this, SignupActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
