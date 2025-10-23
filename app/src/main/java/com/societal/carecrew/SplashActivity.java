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
            // Check Firebase Auth state for persistent login
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            Intent intent;
            if (currentUser != null) {
                // User is already authenticated, go to home
                intent = new Intent(SplashActivity.this, HomePageActivity.class);
            } else {
                // No authenticated user, go to signup
                intent = new Intent(SplashActivity.this, SignupActivity.class);
            }
            startActivity(intent);
            finish(); // Finish SplashActivity
        }, 3000); // 3-second delay
    }
}