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
//import com.payrem.backend.api.jsonArrayToNotification
import com.payrem.backend.api.sendGet

class NotificationService(
    val context: Context
) {
    private val channelId = "ReBalance"
    private var notificationId = 0

    fun start() {
        createNotificationChannel()

        val mainLooper = Looper.getMainLooper()
        val preferences = Preferences(context).read()

//        Thread {
//            try {
//                while (true) {
//                    val notifications = jsonArrayToNotification(
//                        sendGet("http://${preferences.serverIp}/users/${
//                            preferences.userId}/notifications")
//                    )
//
//                    if (notifications.isNotEmpty()) {
//                        for (notification in notifications) {
//                            if (notification.getUserId().toString() == preferences.userId &&
//                                    notification.getAmount() < 0 &&
//                                    notification.getUserFromId().toString() != preferences.userId) {
//                                Handler(mainLooper).post {
//                                    if (notification.getExpenseId() != -1L) {
//                                        sendNotification("Added new expense")
//                                    }
//                                    if (notification.getGroupId() != -1L) {
//                                        sendNotification("Added to new group")
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    Thread.sleep(5_000)
//                }
//            } catch (_: Exception) { }
//        }.start()
    }

    private fun createNotificationChannel() {
        val name = "ReBalance"
        val descriptionText = "ReBalance main channel"
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

        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(androidx.core.R.drawable.notification_template_icon_bg)
            .setContentTitle("ReBalance")
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId++, builder.build())
        }
    }
}
