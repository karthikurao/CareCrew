// TwoFactorAuthActivity.java
package com.societal.carecrew;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.societal.carecrew.databinding.ActivityTwoFactorAuthBinding;

import java.util.concurrent.TimeUnit;

public class TwoFactorAuthActivity extends AppCompatActivity {

    private ActivityTwoFactorAuthBinding binding;
    private FirebaseAuth mAuth;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private boolean isFromSettings = false;
    private static final String TAG = "TwoFactorAuthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTwoFactorAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        isFromSettings = getIntent().getBooleanExtra("fromSettings", false);

        // Initialize phone auth callbacks
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                binding.progressBar.setVisibility(View.GONE);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(TwoFactorAuthActivity.this, 
                    "Verification failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                 @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                TwoFactorAuthActivity.this.verificationId = verificationId;
                TwoFactorAuthActivity.this.resendToken = token;
                
                binding.progressBar.setVisibility(View.GONE);
                binding.phoneNumberSection.setVisibility(View.GONE);
                binding.otpSection.setVisibility(View.VISIBLE);
                Toast.makeText(TwoFactorAuthActivity.this, 
                    "Verification code sent", Toast.LENGTH_SHORT).show();
            }
        };

        binding.sendCodeButton.setOnClickListener(v -> sendVerificationCode());
        binding.verifyCodeButton.setOnClickListener(v -> verifyCode());
        binding.resendCodeButton.setOnClickListener(v -> resendVerificationCode());
        binding.skipButton.setOnClickListener(v -> skipTwoFactorAuth());
    }

    private void sendVerificationCode() {
        String phoneNumber = binding.phoneNumberEditText.getText().toString().trim();
        
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ensure phone number has country code
        if (!phoneNumber.startsWith("+")) {
            phoneNumber = "+1" + phoneNumber; // Default to US country code
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resendVerificationCode() {
        String phoneNumber = binding.phoneNumberEditText.getText().toString().trim();
        
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!phoneNumber.startsWith("+")) {
            phoneNumber = "+1" + phoneNumber;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .setForceResendingToken(resendToken)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyCode() {
        String code = binding.otpEditText.getText().toString().trim();
        
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "Please enter verification code", Toast.LENGTH_SHORT).show();
            return;
        }

        if (code.length() != 6) {
            Toast.makeText(this, "Please enter a valid 6-digit code", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        
        if (currentUser != null) {
            // Link the phone credential to the existing user account
            currentUser.updatePhoneNumber(credential)
                    .addOnCompleteListener(this, task -> {
                        binding.progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Log.d(TAG, "updatePhoneNumber:success");
                            save2FAStatus(true);
                            Toast.makeText(TwoFactorAuthActivity.this, 
                                "2FA enabled successfully!", Toast.LENGTH_SHORT).show();
                            finishSetup();
                        } else {
                            Log.w(TAG, "updatePhoneNumber:failure", task.getException());
                            Toast.makeText(TwoFactorAuthActivity.this, 
                                "Failed to enable 2FA: " + task.getException().getMessage(), 
                                Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void save2FAStatus(boolean enabled) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(user.getUid());
            userRef.child("twoFactorEnabled").setValue(enabled)
                    .addOnSuccessListener(aVoid -> 
                        Log.d(TAG, "2FA status saved successfully"))
                    .addOnFailureListener(e -> 
                        Log.e(TAG, "Failed to save 2FA status", e));
        }
    }

    private void skipTwoFactorAuth() {
        if (isFromSettings) {
            finish();
        } else {
            finishSetup();
        }
    }

    private void finishSetup() {
        if (isFromSettings) {
            finish();
        } else {
            startActivity(new Intent(TwoFactorAuthActivity.this, HomePageActivity.class));
            finish();
        }
    }
}
