# Two-Factor Authentication (2FA) Implementation Flow

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                     CareCrew 2FA Architecture                    │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────┐         ┌──────────────────┐
│  SignupActivity  │────────▶│ TwoFactorAuth    │
│                  │         │ Activity         │
└──────────────────┘         └──────────────────┘
                                      │
                                      │ Phone Auth
                                      ▼
┌──────────────────┐         ┌──────────────────┐
│  ProfileActivity │◀───────▶│ Firebase Phone   │
│  (Enable/Disable)│         │ Authentication   │
└──────────────────┘         └──────────────────┘
                                      │
                                      ▼
┌──────────────────┐         ┌──────────────────┐
│  HelperClass     │◀───────▶│ Firebase         │
│  (2FA Status)    │         │ Realtime DB      │
└──────────────────┘         └──────────────────┘
```

## User Journey

### New User Signup with 2FA
```
┌─────────────┐
│ Launch App  │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│  Sign Up    │
│  Form       │
└──────┬──────┘
       │
       ▼
┌─────────────────────┐
│ TwoFactorAuth       │
│ Activity            │
│ ┌─────────────────┐ │
│ │ Enter Phone #   │ │
│ │ +1234567890     │ │
│ └────────┬────────┘ │
│          │          │
│          ▼          │
│ ┌─────────────────┐ │
│ │ Send Code       │ │
│ └────────┬────────┘ │
│          │          │
│          ▼          │
│ ┌─────────────────┐ │
│ │ Enter OTP       │ │
│ │ [1][2][3][4]... │ │
│ └────────┬────────┘ │
│          │          │
│          ▼          │
│ ┌─────────────────┐ │
│ │ Verify Code     │ │
│ └─────────────────┘ │
└──────┬──────────────┘
       │
       ▼
┌─────────────┐
│ Home Page   │
└─────────────┘
```

### Existing User Enabling 2FA
```
┌─────────────┐
│ Profile     │
│ Page        │
└──────┬──────┘
       │
       ▼
┌─────────────────────┐
│ Enable 2FA Button   │
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│ TwoFactorAuth       │
│ Activity            │
│ (fromSettings=true) │
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│ 2FA Setup Process   │
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│ Back to Profile     │
│ (Button: Disable)   │
└─────────────────────┘
```

## Component Interaction

### 1. TwoFactorAuthActivity
**Purpose:** Handle phone verification and OTP validation

**Key Methods:**
- `sendVerificationCode()` - Initiates phone verification
- `resendVerificationCode()` - Resends OTP if needed
- `verifyCode()` - Validates user-entered OTP
- `signInWithPhoneAuthCredential()` - Links phone to account
- `save2FAStatus()` - Updates Firebase database

**UI States:**
- Phone Number Input (default)
- OTP Verification (after code sent)
- Loading (during API calls)

### 2. ProfileActivity Additions
**New Features:**
- 2FA enable/disable button
- Dynamic button text based on status
- Navigation to TwoFactorAuthActivity

**New Method:**
- `updateTwoFactorButton(boolean enabled)` - Updates UI based on 2FA status

### 3. SignupActivity Integration
**Change:**
- After successful signup, redirect to TwoFactorAuthActivity
- Allow users to skip 2FA setup

### 4. HelperClass Extension
**New Field:**
- `boolean twoFactorEnabled` - Tracks 2FA status

**Purpose:**
- Store 2FA status in Firebase Realtime Database
- Persist across sessions

## Firebase Integration

### Phone Authentication Flow
```
User Input Phone     Firebase Sends SMS     User Enters OTP
     │                      │                      │
     ▼                      ▼                      ▼
┌──────────┐         ┌──────────┐         ┌──────────┐
│ Activity │────────▶│ Firebase │────────▶│ Activity │
└──────────┘         │  Auth    │         └──────────┘
                     └──────────┘
                           │
                           ▼
                  ┌────────────────┐
                  │ Phone Linked   │
                  │ to Firebase    │
                  │ User Account   │
                  └────────────────┘
```

### Database Structure
```
firebase-realtime-database/
└── users/
    └── {userId}/
        ├── name: "John Doe"
        ├── email: "john@example.com"
        ├── username: "johndoe"
        ├── twoFactorEnabled: true  ← New field
        └── ... (other fields)
```

## Security Features

1. **Input Validation**
   - Empty phone number check
   - OTP length validation (6 digits)
   - Country code auto-prepend

2. **Firebase Security**
   - Server-side OTP generation
   - Time-limited verification codes (60s)
   - Rate limiting to prevent abuse
   - HTTPS encrypted communication

3. **Data Protection**
   - Phone number managed by Firebase (not stored separately)
   - 2FA status in secure Firebase Realtime Database
   - No sensitive data in local storage

## Files Modified/Created

### New Files (4)
1. `TwoFactorAuthActivity.java` - Main 2FA logic (203 lines)
2. `activity_two_factor_auth.xml` - 2FA UI layout (149 lines)
3. `docs/2FA_TESTING_GUIDE.md` - Testing documentation (133 lines)
4. `docs/2FA_SECURITY_SUMMARY.md` - Security analysis (117 lines)

### Modified Files (10)
1. `build.gradle.kts` - Firebase Phone Auth dependency
2. `build.gradle` - Repository configuration
3. `AndroidManifest.xml` - Activity registration
4. `HelperClass.java` - 2FA status field
5. `SignupActivity.java` - Navigation to 2FA
6. `ProfileActivity.java` - 2FA management
7. `activity_profile.xml` - 2FA button
8. `strings.xml` - 2FA strings
9. `README.md` - Feature documentation
10. `gradlew` - Made executable

**Total Changes:** 704 insertions, 3 deletions

## Usage Statistics

- **Lines of Code Added:** ~700
- **New Activities:** 1
- **New Layouts:** 1
- **New String Resources:** 12
- **Documentation Pages:** 2
- **Test Scenarios Documented:** 6
