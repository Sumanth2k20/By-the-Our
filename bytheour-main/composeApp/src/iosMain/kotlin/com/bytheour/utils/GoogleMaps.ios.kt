package com.bytheour.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun GoogleMaps(
    latitude: String,
    longitude: String,
    onLocationChange: (String, String) -> Unit,
    onMarkerClick: (String, String) -> Unit
) {
    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            IosGoogleMapBridgeRegistry.bridge?.createMapView(
                latitude,
                longitude,
                onLocationChange,
                onMarkerClick
            ) as? UIView ?: UIView() // fallback empty view
        }
    )
}