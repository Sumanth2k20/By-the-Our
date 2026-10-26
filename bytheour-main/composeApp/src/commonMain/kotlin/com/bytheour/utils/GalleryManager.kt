package com.bytheour.utils

import androidx.compose.runtime.Composable

@Composable
expect fun rememberGalleryManager(
    allowMultiple: Boolean,
    onResult: (SharedImage?) -> Unit,
    onResultM: (List<SharedImage>) -> Unit
): GalleryManager


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class GalleryManager(
    onLaunch: () -> Unit
) {
    fun launch()
}