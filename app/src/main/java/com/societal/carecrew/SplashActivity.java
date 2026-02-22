// SplashActivity.java
package com.societal.carecrew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            // Check Firebase Auth state for persistent login
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            
            // Also check welcome screen status
            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            boolean hasSeenWelcome = prefs.getBoolean("has_seen_welcome", false);

            Intent intent;
            if (currentUser != null) {
                // User is already authenticated, go to home
                intent = new Intent(SplashActivity.this, HomePageActivity.class);
            } else if (!hasSeenWelcome) {
                // First time user, show welcome screen
                intent = new Intent(SplashActivity.this, WelcomeActivity.class);
            } else {
                // User has seen welcome but not logged in, go to signup
                intent = new Intent(SplashActivity.this, SignupActivity.class);
            }
            startActivity(intent);
            finish(); // Finish SplashActivity
        }, 3000); // 3-second delay
    }
}