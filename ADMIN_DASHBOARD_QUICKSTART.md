# Admin Dashboard Quick Start Guide

## Prerequisites

Before testing the Admin Dashboard feature, ensure you have:

1. ✅ Android Studio installed
2. ✅ Firebase project configured with:
   - Firebase Authentication enabled
   - Firebase Realtime Database enabled
   - `google-services.json` file in `/app` directory
3. ✅ At least one user account created in the app

## Setup Instructions

### Step 1: Grant Admin Access to a User

1. Open your Firebase Console: https://console.firebase.google.com/
2. Select your Care Crew project
3. Navigate to **Realtime Database**
4. Find your user under the `/users` node
5. Click on the user's UID
6. Click the **+** icon to add a new field
7. Add the following:
   - **Name**: `isAdmin`
   - **Type**: `boolean`
   - **Value**: `true`
8. Click **Add**

Your user node should now look like this:

```json
{
  "users": {
    "USER_UID_HERE": {
      "name": "John Doe",
      "email": "john@example.com",
      "username": "johndoe",
      "isAdmin": true,
      ...
    }
  }
}
```

### Step 2: Add Sample Events (Optional)

To test the event management feature, add some sample events:

1. In Firebase Console, navigate to **Realtime Database**
2. Click on the root node
3. Click the **+** icon to add a new node
4. Add the following:
   - **Name**: `events`
   - Click **+** to add a child
   - **Name**: `event1` (or any unique ID)
   - Add the following fields:

```json
{
  "events": {
    "event1": {
      "eventId": "event1",
      "title": "Blood Donation Drive",
      "description": "Help save lives by donating blood at our community center",
      "date": "2024-12-15",
      "location": "Community Center, Main Street",
      "category": "Health",
      "creatorId": "admin123",
      "timestamp": 1700000000000,
      "participantCount": 15
    },
    "event2": {
      "eventId": "event2",
      "title": "Flood Relief Effort",
      "description": "Volunteer to help flood victims in the riverside district",
      "date": "2024-12-20",
      "location": "Riverside District",
      "category": "Disaster Relief",
      "creatorId": "admin123",
      "timestamp": 1700100000000,
      "participantCount": 42
    }
  }
}
```

### Step 3: Build and Run the App

1. Open the project in Android Studio
2. Sync Gradle files
3. Connect your Android device or start an emulator
4. Click **Run** ▶️

### Step 4: Access the Admin Dashboard

1. **Login** with the admin user credentials
2. Navigate to the **Profile** tab (bottom navigation)
3. You should see an **"Admin Dashboard"** button
4. Tap the button to open the Admin Dashboard

## Testing the Admin Dashboard

### Test Case 1: View Analytics

✅ **Expected Result**: You should see four analytics cards showing:
- Total Users (count of all users in Firebase)
- Total Events (count of all events)
- Total Groups (count of all groups)
- Total Posts (count of all posts)

### Test Case 2: View Events List

✅ **Expected Result**: 
- A list of all events should be displayed
- Each event card shows: title, description, date, location, category, participant count
- Each event has a delete button (X icon)

### Test Case 3: Delete an Event

1. Tap the **delete button** (X icon) on any event
2. A confirmation dialog should appear
3. Tap **"Delete"**

✅ **Expected Result**:
- The event is removed from the list
- A success toast message appears
- Analytics are refreshed (Total Events count decreases)

### Test Case 4: Refresh Data

1. Tap the **refresh button** (top right corner)

✅ **Expected Result**:
- Analytics are reloaded
- Events list is refreshed
- All data is up-to-date

### Test Case 5: Non-Admin Access

1. Logout from the admin account
2. Login with a non-admin user (user without `isAdmin: true`)
3. Navigate to Profile

✅ **Expected Result**:
- Admin Dashboard button should NOT be visible
- If user somehow accesses AdminDashboardActivity directly, they should see "Access Denied" message

## Troubleshooting

### Problem: "Admin Dashboard" button not visible

**Solutions:**
1. Verify the user has `isAdmin: true` in Firebase Database
2. Check Firebase connection (look for Firebase logs in Logcat)
3. Force close and reopen the app
4. Clear app data and login again

### Problem: Analytics showing 0

**Solutions:**
1. Check if Firebase Database has data in correct structure
2. Verify Firebase Database rules allow read access
3. Check internet connection
4. Look for error messages in Logcat

### Problem: Events not loading

**Solutions:**
1. Ensure `/events` node exists in Firebase
2. Verify event data structure matches the Event model
3. Check Firebase Database read permissions
4. Review Logcat for error messages

### Problem: Delete not working

**Solutions:**
1. Check Firebase Database write permissions
2. Verify admin has proper permissions in Firebase rules
3. Look for error toast messages
4. Check Logcat for detailed error information

## Recommended Firebase Security Rules

For production, implement proper security rules:

```json
{
  "rules": {
    "users": {
      "$uid": {
        ".read": "auth != null",
        ".write": "auth != null && auth.uid === $uid"
      }
    },
    "events": {
      ".read": true,
      "$eventId": {
        ".write": "auth != null && root.child('users').child(auth.uid).child('isAdmin').val() === true"
      }
    },
    "groups": {
      ".read": true,
      ".write": "auth != null"
    },
    "posts": {
      ".read": true,
      ".write": "auth != null"
    }
  }
}
```

## Expected App Behavior

### For Admin Users:
1. ✅ See "Admin Dashboard" button on Profile screen
2. ✅ Can access Admin Dashboard
3. ✅ Can view all analytics
4. ✅ Can view all events
5. ✅ Can delete events
6. ✅ Can refresh data

### For Regular Users:
1. ✅ Do NOT see "Admin Dashboard" button on Profile screen
2. ✅ Cannot access Admin Dashboard (if attempted directly)
3. ✅ See "Access Denied" message if they try to access

## Screenshots

After testing, you should see:

1. **Profile Screen (Admin User)**
   - Edit Profile button
   - **Admin Dashboard button** ← Should be visible
   - Logout button

2. **Admin Dashboard Screen**
   - Header with back and refresh buttons
   - Four analytics cards (Users, Events, Groups, Posts)
   - Events list with delete buttons

3. **Delete Confirmation Dialog**
   - Title: "Delete Event"
   - Message: "Are you sure you want to delete this event?"
   - Delete and Cancel buttons

## Support

If you encounter any issues:

1. Check the logs in Android Studio Logcat
2. Verify Firebase configuration
3. Review the ADMIN_DASHBOARD_DOCUMENTATION.md file
4. Create an issue on the GitHub repository

## Next Steps

After testing the basic functionality, consider:

1. Adding more events to test with large datasets
2. Testing with multiple admin users
3. Implementing Firebase Security Rules
4. Adding event creation functionality
5. Adding event editing functionality
6. Implementing search and filter features

---

**Happy Testing! 🚀**
