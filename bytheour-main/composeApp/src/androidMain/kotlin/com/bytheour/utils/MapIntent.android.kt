package com.bytheour.utils

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat

// androidMain

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class MapNavigator {
    private val context = AppContext.get()

    actual fun openGoogleMaps(latitude: Double, longitude: Double,pinCode:String) {
        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude,$pinCode")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // ✅ Important fix
        }

        if (mapIntent.resolveActivity(context.packageManager) != null) {
            ContextCompat.startActivity(context, mapIntent, null)
        } else {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/@$latitude,$longitude,15z")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // ✅ Also add here
            }
            ContextCompat.startActivity(context, browserIntent, null)
        }
    }
}

actual fun createMapNavigator(): MapNavigator = MapNavigator()
