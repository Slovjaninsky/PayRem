package com.payrem

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.payrem.backend.entities.Reminder
//import com.payrem.backend.api.jsonArrayToNotification
import com.payrem.backend.service.BackendService
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class NotificationIdle(val context: Context, workerParams: WorkerParameters) : Worker(
    context,
    workerParams
) {
    private val channelId = "PayRem"
    private var notificationId = 0

    override fun doWork(): Result {
        for(i in 1..15) {
            try {
                createNotificationChannel()
                val preferences = Preferences(context).read()
                val mainLooper = Looper.getMainLooper()

                var remindersForUser =
                    BackendService(preferences).getAllRemindersOfUser(preferences.userId)

                for (reminder in remindersForUser) {
                    if (checkReminder(reminder)) {
                        Handler(mainLooper).post {
                            sendNotification(reminder.name)
                        }
                    }
                }

                val remindersForGroup = ArrayList<Reminder>()
                for (group in BackendService(preferences).getAllGroupOfUser(preferences.userId)) {
                    remindersForGroup.addAll(
                        BackendService(preferences).getAllRemindersOfGroup(
                            group.id
                        )
                    )
                }

                for (reminder in remindersForGroup) {
                    if (checkReminder(reminder)) {
                        Handler(mainLooper).post {
                            sendNotification(reminder.name)
                        }
                    }
                }
//                Result.success()
            } catch (ignored: Throwable) {
//                Result.failure()
            }
            Thread.sleep(60_000)
        }
        return Result.success()
    }

    private fun checkReminder(reminder: Reminder): Boolean {
        /* val reminderTime = LocalDateTime.parse(reminder.date +"T" + reminder.time + ":00")
        val current = LocalDateTime.now()

        return reminderTime.year == current.year && reminderTime.month == current.month
                && reminderTime.dayOfMonth == current.dayOfMonth && reminderTime.hour == current.hour
                && reminderTime.minute == current.minute */

        var reminderTime = LocalDateTime.parse(reminder.date +"T" + reminder.time)
        val current = LocalDateTime.now()
        val period = reminder.period

        if (period == 0L) {
            return reminderTime.year == current.year && reminderTime.month == current.month
                    && reminderTime.dayOfMonth == current.dayOfMonth && reminderTime.hour == current.hour
                    && reminderTime.minute == current.minute
        }
        else if (period > 0) {
            while (reminderTime <= current.truncatedTo(ChronoUnit.MINUTES)) {
                if (reminderTime.year == current.year && reminderTime.month == current.month
                    && reminderTime.dayOfMonth == current.dayOfMonth && reminderTime.hour == current.hour
                    && reminderTime.minute == current.minute) {
                    return true
                }
                reminderTime = reminderTime.plusDays(reminder.period)
            }
        }
        else {
            while (reminderTime <= current.truncatedTo(ChronoUnit.MINUTES)) {
                if (reminderTime.year == current.year && reminderTime.month == current.month
                    && reminderTime.dayOfMonth == current.dayOfMonth && reminderTime.hour == current.hour
                    && reminderTime.minute == current.minute) {
                    return true
                }
                reminderTime = reminderTime.plusMonths(-reminder.period)
            }
        }
        return false
    }

    private fun createNotificationChannel() {
        val name = "PayRem"
        val descriptionText = "PayRem main channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun sendNotification(
        textContent: String
    ) {
        val intent = Intent(context, LoadingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(androidx.core.R.drawable.notification_template_icon_bg)
            .setContentTitle("PayRem")
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId++, builder.build())
        }
    }

}
