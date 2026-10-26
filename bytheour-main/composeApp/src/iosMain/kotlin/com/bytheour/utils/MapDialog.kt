package com.bytheour.utils

import platform.Foundation.NSURL
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication

object MapDialogHelper {
    fun showMapChoiceDialog(googleUrl: NSURL, appleUrl: NSURL) {
        val alert = UIAlertController.alertControllerWithTitle(
            title = "Open Map",
            message = "Choose a map app to open",
            preferredStyle = UIAlertControllerStyleAlert
        )

        val googleAction = UIAlertAction.actionWithTitle(
            title = "Google Maps",
            style = UIAlertActionStyleDefault
        ) { _ ->
            UIApplication.sharedApplication.openURL(
                url = googleUrl,
                options = emptyMap<Any?, Any?>(),
                completionHandler = null
            )
        }

        val appleAction = UIAlertAction.actionWithTitle(
            title = "Apple Maps",
            style = UIAlertActionStyleDefault
        ) { _ ->
            UIApplication.sharedApplication.openURL(
                url = appleUrl,
                options = emptyMap<Any?, Any?>(),
                completionHandler = null
            )
        }

//        val webAction = UIAlertAction.actionWithTitle(
//            title = "Open in Browser",
//            style = UIAlertActionStyleDefault
//        ) { _ ->
//            UIApplication.sharedApplication.openURL(
//                url = webUrl,
//                options = emptyMap<Any?, Any?>(),
//                completionHandler = null
//            )
//        }

        val cancelAction = UIAlertAction.actionWithTitle(
            title = "Cancel",
            style = UIAlertActionStyleCancel
        ) { _ -> }

        alert.addAction(googleAction)
        alert.addAction(appleAction)
//        alert.addAction(webAction)
        alert.addAction(cancelAction)

        val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootVC?.presentViewController(alert, animated = true, completion = null)
    }
}
