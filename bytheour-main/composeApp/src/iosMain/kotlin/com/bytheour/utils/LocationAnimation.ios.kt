package com.bytheour.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitViewController
import com.bytheour.LocalLocationAnimViewFactory
import com.bytheour.LocalSearchAnimViewFactory
import com.bytheour.presentations.screens.location.JTLocationFetchingViewModel


@Composable
actual fun LocationAnimation(uiState: JTLocationFetchingViewModel.LocationUiSate) {
    val locationAnim = LocalLocationAnimViewFactory.current
    UIKitViewController(
        factory = {
            locationAnim.createLocationAnimationView()
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
actual fun SearchAnimation(modifier: Modifier) {
    val locationAnim = LocalSearchAnimViewFactory.current
    UIKitViewController(
        factory = {
            locationAnim.createSearchAnimationView()
        },
        modifier = Modifier.height(55.dp)
    )
}