package com.societal.carecrew;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.atomic.AtomicInteger;

public class NotificationHelper {

    private static final String TAG = "NotificationHelper";
    public static final String URGENT_CHANNEL_ID = "urgent_volunteer_channel";
    private static final String URGENT_CHANNEL_NAME = "Urgent Volunteer Needs";
    private static final String GENERAL_CHANNEL_ID = "general_volunteer_channel";
    private static final String GENERAL_CHANNEL_NAME = "General Notifications";

    private static final AtomicInteger NOTIFICATION_ID_COUNTER = new AtomicInteger(1000);

    /**
     * Create notification channels for Android O and above
     */
    public static void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Urgent notification channel
            NotificationChannel urgentChannel = new NotificationChannel(
                    URGENT_CHANNEL_ID,
                    URGENT_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            urgentChannel.setDescription("Notifications for urgent volunteer opportunities");
            urgentChannel.enableVibration(true);
            urgentChannel.enableLights(true);
            notificationManager.createNotificationChannel(urgentChannel);

            // General notification channel
            NotificationChannel generalChannel = new NotificationChannel(
                    GENERAL_CHANNEL_ID,
                    GENERAL_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            generalChannel.setDescription("General notifications for volunteer activities");
            notificationManager.createNotificationChannel(generalChannel);
        }
    }

    /**
     * Send a local notification for urgent volunteer needs
     */
    public static void sendUrgentNotification(Context context, String title, String message) {
        sendNotification(context, title, message, URGENT_CHANNEL_ID, true);
    }

    /**
     * Send a general notification
     */
    public static void sendGeneralNotification(Context context, String title, String message) {
        sendNotification(context, title, message, GENERAL_CHANNEL_ID, false);
    }

    /**
     * Internal method to send notifications
     */
    private static void sendNotification(Context context, String title, String message,
                                         String channelId, boolean isUrgent) {
        Intent intent = new Intent(context, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int notificationId = NOTIFICATION_ID_COUNTER.incrementAndGet();

        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        int priority = isUrgent ? NotificationCompat.PRIORITY_HIGH : NotificationCompat.PRIORITY_DEFAULT;

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setAutoCancel(true)
                        .setPriority(priority)
                        .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(notificationId, notificationBuilder.build());
        }
    }

    /**
     * Subscribe to urgent notifications topic
     */
    public static void subscribeToUrgentNotifications() {
        FirebaseMessaging.getInstance().subscribeToTopic("urgent_needs")
                .addOnCompleteListener(task -> {
                    String msg = task.isSuccessful() ? "Subscribed to urgent notifications" : "Failed to subscribe";
                    Log.d(TAG, msg);
                });
    }

    /**
     * Unsubscribe from urgent notifications topic
     */
    public static void unsubscribeFromUrgentNotifications() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("urgent_needs")
                .addOnCompleteListener(task -> {
                    String msg = task.isSuccessful() ? "Unsubscribed from urgent notifications" : "Failed to unsubscribe";
                    Log.d(TAG, msg);
                });
    }

    /**
     * Get FCM token for this device
     */
    public static void getFCMToken(OnTokenReceivedListener listener) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String token = task.getResult();
                        Log.d(TAG, "FCM Token: " + token);
                        if (listener != null) {
                            listener.onTokenReceived(token);
                        }
                    } else {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        if (listener != null) {
                            listener.onTokenFailed(task.getException());
                        }
                    }
                });
    }

    public interface OnTokenReceivedListener {
        void onTokenReceived(String token);
        void onTokenFailed(Exception exception);
    }
}
