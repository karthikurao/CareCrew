// SplashActivity.java
package com.societal.carecrew;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SplashActivity extends AppCompatActivity {

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                // Permission granted or denied — proceed regardless
                proceedAfterSplash();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize notification channels
        NotificationHelper.createNotificationChannels(this);

        new Handler().postDelayed(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                    return;
                }
            }
            proceedAfterSplash();
        }, 2000); // 2-second splash delay
    }

    private void proceedAfterSplash() {
        boolean isLoggedIn = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getBoolean("is_logged_in", false);

        Intent intent;
        if (isLoggedIn) {
            intent = new Intent(SplashActivity.this, HomePageActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, SignupActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
