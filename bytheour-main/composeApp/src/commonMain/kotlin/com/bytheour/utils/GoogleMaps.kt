package com.bytheour.utils

import androidx.compose.runtime.Composable


@Composable
expect fun GoogleMaps(
    latitude: String,
    longitude: String,
    onLocationChange: (String, String) -> Unit,
    onMarkerClick: (String, String) -> Unit,
)