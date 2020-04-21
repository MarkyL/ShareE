package com.mark.sharee.fcm

import com.example.sharee.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class ShareeFirebaseMessagingService : FirebaseMessagingService() {

    /* TODO:

    <meta-data
    android:name="firebase_messaging_auto_init_enabled"
    android:value="false" />
    <meta-data
    android:name="firebase_analytics_collection_enabled"
    android:value="false" />

    */

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.i("$TAG - From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Timber.i("$TAG - Message data payload: " + remoteMessage.data)
            sendNotification(remoteMessage.data)
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Timber.i("$TAG - Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private fun sendNotification(data: Map<String, String>) {
        val title = data[NOTIFICATION_TITLE]
        val body = data[NOTIFICATION_BODY]
        if (!title.isNullOrEmpty() && !body.isNullOrEmpty()) {
            sendNotification(title, body)
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Timber.i("$TAG - Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Timber.i("$TAG - sendRegistrationTokenToServer($token)")
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageTitle: String, messageBody: String) {
        NotificationUtil(applicationContext).sendNotification(
            messageTitle, messageBody, applicationContext.resources.getColor(
                R.color.leading_color, null)
        )
    }

    companion object {
        private const val TAG = "ShareeFirebaseMessagingService"
        private const val NOTIFICATION_TITLE = "title"
        private const val NOTIFICATION_BODY = "body"
    }



}