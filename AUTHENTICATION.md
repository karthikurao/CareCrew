# Firebase Authentication Implementation Guide

## Overview

The CareCrew app implements Firebase Email/Password Authentication with the following features:
- Email/Password authentication
- Google Sign-In integration
- Persistent login sessions
- Secure logout functionality
- Session validation on app startup

## Architecture

### Authentication Flow

```
App Launch → SplashActivity → Check Auth Status
                                    ↓
                        Is user authenticated?
                    ↙                          ↘
                YES                              NO
                 ↓                                ↓
          HomePageActivity                 SignupActivity
                                                 ↓
                                         User can navigate to
                                            LoginActivity
```

### Components

#### 1. SplashActivity
**File:** `app/src/main/java/com/societal/carecrew/SplashActivity.java`

**Purpose:** Entry point that validates user authentication status

**Implementation:**
- Checks both `SharedPreferences` and `FirebaseAuth.getCurrentUser()`
- Dual validation prevents stale sessions
- Clears invalid SharedPreferences if Firebase session is null
- Redirects to appropriate activity (Home or Signup)

**Key Code:**
```java
boolean isLoggedIn = getSharedPreferences("app_prefs", MODE_PRIVATE)
        .getBoolean("is_logged_in", false);
FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

if (isLoggedIn && currentUser != null) {
    // Redirect to HomePageActivity
} else {
    // Clear stale data and redirect to SignupActivity
}
```

#### 2. SignupActivity
**File:** `app/src/main/java/com/societal/carecrew/SignupActivity.java`

**Purpose:** Handles new user registration

**Features:**
- Email/Password signup
- Google Sign-In alternative
- Input validation (email format, password length)
- User data storage in Firebase Realtime Database
- Error handling with detailed messages

**Security Measures:**
1. **Password Validation:** Minimum 6 characters required
2. **No Password Storage:** Passwords are NOT stored in the database (only in Firebase Auth)
3. **Atomic Operations:** If database save fails, Firebase Auth user is deleted
4. **Session Management:** Sets `is_logged_in` flag in SharedPreferences

**Key Code:**
```java
mAuth.createUserWithEmailAndPassword(email, password)
    .addOnCompleteListener(this, task -> {
        if (task.isSuccessful()) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                // Don't store password - security best practice
                HelperClass helperClass = new HelperClass(name, email, username, "");
                reference.child(user.getUid()).setValue(helperClass)
                    .addOnSuccessListener(aVoid -> {
                        // Save login state
                        getSharedPreferences("app_prefs", MODE_PRIVATE).edit()
                            .putBoolean("is_logged_in", true)
                            .apply();
                        // Navigate to home
                    })
                    .addOnFailureListener(e -> {
                        // Delete user if database save fails
                        user.delete();
                    });
            }
        }
    });
```

#### 3. LoginActivity
**File:** `app/src/main/java/com/societal/carecrew/LoginActivity.java`

**Purpose:** Handles existing user authentication

**Features:**
- Email/Password login
- Google Sign-In alternative
- Email validation
- Detailed error messages
- Persistent session creation

**Key Code:**
```java
mAuth.signInWithEmailAndPassword(email, password)
    .addOnCompleteListener(this, task -> {
        if (task.isSuccessful()) {
            // Save login status
            getSharedPreferences("app_prefs", MODE_PRIVATE).edit()
                .putBoolean("is_logged_in", true)
                .apply();
            updateUI(user);
        } else {
            // Show detailed error message
            String errorMessage = task.getException().getMessage();
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    });
```

#### 4. ProfileActivity (Logout)
**File:** `app/src/main/java/com/societal/carecrew/ProfileActivity.java`

**Purpose:** Handles user logout

**Implementation:**
```java
binding.logoutButton.setOnClickListener(v -> {
    FirebaseAuth.getInstance().signOut();
    getSharedPreferences("app_prefs", MODE_PRIVATE).edit()
        .putBoolean("is_logged_in", false)
        .apply();
    
    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
    startActivity(intent);
    finish();
});
```

## Firebase Configuration

### Dependencies
**File:** `app/build.gradle.kts`

```kotlin
dependencies {
    // Firebase BOM (Bill of Materials)
    implementation(platform("com.google.firebase:firebase-bom:32.2.3"))
    
    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth-ktx")
    
    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.6.0")
    
    // Firebase Realtime Database
    implementation("com.google.firebase:firebase-database")
}
```

