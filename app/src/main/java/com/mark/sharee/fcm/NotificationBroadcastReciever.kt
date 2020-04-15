package com.mark.sharee.fcm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.mark.sharee.fcm.NotificationsWorker.Companion.NOTIFICATION_MESSAGE
import com.mark.sharee.fcm.NotificationsWorker.Companion.NOTIFICATION_TITLE
import timber.log.Timber

class NotificationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.i("NotificationBroadcastReceiver - onReceive")
        intent?.let {
            val title = it.getStringExtra(NOTIFICATION_TITLE)
            val message = it.getStringExtra(NOTIFICATION_MESSAGE)

            // Create Notification Data
            val notificationData = Data.Builder()
                .putString(NOTIFICATION_TITLE, title)
                .putString(NOTIFICATION_MESSAGE, message)
                .build()

            // Init Worker
            val work = OneTimeWorkRequest.Builder(NotificationsWorker::class.java)
                .setInputData(notificationData)
                .build()

            // Start Worker
            WorkManager.getInstance(context!!).beginWith(work).enqueue()

            Timber.i("NotificationBroadcastReceiver - WorkManager is Enqueued.")
        }
    }
}