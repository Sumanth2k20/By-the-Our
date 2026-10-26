package com.bytheour

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.ComposeUIViewController
import com.bytheour.di.JtKoinInitializer
import com.bytheour.utils.FirebaseAuthService
import com.bytheour.utils.FirebaseAuthServiceImpl
import com.bytheour.utils.ILocationAnimation
import com.bytheour.utils.ISearchAnimation
import com.bytheour.utils.NotificationHandler

val LocalLocationAnimViewFactory = staticCompositionLocalOf<ILocationAnimation> {
    error("No view factory provided.")
}

val LocalSearchAnimViewFactory = staticCompositionLocalOf<ISearchAnimation> {
    error("No view factory provided.")
}

var firebase:FirebaseAuthService  = FirebaseAuthServiceImpl()

var fcmTokenCallback: ((String) -> Unit)? = null

fun MainViewController(
    firebaseAuthService: FirebaseAuthServiceImpl,
    nativeLocationAnimation: ILocationAnimation,
    nativeSearchAnimation: ISearchAnimation,
    onFcmToken: (String) -> Unit
) = ComposeUIViewController(
    configure = {
        JtKoinInitializer().init()
        NotificationService().initLocalNotifications()
    }
) {
    firebase = firebaseAuthService
    fcmTokenCallback = { token ->
        println("FcmToken from iOS → Kotlin in IosMain ============================= $token")
        iosFcmToken = token   // store token globally
        onFcmToken(token)
    }

    println("firebaseAuthService============================= $firebaseAuthService")
    println("FcmToken from Ios to Kotlin Side in IosMain ============================= $fcmTokenCallback")

    if (NotificationHandler.wasLaunchedFromNotification) {
        notificationScreenTypeState.value = NotificationHandler.notificationType ?: ""
    }
    println("notificationScreenTypeState============================= ${notificationScreenTypeState.value}")
    CompositionLocalProvider(
        LocalLocationAnimViewFactory provides nativeLocationAnimation,
        LocalSearchAnimViewFactory provides nativeSearchAnimation
    ) {
        App(null, notificationScreenTypeState.value)
    }
}
