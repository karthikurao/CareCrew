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
            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
            boolean hasSeenWelcome = prefs.getBoolean("has_seen_welcome", false);
            
            // Check both SharedPreferences and Firebase Auth for session validation
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            Intent intent;
            // User is logged in if both SharedPreferences says so AND Firebase has a valid session
            if (isLoggedIn && currentUser != null) {
                // User is logged in, go to home
                intent = new Intent(SplashActivity.this, HomePageActivity.class);
            } else if (!hasSeenWelcome) {
                // First time user, show welcome screen
                intent = new Intent(SplashActivity.this, WelcomeActivity.class);
            } else {
                // If either check fails, clear the SharedPreferences and go to signup
                if (isLoggedIn && currentUser == null) {
                    // Clear stale SharedPreferences
                    prefs.edit()
                            .putBoolean("is_logged_in", false)
                            .apply();
                }
                // User has seen welcome but not logged in, go to signup
                intent = new Intent(SplashActivity.this, SignupActivity.class);
            }
            startActivity(intent);
            finish(); // Finish SplashActivity
        }, 3000); // 3-second delay
    }
}