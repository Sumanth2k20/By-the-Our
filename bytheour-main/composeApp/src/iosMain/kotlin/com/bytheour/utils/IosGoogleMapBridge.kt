package com.bytheour.utils

import kotlin.concurrent.Volatile

interface IosGoogleMapBridge {
    fun createMapView(
        latitude: String,
        longitude: String,
        onLocationChange: (String, String) -> Unit,
        onMarkerClick: (String, String) -> Unit
    ): Any // UIView on iOS
}

object IosGoogleMapBridgeRegistry {
    @Volatile
    var bridge: IosGoogleMapBridge? = null

    fun register(impl: IosGoogleMapBridge) {
        bridge = impl
    }
}