package com.bytheour

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()
actual val platform: String = "IOS"


var iosFcmToken: String? = null



actual fun getDeviceToken(onTokenReceived: (String?) -> Unit) {
    onTokenReceived(iosFcmToken)
}

actual val osName: String
    get() = UIDevice.currentDevice.systemName()

actual val sdkVersionName: String
    get() = UIDevice.currentDevice.systemVersion

actual val device: String
    get() = UIDevice.currentDevice.model