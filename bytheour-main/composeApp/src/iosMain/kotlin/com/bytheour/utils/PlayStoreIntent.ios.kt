package com.bytheour.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

actual fun openPlayStore(appPackageName: String) {
    val nsUrl = NSURL.URLWithString(appPackageName)
    if (nsUrl != null) {
        dispatch_async(dispatch_get_main_queue()) {
            UIApplication.sharedApplication.openURL(
                url = nsUrl,
                options = emptyMap<Any?, Any?>(),
                completionHandler = null
            )
        }
    }
}