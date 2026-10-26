package com.bytheour.utils

import android.app.Activity
import com.bytheour.MyFirebaseMessagingService
import org.koin.dsl.module


actual class PlatformActivityContext(val activity :Activity?)

fun androidPlatformModule() = module {
    single<FirebaseAuthService> { FirebaseAuthServiceImpl() }
    single<MyFirebaseMessagingService> { MyFirebaseMessagingService() }
}