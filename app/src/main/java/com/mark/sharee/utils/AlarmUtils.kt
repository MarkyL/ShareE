package com.mark.sharee.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.preference.PreferenceManager
import com.mark.sharee.core.Constants
import com.mark.sharee.fcm.NotificationBroadcastReceiver
import com.mark.sharee.fcm.NotificationsWorker
import com.mark.sharee.fragments.main.MainFragment
import org.json.JSONArray
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


object AlarmUtils {

    // 7Days * 24Hrs * 60Min * 60Sec * 1000Millis
    private const val WEEK_IN_MILLIS: Long = 7 * 24 * 60 * 60 * 1000
    private val sTagAlarms = ":alarms"

    fun addAlarm(context: Context, pendingIntent: PendingIntent, notificationId: Int, scheduledTime: Long) {
        Timber.i("addAlarm - with ID = $notificationId")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, scheduledTime, WEEK_IN_MILLIS, pendingIntent)
        saveAlarmId(context, notificationId)
    }

    fun cancelAlarm(context: Context, pendingIntent: PendingIntent, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()

        removeAlarmId(context, requestCode)
    }

    fun cancelAllAlarms(context: Context, pendingIntent: PendingIntent) {
        var alarmIds = getAlarmIds(context)

        for (idAlarm in alarmIds) {
            cancelAlarm(context, pendingIntent, idAlarm)
        }

        alarmIds = getAlarmIds(context)
    }

    fun hasAlarm(context: Context, intent: Intent, notificationId: Int): Boolean {
        return PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_NO_CREATE
        ) != null
    }

    private fun saveAlarmId(context: Context, id: Int) {
        val idsAlarms = getAlarmIds(context)

        if (idsAlarms.contains(id)) {
            return
        }

        idsAlarms.add(id)

        saveIdsInPreferences(context, idsAlarms)
    }

    private fun removeAlarmId(context: Context, id: Int) {
        val idsAlarms = getAlarmIds(context)

        for (i in idsAlarms.indices) {
            if (idsAlarms[i] == id) {
                idsAlarms.removeAt(i)
                break
            }
        }

        saveIdsInPreferences(context, idsAlarms)
    }

    private fun getAlarmIds(context: Context): MutableList<Int> {
        val ids = mutableListOf<Int>()
        try {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val jsonArray2 = JSONArray(prefs.getString(context.packageName + sTagAlarms, "[]"))

            for (i in 0 until jsonArray2.length()) {
                ids.add(jsonArray2.getInt(i))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
//        Timber.i("mark - getAlarmIds = [$ids]")
        return ids
    }

    private fun saveIdsInPreferences(context: Context, lstIds: List<Int>) {
        val jsonArray = JSONArray()
        for (idAlarm in lstIds) {
            jsonArray.put(idAlarm)
        }

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(context.packageName + sTagAlarms, jsonArray.toString())

        editor.apply()
    }

    //region specific pending intents
    fun getAlarmIntent(context: Context, title: String, message: String, requestCode: Int): PendingIntent {
        return Intent(context, NotificationBroadcastReceiver::class.java)
            .setAction(Constants.NOTIFICATION_INTENT_ACTION)
            .let { intent ->
                intent.putExtra(NotificationsWorker.NOTIFICATION_TITLE, title)
                intent.putExtra(NotificationsWorker.NOTIFICATION_MESSAGE, message)
                PendingIntent.getBroadcast(context, requestCode, intent, 0)
            }
    }

    fun cancelAllNotificationAlarms(context: Context) {
        val alarmIds = getAlarmIds(context)

        for (alarmId in alarmIds) {
            val pendingIntent = getAlarmIntent(context, "", "", alarmId)
            cancelAlarm(context, pendingIntent, alarmId)
        }
    }

    //region
}