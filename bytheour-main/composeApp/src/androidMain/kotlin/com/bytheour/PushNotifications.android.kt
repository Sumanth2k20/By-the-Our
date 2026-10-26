package com.bytheour

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

lateinit var appContext: Context

fun initAppContext(context: Context) {
    appContext = context.applicationContext
}
// Platform-specific (Android) implementation of NotificationService
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class NotificationService {


    actual fun showNotification(
        title: String?,
        body: String?,
        imageUrl: String?,
        type: String?
    ) {
        val notificationId = System.currentTimeMillis().toInt()

        val notificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        initNotificationChannel(notificationManager)

        val intent = Intent(appContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("type", type)
        }

        val pendingIntent = PendingIntent.getActivity(
            appContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_round)
            .setContentTitle(title ?: appContext.getString(R.string.app_name))
            .setContentText(body ?: "")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)


        if (!imageUrl.isNullOrBlank()) {
            try {
                val url = java.net.URL(imageUrl)
                val connection = url.openConnection()
                connection.connect()
                val input = connection.getInputStream()
                val bitmap = BitmapFactory.decodeStream(input)
                builder.setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                )
            } catch (e: Exception) {
                builder.setStyle(NotificationCompat.BigTextStyle().bigText(body))
            }
        } else {
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(body))
        }

        notificationManager.notify(notificationId, builder.build())
    }

    private fun initNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for default notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }


    companion object {
        private const val CHANNEL_ID = "default_channel"
        private const val CHANNEL_NAME = "General Notifications"
    }
}


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            val data = remoteMessage.data
             val title = data["title"] ?: getString(R.string.app_name)
                val body = data["body"] ?: ""
                val imageUrl = data["image_url"] ?: data["image"]
                val type = data["type"]

            NotificationService().showNotification(
                title = title,
                body = body,
                imageUrl = imageUrl,
                type = type
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("FCM Token: ============================= $token")
    }
}
