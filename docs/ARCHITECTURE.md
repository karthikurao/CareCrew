# Push Notifications Architecture

## System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Firebase Cloud Messaging                 │
│                  (FCM - Google Cloud Service)                │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       │ Push Notification
                       ▼
┌─────────────────────────────────────────────────────────────┐
│                    Android Device                            │
│  ┌───────────────────────────────────────────────────────┐  │
│  │              FCMService.java                          │  │
│  │  - onMessageReceived()                                │  │
│  │  - onNewToken()                                       │  │
│  │  - handleDataMessage()                                │  │
│  │  - sendNotification()                                 │  │
│  └────────────────────┬──────────────────────────────────┘  │
│                       │                                      │
│                       ▼                                      │
│  ┌───────────────────────────────────────────────────────┐  │
│  │         NotificationHelper.java                       │  │
│  │  - createNotificationChannels()                       │  │
│  │  - sendUrgentNotification()                           │  │
│  │  - sendGeneralNotification()                          │  │
│  │  - subscribeToUrgentNotifications()                   │  │
│  │  - unsubscribeFromUrgentNotifications()               │  │
│  │  - getFCMToken()                                      │  │
│  └────────────────────┬──────────────────────────────────┘  │
│                       │                                      │
│                       ▼                                      │
│  ┌───────────────────────────────────────────────────────┐  │
│  │            Android Notification System                │  │
│  │  - Notification Channels                              │  │
│  │  - Notification Manager                               │  │
│  │  - Status Bar Integration                             │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

## Component Diagram

```
┌──────────────────┐      ┌──────────────────┐
│ SplashActivity   │      │  LoginActivity   │
│ ───────────────  │      │  ──────────────  │
│ onCreate()       │      │  updateUI()      │
│   └─ Initialize  │      │    └─ Subscribe  │
│      Channels    │      │       to Topics  │
└──────────────────┘      └──────────────────┘
                                    │
                                    │
                          ┌─────────▼─────────┐
                          │  SignupActivity   │
                          │  ───────────────  │
                          │  updateUI()       │
                          │    └─ Subscribe   │
                          │       to Topics   │
                          └───────────────────┘
                                    │
                    ┌───────────────┴───────────────┐
                    │                               │
        ┌───────────▼──────────┐      ┌────────────▼──────────┐
        │ NotificationHelper   │      │    FCMService         │
        │ ──────────────────   │      │    ────────────       │
        │ Topic Management     │      │    Message Handler    │
        │ Local Notifications  │      │    Token Management   │
        │ Channel Creation     │      │    Remote Notif.      │
        └──────────────────────┘      └───────────────────────┘
```

## Data Flow

### 1. App Initialization
```
App Launch
    │
    ▼
SplashActivity.onCreate()
    │
    ├─► NotificationHelper.createNotificationChannels()
    │       │
    │       ├─► Create "Urgent Volunteer Needs" channel (High Priority)
    │       └─► Create "General Notifications" channel (Default Priority)
    │
    ▼
Check User Login Status
```

### 2. User Authentication Flow
```
Login/Signup Success
    │
    ▼
updateUI()
    │
    ▼
NotificationHelper.subscribeToUrgentNotifications()
    │
    ├─► FirebaseMessaging.subscribeToTopic("urgent_needs")
    │
    └─► Store in FCM backend
```

### 3. Receiving Notifications
```
FCM Server sends notification
    │
    ▼
FCMService.onMessageReceived()
    │
    ├─► Parse notification payload
    │       │
    │       ├─► Check for notification data
    │       └─► Check for data payload
    │
    ├─► handleDataMessage() (if data payload)
    │
    └─► sendNotification()
            │
            ├─► Create PendingIntent
            │
            ├─► Build notification with:
            │       - Title
            │       - Body
            │       - Icon
            │       - Channel
            │       - Priority
            │
            └─► NotificationManager.notify()
```

### 4. Local Notification Flow
```
App Component (e.g., when new urgent opportunity created)
    │
    ▼
NotificationHelper.sendUrgentNotification(context, title, message)
    │
    ├─► Create Intent → HomePageActivity
    │
    ├─► Create PendingIntent with FLAG_IMMUTABLE
    │
    ├─► Build NotificationCompat.Builder
    │       - Channel: urgent_volunteer_channel
    │       - Priority: HIGH
    │       - Icon: ic_notification
    │
    └─► NotificationManager.notify()
```

## File Structure

