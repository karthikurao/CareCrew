# Push Notifications Setup Guide

## Overview

Care Crew now supports push notifications to alert volunteers about urgent needs in their community. This feature uses Firebase Cloud Messaging (FCM) to deliver real-time notifications.

## Features

- ✅ Automatic subscription to urgent notifications on login/signup
- ✅ Support for notification channels (Android 8.0+)
- ✅ High-priority notifications for urgent volunteer needs
- ✅ Topic-based messaging for scalable notifications
- ✅ Local notification support for in-app alerts

## Setup Instructions

### 1. Firebase Console Configuration

1. Go to the [Firebase Console](https://console.firebase.google.com/)
2. Select your Care Crew project
3. Navigate to **Cloud Messaging** under the **Build** section
4. Enable Cloud Messaging API (if not already enabled)

### 2. Server Key (for sending notifications)

To send notifications from a backend server:

1. In Firebase Console, go to **Project Settings** > **Cloud Messaging**
2. Copy the **Server Key** (keep this secure)
3. Use this key when sending notifications via the FCM API

### 3. Testing Notifications

#### Option A: Using Firebase Console

1. Go to Firebase Console > **Cloud Messaging**
2. Click **Send your first message**
3. Enter notification title and text
4. Under **Target**, select **Topic** and enter `urgent_needs`
5. Click **Send message**

#### Option B: Using FCM REST API

Send a POST request to `https://fcm.googleapis.com/fcm/send`:

```bash
curl -X POST -H "Authorization: key=YOUR_SERVER_KEY" \
-H "Content-Type: application/json" \
-d '{
  "to": "/topics/urgent_needs",
  "notification": {
    "title": "Urgent: Flood Relief Needed",
    "body": "Volunteers needed immediately for flood relief in downtown area"
  },
  "data": {
    "title": "Urgent: Flood Relief Needed",
    "body": "Volunteers needed immediately for flood relief in downtown area",
    "urgent": "true"
  }
}' https://fcm.googleapis.com/fcm/send
```

## How It Works

### Automatic Subscription

When users sign up or log in, they are automatically subscribed to the `urgent_needs` topic:

```java
NotificationHelper.subscribeToUrgentNotifications();
```

### Notification Channels

Two notification channels are created:

1. **Urgent Volunteer Needs** (`urgent_volunteer_channel`)
   - High priority
   - Vibration and LED enabled
   - For time-sensitive volunteer opportunities

2. **General Notifications** (`general_volunteer_channel`)
   - Default priority
   - For regular updates

### Receiving Notifications

The `FCMService` class handles incoming notifications:

- **Foreground**: Notifications are displayed with a custom notification builder
- **Background**: Handled automatically by Firebase
- **Data payloads**: Custom handling for urgent flags and additional data

## Notification Priority

### Urgent Notifications

For urgent volunteer needs, send notifications with:
- High priority setting
- `urgent: true` in data payload
- Sent to `urgent_needs` topic

### Regular Notifications

For general updates:
- Default priority
- Sent to specific user tokens or other topics

## Opportunity Model with Urgent Flag

Volunteer opportunities can now be marked as urgent:

```java
Opportunity opportunity = new Opportunity(
    "Flood Relief",
    "Help needed for flood victims",
    "2024-01-15",
    "Downtown Area",
    true  // isUrgent flag
);
```

## Sending Local Notifications

To send local notifications within the app:

```java
// Urgent notification
NotificationHelper.sendUrgentNotification(
    context,
    "Urgent: Blood Donation Needed",
    "O- blood type needed at City Hospital immediately"
);

// General notification
NotificationHelper.sendGeneralNotification(
    context,
    "New Volunteer Event",
    "Community cleanup event scheduled for Saturday"
);
```

## Getting FCM Token

To get the device's FCM token (for targeted notifications):

```java
NotificationHelper.getFCMToken(new NotificationHelper.OnTokenReceivedListener() {
    @Override
    public void onTokenReceived(String token) {
        // Store token in database for targeted notifications
        Log.d("FCM", "Token: " + token);
    }

    @Override
    public void onTokenFailed(Exception exception) {
        // Handle error
        Log.e("FCM", "Failed to get token", exception);
    }
});
```

## Notification Permissions

On Android 13+ (API level 33), users must grant notification permission. The app requests this permission through:

```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

Users can manage notification preferences in their device settings.

## Best Practices

1. **Use Topics for Broadcasting**: Use the `urgent_needs` topic for alerts that all volunteers should see
2. **Targeted Notifications**: Store user FCM tokens in the database for location-based or personalized notifications
3. **Rich Notifications**: Include action buttons for quick responses (e.g., "I'm Available", "View Details")
4. **Don't Overuse**: Only send urgent notifications when truly necessary to avoid notification fatigue
5. **Test Thoroughly**: Test notifications on both foreground and background app states

## Troubleshooting

### Notifications Not Received

1. Check if the app has notification permissions enabled
2. Verify that the `google-services.json` file is correctly placed in the `/app` directory
3. Ensure Firebase Cloud Messaging is enabled in Firebase Console
4. Check device logs for FCM registration errors

### Token Not Generated

1. Verify internet connectivity
2. Check that Firebase dependencies are correctly added
3. Ensure `google-services.json` is up to date

### Notifications Don't Show in Foreground

1. Check notification channel settings
2. Verify that `FCMService` is correctly registered in `AndroidManifest.xml`
3. Check device notification settings for the app

## Future Enhancements

- User preferences for notification types
- Location-based notifications
- In-app notification center
- Notification scheduling
- Rich media notifications (images, action buttons)

## Security Considerations

- Keep your Firebase Server Key secure
- Validate notification data on the server side
- Don't include sensitive information in notification payloads
- Use HTTPS for all FCM API requests

## Support

For issues or questions, please refer to:
- [Firebase Cloud Messaging Documentation](https://firebase.google.com/docs/cloud-messaging)
- [Android Notification Guide](https://developer.android.com/guide/topics/ui/notifiers/notifications)
