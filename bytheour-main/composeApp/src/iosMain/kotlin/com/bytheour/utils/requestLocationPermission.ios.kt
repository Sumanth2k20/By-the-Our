package com.bytheour.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.darwin.NSObject

@Composable
actual fun JtOpenAppSettings() {
    val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
    if (url != null && UIApplication.sharedApplication.canOpenURL(url)) {
        UIApplication.sharedApplication.openURL(
            url,
            options = emptyMap<Any?, Any?>(),
            completionHandler = null
        )
    }
}


@Composable
actual fun JtRequestLocationPermission(onPermissionResult: (Boolean) -> Unit) {
    val locationManager = remember { CLLocationManager() }

    var shouldOpenSettings by remember { mutableStateOf(false) }

    val delegate = remember {
        object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
                when (manager.authorizationStatus) {
                    kCLAuthorizationStatusAuthorizedAlways,
                    kCLAuthorizationStatusAuthorizedWhenInUse -> {
                        onPermissionResult(true)
                    }
                    kCLAuthorizationStatusDenied -> {
                        shouldOpenSettings = true
                        onPermissionResult(false)
                    }
                    kCLAuthorizationStatusRestricted->{
                        onPermissionResult(false)
                    }
                    kCLAuthorizationStatusNotDetermined->{
                        onPermissionResult(false)
                    }
                    else -> {
                        onPermissionResult(false)
                    }
                }
            }
        }
    }

    SideEffect {
        locationManager.delegate = delegate
    }

    LaunchedEffect(Unit) {
        if (CLLocationManager.locationServicesEnabled()) {
            locationManager.requestWhenInUseAuthorization()
        } else {
            println("Location services are not enabled")
            onPermissionResult(false)
        }
    }

    if (shouldOpenSettings) {
        JtOpenAppSettings()
        shouldOpenSettings = false
    }
}


