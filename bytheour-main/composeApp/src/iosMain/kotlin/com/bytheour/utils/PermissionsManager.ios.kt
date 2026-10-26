package com.bytheour.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import platform.AVFoundation.AVAuthorizationStatus
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeAudio
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Contacts.CNAuthorizationStatus
import platform.Contacts.CNAuthorizationStatusAuthorized
import platform.Contacts.CNAuthorizationStatusDenied
import platform.Contacts.CNAuthorizationStatusNotDetermined
import platform.Contacts.CNContactStore
import platform.Contacts.CNEntityType
import platform.Foundation.NSURL
import platform.Photos.PHAuthorizationStatus
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusDenied
import platform.UserNotifications.UNAuthorizationStatusNotDetermined
import platform.UserNotifications.UNUserNotificationCenter

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PermissionsManager actual constructor(private val callback: PermissionCallback) : PermissionHandler {
    @Composable
    override fun AskPermission(permission: PermissionType) {
        when (permission) {
            PermissionType.CAMERA -> {
                val status: AVAuthorizationStatus =
                    remember { AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo) }
                askCameraPermission(status, permission, callback)
            }
            PermissionType.GALLERY -> {
                val status: PHAuthorizationStatus =
                    remember { PHPhotoLibrary.authorizationStatus() }
                askGalleryPermission(status, permission, callback)
            }
            PermissionType.RECORD_AUDIO -> {
                val status: AVAuthorizationStatus =
                    remember { AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeAudio) }
                askAudioPermission(status, permission, callback)
            }
//            PermissionType.CONTACTS -> {
//                val status: CNAuthorizationStatus =
//                    remember { CNContactStore.authorizationStatusForEntityType(CNEntityType.CNEntityTypeContacts) }
//                askContactsPermission(status, permission, callback)
//            }
            PermissionType.NOTIFICATION -> {
                askNotificationPermission(permission, callback)
            }
        }
    }

    private fun askCameraPermission(
        status: AVAuthorizationStatus, permission: PermissionType, callback: PermissionCallback
    ) {
        when (status) {
            AVAuthorizationStatusAuthorized -> {
                callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
            }
            AVAuthorizationStatusNotDetermined -> {
                AVCaptureDevice.Companion.requestAccessForMediaType(AVMediaTypeVideo) { isGranted ->
                    if (isGranted) {
                        callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
                    } else {
                        callback.onPermissionStatus(permission, PermissionStatus.DENIED)
                    }
                }
            }
            AVAuthorizationStatusDenied -> {
                callback.onPermissionStatus(permission, PermissionStatus.DENIED)
            }
            else -> error("unknown camera status $status")
        }
    }

    private fun askGalleryPermission(
        status: PHAuthorizationStatus, permission: PermissionType, callback: PermissionCallback
    ) {
        when (status) {
            PHAuthorizationStatusAuthorized -> {
                callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
            }
            PHAuthorizationStatusNotDetermined -> {
                PHPhotoLibrary.Companion.requestAuthorization { newStatus ->
                    askGalleryPermission(newStatus, permission, callback)
                }
            }
            PHAuthorizationStatusDenied -> {
                callback.onPermissionStatus(permission, PermissionStatus.DENIED)
            }
            else -> error("unknown gallery status $status")
        }
    }

    private fun askAudioPermission(
        status: AVAuthorizationStatus, permission: PermissionType, callback: PermissionCallback
    ) {
        when (status) {
            AVAuthorizationStatusAuthorized -> {
                callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
            }
            AVAuthorizationStatusNotDetermined -> {
                AVCaptureDevice.Companion.requestAccessForMediaType(AVMediaTypeAudio) { isGranted ->
                    if (isGranted) {
                        callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
                    } else {
                        callback.onPermissionStatus(permission, PermissionStatus.DENIED)
                    }
                }
            }
            AVAuthorizationStatusDenied -> {
                callback.onPermissionStatus(permission, PermissionStatus.DENIED)
            }
            else -> error("unknown audio status $status")
        }
    }

//    private fun askContactsPermission(
//        status: CNAuthorizationStatus, permission: PermissionType, callback: PermissionCallback
//    ) {
//        when (status) {
//            CNAuthorizationStatusAuthorized -> {
//                callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
//            }
//            CNAuthorizationStatusNotDetermined -> {
//                CNContactStore().requestAccessForEntityType(CNEntityType.CNEntityTypeContacts) { granted, error ->
//                    if (granted) {
//                        callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
//                    } else {
//                        callback.onPermissionStatus(permission, PermissionStatus.DENIED)
//                    }
//                }
//            }
//            CNAuthorizationStatusDenied -> {
//                callback.onPermissionStatus(permission, PermissionStatus.DENIED)
//            }
//            else -> error("unknown contacts status $status")
//        }
//    }

    private fun askNotificationPermission(
        permission: PermissionType, callback: PermissionCallback
    ) {
        val center = UNUserNotificationCenter.currentNotificationCenter()

        center.getNotificationSettingsWithCompletionHandler { settings ->
            when (settings?.authorizationStatus) {
                UNAuthorizationStatusAuthorized -> {
                    callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
                }
                UNAuthorizationStatusNotDetermined -> {
                    val options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
                    center.requestAuthorizationWithOptions(options) { granted, error ->
                        if (granted) {
                            callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
                        } else {
                            callback.onPermissionStatus(permission, PermissionStatus.DENIED)
                        }
                    }
                }
                UNAuthorizationStatusDenied -> {
                    callback.onPermissionStatus(permission, PermissionStatus.DENIED)
                }
                else -> {
                    callback.onPermissionStatus(permission, PermissionStatus.DENIED)
                }
            }
        }
    }

    @Composable
    override fun isPermissionGranted(permission: PermissionType): Boolean {
        return when (permission) {
            PermissionType.CAMERA -> {
                val status: AVAuthorizationStatus =
                    remember { AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo) }
                status == AVAuthorizationStatusAuthorized
            }
            PermissionType.GALLERY -> {
                val status: PHAuthorizationStatus =
                    remember { PHPhotoLibrary.authorizationStatus() }
                status == PHAuthorizationStatusAuthorized
            }
            PermissionType.RECORD_AUDIO -> {
                val status: AVAuthorizationStatus =
                    remember { AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeAudio) }
                status == AVAuthorizationStatusAuthorized
            }
//            PermissionType.CONTACTS -> {
//                val status: CNAuthorizationStatus =
//                    remember { CNContactStore.authorizationStatusForEntityType(CNEntityType.CNEntityTypeContacts) }
//                status == CNAuthorizationStatusAuthorized
//            }
            PermissionType.NOTIFICATION -> {
                val isGranted by produceState(initialValue = false) {
                    val center = UNUserNotificationCenter.currentNotificationCenter()
                    center.getNotificationSettingsWithCompletionHandler { settings ->
                        value = settings?.authorizationStatus == UNAuthorizationStatusAuthorized
                    }
                }
                isGranted
            }

        }
    }

    @Composable
    override fun LaunchSettings() {
        val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
        if (url != null && UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(
                url,
                options = emptyMap<Any?, Any?>(),
                completionHandler = null
            )
        }
    }
}

@Composable
actual fun createPermissionsManager(callback: PermissionCallback): PermissionsManager {
    return PermissionsManager(callback)
}