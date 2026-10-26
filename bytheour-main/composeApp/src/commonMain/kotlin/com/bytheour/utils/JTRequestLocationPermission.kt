package com.bytheour.utils

import androidx.compose.runtime.Composable

@Composable
expect fun JtRequestLocationPermission(onPermissionResult: (Boolean) -> Unit)


@Composable
expect fun JtOpenAppSettings()
