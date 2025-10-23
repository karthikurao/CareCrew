# Two-Factor Authentication (2FA) Testing Guide

## Overview
This document provides testing guidance for the Two-Factor Authentication (2FA) feature in Care Crew.

## Prerequisites
1. Firebase project with Phone Authentication enabled
2. Android device or emulator (Phone authentication may not work on all emulators)
3. Valid phone number for testing

## Test Scenarios

### 1. 2FA Setup During Signup
**Steps:**
1. Launch the app
2. Click "Sign Up"
3. Fill in the registration form (name, email, username, password)
4. Click "Sign Up"
5. After successful signup, verify you're redirected to TwoFactorAuthActivity
6. Enter a valid phone number with country code (e.g., +1234567890)
7. Click "Send Verification Code"
8. Verify you receive an SMS with a 6-digit code
9. Enter the code in the OTP field
10. Click "Verify Code"
11. Verify successful 2FA setup and redirection to HomePageActivity

**Expected Result:** User account is created with 2FA enabled

**Alternative Flow:**
- At step 5, click "Skip for now" to skip 2FA setup
- Verify redirection to HomePageActivity without 2FA enabled

### 2. Enable 2FA from Profile
**Steps:**
1. Log in to the app with an existing account (without 2FA)
2. Navigate to Profile page
3. Verify "Enable Two-Factor Authentication" button is visible
4. Click "Enable Two-Factor Authentication"
5. Follow steps 6-10 from Test Scenario 1
6. Verify successful 2FA setup
7. Navigate back to Profile
8. Verify button text changed to "Disable Two-Factor Authentication"

**Expected Result:** 2FA is enabled for the existing account

### 3. 2FA Status Persistence
**Steps:**
1. Enable 2FA on an account (using Test Scenario 1 or 2)
2. Log out from the app
3. Log in again with the same account
4. Navigate to Profile page
5. Verify the 2FA button shows "Disable Two-Factor Authentication"

**Expected Result:** 2FA status is persisted across sessions

### 4. Resend Verification Code
**Steps:**
1. Start 2FA setup flow
2. Enter phone number and click "Send Verification Code"
3. Wait for the OTP screen to appear
4. Click "Resend Code" button
5. Verify a new code is sent to the phone

**Expected Result:** New verification code is received

### 5. Invalid OTP Handling
**Steps:**
1. Start 2FA setup flow
2. Enter phone number and click "Send Verification Code"
3. Enter an incorrect 6-digit code
4. Click "Verify Code"
5. Verify an error message is displayed

**Expected Result:** User is informed about invalid code

### 6. Phone Number Validation
**Steps:**
1. Start 2FA setup flow
2. Leave phone number field empty
3. Click "Send Verification Code"
4. Verify error message: "Please enter phone number"

**Expected Result:** Validation prevents empty phone numbers

## Firebase Database Structure
After enabling 2FA, verify the following in Firebase Realtime Database:
```
users/
  {userId}/
    twoFactorEnabled: true
    (other user fields...)
```

## Known Limitations
1. Phone authentication requires a real device or an emulator with Google Play Services
2. SMS delivery depends on the phone carrier and may have delays
3. Some countries/phone numbers may not be supported by Firebase Phone Auth
4. Rate limiting applies - too many verification attempts may temporarily block the number

## Security Considerations
1. Phone numbers are verified through Firebase, ensuring authenticity
2. Verification codes expire after 60 seconds
3. 2FA status is stored securely in Firebase Realtime Database
4. Phone credentials are linked to the user's Firebase account, not stored separately

## Troubleshooting

### Issue: Not receiving SMS
- Verify Phone Authentication is enabled in Firebase Console
- Check if the phone number is in a supported region
- Ensure the app's SHA-1 fingerprint is configured in Firebase
- Try using a different phone number

### Issue: "Verification failed" error
- Check Firebase Console for error logs
- Verify internet connectivity
- Ensure Firebase dependencies are properly configured in build.gradle

### Issue: App crashes on 2FA screen
- Check Android Logcat for error messages
- Verify all required permissions are granted
- Ensure google-services.json is properly configured

## Manual Verification Checklist
- [ ] New users can enable 2FA during signup
- [ ] New users can skip 2FA during signup
- [ ] Existing users can enable 2FA from Profile
- [ ] 2FA button text updates correctly based on status
- [ ] Phone verification code is received via SMS
- [ ] Invalid OTP shows appropriate error
- [ ] Resend code functionality works
- [ ] 2FA status persists after logout/login
- [ ] Firebase database updates with twoFactorEnabled field
