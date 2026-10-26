package com.bytheour

import android.os.Build
import com.google.firebase.BuildConfig

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
actual val platform: String = "ANDROID"

val fields = Build.VERSION_CODES::class.java.fields

actual val osName: String
    get() = fields[Build.VERSION.SDK_INT].name

actual val sdkVersionName: String
    get() = BuildConfig.VERSION_NAME

actual val device: String
    get() = Build.MODEL