package com.bytheour.utils

import androidx.compose.runtime.Composable


@Composable
expect fun getAppVersion(): String



expect val platform:String

expect fun showToast(message: String)