### Google Services Plugin
**File:** `build.gradle`

```groovy
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath libs.google.services
    }
}
```

**File:** `app/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}
```

### Configuration Files

1. **google-services.json** - Located in `app/` directory
   - Contains Firebase project configuration
   - Includes OAuth client IDs for Google Sign-In

2. **strings.xml** - Contains `default_web_client_id`
   ```xml
   <string name="default_web_client_id">YOUR_WEB_CLIENT_ID</string>
   ```

## Security Best Practices

### Implemented
✅ **No Password Storage:** Passwords are never stored in the database  
✅ **Session Validation:** Dual-check with SharedPreferences and FirebaseAuth  
✅ **Error Handling:** Detailed error messages from Firebase  
✅ **Data Consistency:** Delete auth user if database save fails  
✅ **Input Validation:** Email format and password length checks  

### Recommendations
⚠️ **Email Verification:** Consider adding email verification after signup  
⚠️ **Password Reset:** Implement forgot password functionality  
⚠️ **Two-Factor Authentication:** Add 2FA for enhanced security (planned)  
⚠️ **Rate Limiting:** Consider implementing rate limiting for login attempts  

## User Data Structure

Users are stored in Firebase Realtime Database at `/users/{userId}`

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "username": "johndoe",
  "password": "",  // Always empty - never store passwords
  "profileImageUrl": "",
  "bio": "",
  "hoursVolunteered": 0,
  "opportunitiesParticipated": 0,
  "groupsJoined": 0,
  "skills": [],
  "interests": [],
  "causes": [],
  "location": "",
  "aboutMe": ""
}
```

## Google Sign-In Flow

1. User clicks "Sign in with Google" button
2. Google Sign-In intent is launched
3. User selects Google account
4. App receives Google ID token
5. Token is exchanged for Firebase Auth credential
6. User is signed in to Firebase
7. User data is saved to Realtime Database
8. Session is persisted in SharedPreferences
9. User is redirected to HomePageActivity

## Testing Authentication

### Manual Testing Steps

1. **New User Signup**
   - Open app → Signup screen
   - Enter name, email, username, password
   - Click "Sign Up"
   - Verify redirect to HomePageActivity
   - Check Firebase Console for new user

2. **Existing User Login**
   - Open app → Navigate to Login
   - Enter email and password
   - Click "Login"
   - Verify redirect to HomePageActivity

3. **Google Sign-In**
   - Click "Sign in with Google"
   - Select Google account
   - Verify redirect to HomePageActivity

4. **Persistent Session**
   - Login successfully
   - Close app completely
   - Reopen app
   - Verify automatic redirect to HomePageActivity

5. **Logout**
   - Navigate to Profile
   - Click Logout
   - Verify redirect to Login screen
   - Close and reopen app
   - Verify redirect to Signup screen (not auto-login)

### Edge Cases

1. **Invalid Email:** App shows "Please enter a valid email"
2. **Short Password:** App shows "Password must be at least 6 characters"
3. **Wrong Credentials:** App shows Firebase error message
4. **Network Error:** Firebase handles with error callback
5. **Stale Session:** SplashActivity clears and redirects to login

## Troubleshooting

### Common Issues

1. **"Authentication failed" error**
   - Check internet connection
   - Verify Firebase project configuration
   - Ensure google-services.json is up to date

2. **Google Sign-In not working**
   - Verify `default_web_client_id` in strings.xml
   - Check SHA-1 fingerprint in Firebase Console
   - Ensure Google Sign-In is enabled in Firebase Console

3. **User not persisting**
   - Check SharedPreferences implementation
   - Verify FirebaseAuth.getCurrentUser() returns user

4. **Build errors**
   - Ensure repositories are configured in build.gradle
   - Sync Gradle files
   - Clear cache and rebuild

## Future Enhancements

- [ ] Email verification after signup
- [ ] Forgot password functionality
- [ ] Two-Factor Authentication (2FA)
- [ ] Social login (Facebook, Twitter)
- [ ] Biometric authentication
- [ ] Password strength indicator
- [ ] Account deletion feature
- [ ] Profile picture upload during signup

## References

- [Firebase Authentication Documentation](https://firebase.google.com/docs/auth)
- [Firebase Auth Best Practices](https://firebase.google.com/docs/auth/best-practices)
- [Google Sign-In for Android](https://developers.google.com/identity/sign-in/android/start)
