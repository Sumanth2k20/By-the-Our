package com.bytheour

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect val platform:String

// commonMain
expect fun getDeviceToken(onTokenReceived: (String?) -> Unit)

expect val osName: String
expect val sdkVersionName: String
expect val device: String

