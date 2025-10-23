# Admin Dashboard Feature Documentation

## Overview

The Admin Dashboard feature has been successfully added to the Care Crew application. This feature provides administrators with analytics and event management capabilities.

## New Files Created

### Java Classes

1. **AdminDashboardActivity.java** - Main admin dashboard activity
   - Location: `app/src/main/java/com/societal/carecrew/AdminDashboardActivity.java`
   - Purpose: Displays analytics and manages events
   - Features:
     - Analytics overview with total counts for users, events, groups, and posts
     - Event list with delete functionality
     - Admin access verification
     - Real-time data updates from Firebase

2. **Event.java** - Event model class
   - Location: `app/src/main/java/com/societal/carecrew/Event.java`
   - Purpose: Represents event/opportunity data
   - Fields: eventId, title, description, date, location, category, creatorId, timestamp, participantCount

3. **EventAdapter.java** - RecyclerView adapter for event list
   - Location: `app/src/main/java/com/societal/carecrew/EventAdapter.java`
   - Purpose: Displays events in the admin dashboard
   - Features: Shows event details and delete button

### Layout Files

1. **activity_admin_dashboard.xml** - Main admin dashboard layout
   - Location: `app/src/main/res/layout/activity_admin_dashboard.xml`
   - Features:
     - Header with back and refresh buttons
     - Analytics cards showing:
       - Total Users
       - Total Events
       - Total Groups
       - Total Posts
     - Event management section with RecyclerView

2. **item_event_admin.xml** - Event item layout for RecyclerView
   - Location: `app/src/main/res/layout/item_event_admin.xml`
   - Features: Card view displaying event details with delete button

### Modified Files

1. **HelperClass.java**
   - Added `isAdmin` field (Boolean)
   - Added getter and setter for isAdmin field
   - Purpose: Track which users have admin privileges

2. **ProfileActivity.java**
   - Added logic to show "Admin Dashboard" button for admin users
   - Added click listener to navigate to AdminDashboardActivity
   - Features: Only visible for users with `isAdmin = true` in Firebase

3. **activity_profile.xml**
   - Added "Admin Dashboard" button (initially hidden)
   - Visibility controlled by user's admin status

4. **AndroidManifest.xml**
   - Registered AdminDashboardActivity

5. **strings.xml**
   - Added new string resources for admin dashboard

## How to Use

### Setting Up an Admin User

To grant admin access to a user in Firebase:

1. Open Firebase Console
2. Navigate to Realtime Database
3. Find the user under `/users/{userId}`
4. Add a new field: `isAdmin: true`

Example Firebase structure:
```
users/
  {userId}/
    name: "Admin User"
    email: "admin@example.com"
    isAdmin: true
    ...
```

### Accessing the Admin Dashboard

1. Login as a user with `isAdmin = true`
2. Navigate to the Profile screen
3. The "Admin Dashboard" button will be visible
4. Tap the button to open the Admin Dashboard

### Admin Dashboard Features

#### Analytics Overview
- **Total Users**: Shows the count of all registered users
- **Total Events**: Shows the count of all events/opportunities
- **Total Groups**: Shows the count of all volunteer groups
- **Total Posts**: Shows the count of all posts

#### Event Management
- **View All Events**: Displays a list of all events in the system
- **Delete Events**: Tap the delete button (X icon) on any event to remove it
  - A confirmation dialog will appear before deletion
- **Refresh**: Tap the refresh button to reload analytics and events

### Firebase Database Structure

The admin dashboard expects the following Firebase structure:

```
firebase-root/
  users/
    {userId}/
      isAdmin: Boolean
      name: String
      email: String
      ...
  
  events/
    {eventId}/
      eventId: String
      title: String
      description: String
      date: String
      location: String
      category: String
      creatorId: String
      timestamp: Number
      participantCount: Number
  
  groups/
    {groupId}/
      ...
  
  posts/
    {postId}/
      ...
```

## Security Considerations

1. **Access Control**: The AdminDashboardActivity checks for `isAdmin` flag before allowing access
2. **Firebase Rules**: Recommended to add Firebase security rules to restrict admin operations
3. **Button Visibility**: Admin Dashboard button is only visible to admin users

Example Firebase Security Rules:
```json
{
  "rules": {
    "events": {
      ".read": true,
      ".write": "auth != null && root.child('users').child(auth.uid).child('isAdmin').val() === true"
    },
    "users": {
      "$uid": {
        ".read": "auth != null && auth.uid === $uid",
        ".write": "auth != null && auth.uid === $uid"
      }
    }
  }
}
```

## Future Enhancements

Potential improvements for the admin dashboard:

1. **Event Creation**: Add functionality to create new events from the dashboard
2. **Event Editing**: Enable editing of existing events
3. **User Management**: Add ability to manage users (ban, promote to admin, etc.)
4. **Group Management**: Add controls for managing volunteer groups
5. **Analytics Charts**: Add visual charts for better data visualization
6. **Export Data**: Allow exporting analytics data to CSV/PDF
7. **Notifications**: Send notifications to all users or specific groups
8. **Activity Logs**: Track admin actions for audit purposes
9. **Search and Filter**: Add search and filtering capabilities for events
10. **Bulk Operations**: Enable bulk delete/edit operations

## Testing Checklist

- [ ] Verify admin user can see "Admin Dashboard" button on Profile screen
- [ ] Verify non-admin users cannot see "Admin Dashboard" button
- [ ] Verify analytics cards display correct counts
- [ ] Verify event list displays all events from Firebase
- [ ] Verify delete functionality removes events from database
- [ ] Verify refresh button updates all data
- [ ] Verify access is denied for non-admin users attempting to access AdminDashboardActivity
- [ ] Test with empty database (no events)
- [ ] Test with large number of events

## Troubleshooting

### Admin Dashboard button not visible
- Ensure user has `isAdmin: true` in Firebase
- Check that ProfileActivity is loading user data correctly
- Verify binding.adminDashboardButton exists in layout

### Analytics showing 0
- Ensure Firebase database has data in correct structure
- Check network connectivity
- Verify Firebase permissions allow read access

### Events not loading
- Check Firebase database has events in `/events` node
- Verify event data structure matches Event model
- Check Logcat for error messages

## Technical Details

### Dependencies Used
- Firebase Realtime Database
- Firebase Authentication
- RecyclerView
- CardView
- Material Design Components

### Minimum SDK: 29
### Target SDK: 33

## Contact

For questions or issues related to the Admin Dashboard feature, please create an issue on the GitHub repository.

---

**Note**: This feature was implemented as part of the Care Crew project to provide administrative capabilities for managing events and viewing analytics.
