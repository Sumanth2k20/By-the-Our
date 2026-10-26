package com.bytheour.utils

import androidx.compose.runtime.Composable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class PermissionsManager(callback: PermissionCallback) : PermissionHandler

interface PermissionCallback { //used to notify the app whether a permission is granted, denied, or rationale
    fun onPermissionStatus(permissionType: PermissionType, status: PermissionStatus)
}

@Composable
expect fun createPermissionsManager(callback: PermissionCallback): PermissionsManager

interface PermissionHandler {
    @Composable
    fun AskPermission(permission: PermissionType) //Requests permission

    @Composable
    fun isPermissionGranted(permission: PermissionType): Boolean //Checks if permission is granted

    @Composable
    fun LaunchSettings() //Opens device settings for manual permission granting.

}