```
CareCrew/
├── app/
│   ├── build.gradle.kts                    [Modified: +1 FCM dependency]
│   └── src/main/
│       ├── AndroidManifest.xml             [Modified: +FCM service, +permission]
│       ├── java/com/societal/carecrew/
│       │   ├── FCMService.java             [NEW: 102 lines]
│       │   ├── NotificationHelper.java     [NEW: 145 lines]
│       │   ├── Opportunity.java            [Modified: +isUrgent field]
│       │   ├── SplashActivity.java         [Modified: +channel init]
│       │   ├── LoginActivity.java          [Modified: +subscription]
│       │   └── SignupActivity.java         [Modified: +subscription]
│       └── res/
│           └── drawable/
│               └── ic_notification.xml     [NEW: notification icon]
├── build.gradle                            [Modified: +repositories]
├── README.md                               [Modified: +notifications section]
└── docs/
    ├── PUSH_NOTIFICATIONS.md               [NEW: 221 lines - User Guide]
    └── IMPLEMENTATION_SUMMARY.md           [NEW: 188 lines - Dev Guide]
```

## Key Classes and Methods

### FCMService.java
```java
public class FCMService extends FirebaseMessagingService {
    // Receives push notifications from FCM
    void onMessageReceived(RemoteMessage)
    
    // Called when FCM token is updated
    void onNewToken(String token)
    
    // Handles data payload from notifications
    void handleDataMessage(RemoteMessage)
    
    // Displays notification to user
    void sendNotification(String title, String body)
    
    // Registers token with server (placeholder)
    void sendRegistrationToServer(String token)
}
```

### NotificationHelper.java
```java
public class NotificationHelper {
    // Creates notification channels (one-time setup)
    static void createNotificationChannels(Context)
    
    // Sends urgent notification locally
    static void sendUrgentNotification(Context, String title, String message)
    
    // Sends general notification locally
    static void sendGeneralNotification(Context, String title, String message)
    
    // Subscribes user to urgent notifications topic
    static void subscribeToUrgentNotifications()
    
    // Unsubscribes user from urgent notifications
    static void unsubscribeFromUrgentNotifications()
    
    // Gets FCM token for current device
    static void getFCMToken(OnTokenReceivedListener)
    
    // Callback interface for token retrieval
    interface OnTokenReceivedListener {
        void onTokenReceived(String token)
        void onTokenFailed(Exception)
    }
}
```

### Opportunity.java (Extended)
```java
public class Opportunity implements Parcelable {
    private String title;
    private String description;
    private String date;
    private String location;
    private String category;
    private boolean isUrgent;  // NEW FIELD
    
    // Getters, setters, Parcelable implementation
}
```

## Notification Channels

### Channel 1: Urgent Volunteer Needs
- **ID**: `urgent_volunteer_channel`
- **Name**: "Urgent Volunteer Needs"
- **Importance**: HIGH
- **Features**: Vibration, LED, Sound
- **Use Case**: Time-sensitive volunteer opportunities

### Channel 2: General Notifications
- **ID**: `general_volunteer_channel`
- **Name**: "General Notifications"
- **Importance**: DEFAULT
- **Features**: Standard notification behavior
- **Use Case**: Regular updates, reminders

## Security Features

1. **PendingIntent Security**
   - Uses `FLAG_IMMUTABLE` (required for Android 12+)
   - Prevents modification by other apps

2. **Service Export Protection**
   - `android:exported="false"` in manifest
   - Prevents external apps from starting the service

3. **Permission Management**
   - `POST_NOTIFICATIONS` for Android 13+
   - Users have full control over notifications

4. **Data Validation**
   - Null checks on all notification data
   - Proper exception handling

## Integration Points

### Current Implementation
- ✅ Automatic topic subscription on login/signup
- ✅ Notification channels created on app launch
- ✅ FCM service receives and displays notifications
- ✅ Support for both notification and data payloads

### Future Enhancements
- 🔲 Store FCM tokens in Firebase Realtime Database
- 🔲 Location-based notification targeting
- 🔲 User preference settings for notification types
- 🔲 In-app notification history/inbox
- 🔲 Rich notifications with action buttons
- 🔲 Scheduled notifications
- 🔲 Notification analytics

## Testing Checklist

- [ ] Notifications received when app is in foreground
- [ ] Notifications received when app is in background
- [ ] Notifications received when app is closed
- [ ] Tapping notification opens HomePageActivity
- [ ] Notification channels visible in app settings
- [ ] Topic subscription succeeds on login
- [ ] Topic subscription succeeds on signup
- [ ] FCM token generated successfully
- [ ] Notification icon displays correctly
- [ ] High-priority notifications show heads-up display
- [ ] Permissions requested on Android 13+

## Performance Metrics

- **App Size Impact**: ~10KB (2 Java classes + 1 icon)
- **Memory Overhead**: Minimal (FCM SDK managed by system)
- **Battery Impact**: Negligible (FCM uses Google Play Services)
- **Network Usage**: Minimal (only when receiving notifications)

## Compliance & Best Practices

✅ Follows Android notification best practices
✅ Complies with Google Play Store policies
✅ Respects user notification preferences
✅ Provides clear, actionable notification content
✅ Uses appropriate priority levels
✅ Implements proper security measures
✅ Handles permissions correctly
