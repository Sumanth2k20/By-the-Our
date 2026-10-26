package com.bytheour.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent

class AndroidShareService() : ShareService {
    override fun share(title: String, url: String,code: String?) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, "$title\n$url")
            putExtra(Intent.EXTRA_TEXT, "$title\n$code")
        }
        AppContext.get().startActivity(Intent.createChooser(shareIntent, "Share via").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ShareServiceProvider {
    actual fun getShareService(): ShareService = AndroidShareService()
}




@SuppressLint("StaticFieldLeak")
object AppContext {
    private lateinit var context: Context

    fun init(appContext: Context) {
        context = appContext.applicationContext
    }

    fun get(): Context = context
}
