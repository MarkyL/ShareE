package com.mark.sharee.fcm

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber

class DailyRoutineWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {

        Timber.i("$TAG - Work START")
        Timber.i("mark - Work START")

        // Get Notification Data
        val title = inputData.getString(NOTIFICATION_TITLE)
        val message = inputData.getString(NOTIFICATION_MESSAGE)

        // Show Notification
        NotificationUtil(applicationContext).sendNotification(title!!, message!!)

        // TODO Do your other Background Processing

        Timber.i( "$TAG - Work DONE")
        Timber.i( "mark - Work DONE")
        // Return result

        return Result.success()
    }

    companion object {
        private const val TAG = "ScheduledWorker"
        const val NOTIFICATION_TITLE = "notification_title"
        const val NOTIFICATION_MESSAGE = "notification_message"
    }
}