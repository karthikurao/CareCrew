// SplashActivity.java
package com.societal.carecrew;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            boolean isLoggedIn = getSharedPreferences("app_prefs", MODE_PRIVATE)
                    .getBoolean("is_logged_in", false);

            Intent intent;
            if (isLoggedIn) {
                intent = new Intent(SplashActivity.this, HomePageActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, SignupActivity.class);
            }
            startActivity(intent);
            finish(); // Finish SplashActivity
        }, 3000); // 3-second delay
    }
}