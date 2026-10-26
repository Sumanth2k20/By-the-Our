package com.bytheour

import platform.Foundation.NSUUID
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptionBadge
import platform.UserNotifications.UNNotificationPresentationOptionSound
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationResponse
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class NotificationService {

    actual fun showNotification(
        title: String?,
        body: String?,
        imageUrl: String?,
        type: String?
    ) {
        val content = UNMutableNotificationContent().apply {
            setTitle(title ?: "Notification")
            setBody(body ?: "")
            setSound(UNNotificationSound.defaultSound())

            setUserInfo(mapOf(
                "type" to (type ?: ""),
                "imageUrl" to (imageUrl ?: "")
            ))
        }

        val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(0.1, false)
        val request = UNNotificationRequest.requestWithIdentifier(
            NSUUID().UUIDString(),
            content,
            trigger
        )
        println("✅ =========================Notification notification center")
        UNUserNotificationCenter.currentNotificationCenter()

            .addNotificationRequest(request) { error ->
                error?.let {
                    println("❌ ===================== Notification error: ${it.localizedDescription}")
                } ?: println("✅ =========================Notification shown")
            }
    }

    fun initLocalNotifications() {
        val center = UNUserNotificationCenter.currentNotificationCenter()

        center.requestAuthorizationWithOptions(
            UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
        ) { granted, error ->
            println("🔔 Notification Permission: ================ $granted")
        }

        center.delegate = object : NSObject(), UNUserNotificationCenterDelegateProtocol {
            override fun userNotificationCenter(
                center: UNUserNotificationCenter,
                willPresentNotification: UNNotification,
                withCompletionHandler: (UNNotificationPresentationOptions) -> Unit
            ) {
                withCompletionHandler(
                    UNNotificationPresentationOptionAlert or
                            UNNotificationPresentationOptionSound or
                            UNNotificationPresentationOptionBadge
                )
            }

            override fun userNotificationCenter(
                center: UNUserNotificationCenter,
                didReceiveNotificationResponse: UNNotificationResponse,
                withCompletionHandler: () -> Unit
            ) {
                val userInfo = didReceiveNotificationResponse.notification.request.content.userInfo
                println("notification received =====================================: $userInfo")
                val type = userInfo["type"] as? String
                notificationScreenTypeState.value = type
                withCompletionHandler()
            }
        }
    }
}


