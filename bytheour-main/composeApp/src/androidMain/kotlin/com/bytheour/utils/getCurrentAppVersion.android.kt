package com.bytheour.utils

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun getAppVersion(): String {
    val context = LocalContext.current
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    return packageInfo.versionName ?: "0.0"
}



actual val platform: String = "Android"



actual fun showToast(message: String) {
    Toast.makeText(AppContext.get(), message, Toast.LENGTH_SHORT).show()
}
