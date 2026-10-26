package com.bytheour

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.bytheour.data.response.GetEphemeraKeyResponse
import com.bytheour.utils.AppContext
import com.bytheour.utils.PlatformActivityContext
import com.bytheour.utils.StripeBillingFragment
import com.bytheour.utils.StripeUiBridge
import com.bytheour.utils.StripeUiBridgeRegistry
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity(), StripeUiBridge {
    private var type: MutableState<String?> = mutableStateOf(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppContext.init(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        FirebaseApp.initializeApp(this)
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
        type.value = intent?.getStringExtra("type")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    println("FCM Token on launch:------------------ $token")
                } else {
                    println("Fetching FCM token failed: ${task.exception}")
                }
            }

        StripeUiBridgeRegistry.register(this)


        setContent {
            Box(modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
            ) {
                val context = LocalContext.current
                val activity = context.findActivity()
                App(
                    mainActivity =  PlatformActivityContext(activity),
                    type = type.value,
                )
            }
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        type.value = intent.getStringExtra("type")
    }


    override fun showStripeBillingScreen(ephemeralKey: GetEphemeraKeyResponse.Data) {
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, StripeBillingFragment(ephemeralKey))
            .addToBackStack(null)
            .commit()
    }
}


fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

actual fun getDeviceToken(onTokenReceived: (String?) -> Unit) {
    FirebaseMessaging.getInstance().token
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                println("Android FCM Token: $token")
                onTokenReceived(token)
            } else {
                println("Fetching Android FCM token failed: ${task.exception}")
                onTokenReceived(null)
            }
        }
}
