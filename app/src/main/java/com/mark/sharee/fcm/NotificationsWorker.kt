package com.mark.sharee.fcm

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.sharee.R
import timber.log.Timber

class NotificationsWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {

        // Get Notification Data
        val title = inputData.getString(NOTIFICATION_TITLE)
        val message = inputData.getString(NOTIFICATION_MESSAGE)

        // Show Notification
        // TODO: check if app is not in foreground and only then send, otherwise - do nothing.
        NotificationUtil(applicationContext).sendNotification(title!!, message!!,
            applicationContext.resources.getColor(R.color.leading_color, null))

        // TODO Do your other Background Processing

        // Return result

        return Result.success()
    }

    companion object {
        private const val TAG = "ScheduledWorker"
        const val NOTIFICATION_TITLE = "notification_title"
        const val NOTIFICATION_MESSAGE = "notification_message"
    }
}