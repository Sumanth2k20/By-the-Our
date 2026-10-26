package com.bytheour.di

import android.content.Context
import com.bytheour.core.networkModule
import com.bytheour.utils.androidPlatformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

actual class JtKoinInitializer(private val context : Context) {
    actual fun init() {
        startKoin {
            androidContext(context)
            modules(networkModule,commonModule, androidPlatformModule())
        }
    }

}