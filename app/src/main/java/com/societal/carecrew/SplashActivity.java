// SplashActivity.java
package com.societal.carecrew;

import android.content.Intent;
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
            // Check both SharedPreferences and Firebase Auth for session validation
            boolean isLoggedIn = getSharedPreferences("app_prefs", MODE_PRIVATE)
                    .getBoolean("is_logged_in", false);
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            Intent intent;
            // User is logged in if both SharedPreferences says so AND Firebase has a valid session
            if (isLoggedIn && currentUser != null) {
                intent = new Intent(SplashActivity.this, HomePageActivity.class);
            } else {
                // If either check fails, clear the SharedPreferences and go to signup
                if (isLoggedIn && currentUser == null) {
                    // Clear stale SharedPreferences
                    getSharedPreferences("app_prefs", MODE_PRIVATE).edit()
                            .putBoolean("is_logged_in", false)
                            .apply();
                }
                intent = new Intent(SplashActivity.this, SignupActivity.class);
            }
            startActivity(intent);
            finish(); // Finish SplashActivity
        }, 3000); // 3-second delay
    }
}