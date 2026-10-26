package com.bytheour.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class MapNavigator {
    actual fun openGoogleMaps(latitude: Double, longitude: Double, pinCode: String) {
        val application = UIApplication.sharedApplication

        val googleMapsUrl = NSURL.URLWithString("comgooglemaps://?q=$latitude,$longitude&center=$latitude,$longitude,$pinCode&zoom=15")
        val appleMapsUrl = NSURL.URLWithString("http://maps.apple.com/?q=$latitude,$longitude,$pinCode&z=15")
        val webUrl = NSURL.URLWithString("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude,$pinCode")
        val canOpenGoogle = googleMapsUrl != null && application.canOpenURL(googleMapsUrl)
        val canOpenApple = appleMapsUrl != null && application.canOpenURL(appleMapsUrl)

        when {
            canOpenGoogle && canOpenApple -> {
                MapDialogHelper.showMapChoiceDialog(googleMapsUrl, appleMapsUrl)
            }
            canOpenGoogle -> {
                application.openURL(
                    url = googleMapsUrl,
                    options = emptyMap<Any?, Any?>(),
                    completionHandler = null
                )
            }
            canOpenApple -> {
                application.openURL(
                    url = appleMapsUrl,
                    options = emptyMap<Any?, Any?>(),
                    completionHandler = null
                )
            }
            else -> {
                application.openURL(
                    url = webUrl!!,
                    options = emptyMap<Any?, Any?>(),
                    completionHandler = null
                )
            }
        }
    }
}

actual fun createMapNavigator(): MapNavigator = MapNavigator()