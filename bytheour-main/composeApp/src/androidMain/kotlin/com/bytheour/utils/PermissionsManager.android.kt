package com.bytheour.utils

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch

@Composable
actual fun createPermissionsManager(callback: PermissionCallback): PermissionsManager {
    return remember { PermissionsManager(callback) }
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PermissionsManager actual constructor(private val callback: PermissionCallback) :
    PermissionHandler {
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    override fun AskPermission(permission: PermissionType) {
        val lifecycleOwner = LocalLifecycleOwner.current

        when (permission) {
            PermissionType.CAMERA -> {
                val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
                LaunchedEffect(cameraPermissionState) { //Remembers the state of the permission and launches the effect once.
                    val permissionResult = cameraPermissionState.status
                    if (!permissionResult.isGranted) {// enter if it is false
                        if (permissionResult.shouldShowRationale) {// enter if it is true
                            callback.onPermissionStatus(
                                permission, PermissionStatus.SHOW_RATIONAL
                            )
                        } else {
                            lifecycleOwner.lifecycleScope.launch {
                                cameraPermissionState.launchPermissionRequest()// launching permission
                            }
                        }
                    } else {
                        callback.onPermissionStatus(
                            permission, PermissionStatus.GRANTED
                        )
                    }
                }
            }

            PermissionType.GALLERY -> {
                // Granted by default because in Android GetContent API does not require any runtime permissions
                callback.onPermissionStatus(
                    permission, PermissionStatus.GRANTED
                )
            }
            PermissionType.RECORD_AUDIO -> {
                val micPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
                LaunchedEffect(micPermissionState) {
                    val permissionResult = micPermissionState.status
                    if (!permissionResult.isGranted) {
                        if (permissionResult.shouldShowRationale) {
                            callback.onPermissionStatus(
                                permission, PermissionStatus.SHOW_RATIONAL
                            )
                        } else {
                            lifecycleOwner.lifecycleScope.launch {
                                micPermissionState.launchPermissionRequest()
                            }
                        }
                    } else {
                        callback.onPermissionStatus(
                            permission, PermissionStatus.GRANTED
                        )
                    }
                }
            }


//            PermissionType.CONTACTS -> {
//                val contactsPermissionState = rememberPermissionState(Manifest.permission.READ_CONTACTS)
//                LaunchedEffect(contactsPermissionState) {
//                    val permissionResult = contactsPermissionState.status
//                    if (!permissionResult.isGranted) {
//                        if (permissionResult.shouldShowRationale) {
//                            callback.onPermissionStatus(permission, PermissionStatus.SHOW_RATIONAL)
//                        } else {
//                            lifecycleOwner.lifecycleScope.launch {
//                                contactsPermissionState.launchPermissionRequest()
//                            }
//                        }
//                    } else {
//                        callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
//                    }
//                }
//            }

            PermissionType.NOTIFICATION -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
                    LaunchedEffect(notificationPermissionState) {
                        val permissionResult = notificationPermissionState.status
                        if (!permissionResult.isGranted) {
                            if (permissionResult.shouldShowRationale) {
                                callback.onPermissionStatus(permission, PermissionStatus.SHOW_RATIONAL)
                            } else {
                                lifecycleOwner.lifecycleScope.launch {
                                    notificationPermissionState.launchPermissionRequest()
                                }
                            }
                        } else {
                            callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
                        }
                    }
                } else {
                    callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
                }
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    override fun isPermissionGranted(permission: PermissionType): Boolean {
        return when (permission) {
            PermissionType.CAMERA -> {
                val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
                cameraPermissionState.status.isGranted
            }

            PermissionType.GALLERY -> {
                // Granted by default because in Android GetContent API does not require any
                // runtime permissions, and i am using it to access gallery in my app
                true
            }
            PermissionType.RECORD_AUDIO -> {
                val micPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
                micPermissionState.status.isGranted
            }


//            PermissionType.CONTACTS -> {
//                val contactsPermissionState = rememberPermissionState(Manifest.permission.READ_CONTACTS)
//                contactsPermissionState.status.isGranted
//            }

            PermissionType.NOTIFICATION -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
                    notificationPermissionState.status.isGranted
                } else {
                    true
                }
            }
        }
    }

    @Composable
    override fun LaunchSettings() {
        val context = LocalContext.current
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        ).also {
            context.startActivity(it)
        }
    }
}