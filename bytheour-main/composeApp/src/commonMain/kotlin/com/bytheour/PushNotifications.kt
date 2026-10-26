package com.bytheour

import androidx.compose.runtime.mutableStateOf


var notificationScreenTypeState = mutableStateOf<String?>(null)
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class NotificationService(){
    fun showNotification(
        title: String?,
        body: String?,
        imageUrl: String?,
        type:String?
    )
}