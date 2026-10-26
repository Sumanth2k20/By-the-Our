package com.bytheour.utils


import android.content.Intent
import androidx.core.net.toUri

actual fun openPlayStore(appPackageName: String) {
    val context = AppContext.get()
    try {
        val intent = Intent(Intent.ACTION_VIEW, appPackageName.toUri())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}