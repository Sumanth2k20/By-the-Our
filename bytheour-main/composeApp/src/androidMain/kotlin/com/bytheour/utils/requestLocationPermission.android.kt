package com.bytheour.utils

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun JtRequestLocationPermission(onPermissionResult: (Boolean) -> Unit) {

    var permissionDeniedCount by remember { mutableIntStateOf(0) }
    var shouldRequestPermission by remember { mutableStateOf(true) }
    var triggerOpenSettings by remember { mutableStateOf(false) }
    val permission = Manifest.permission.ACCESS_FINE_LOCATION

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionResult(true)
        } else {
            permissionDeniedCount++
            if (permissionDeniedCount < 3) {
                shouldRequestPermission = true
            } else {
                triggerOpenSettings = true
            }
            onPermissionResult(false)
        }
    }

    if (shouldRequestPermission) {
        LaunchedEffect(permissionDeniedCount) {
            shouldRequestPermission = false
            locationPermissionLauncher.launch(permission)
        }
    }

    if (triggerOpenSettings) {
        JtOpenAppSettings()
    }
}




@Composable
actual fun JtOpenAppSettings() {
    val context = LocalContext.current
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}
