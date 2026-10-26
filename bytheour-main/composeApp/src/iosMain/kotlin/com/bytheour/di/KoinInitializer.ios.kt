package com.bytheour.di

import com.bytheour.core.networkModule
import org.koin.core.context.startKoin

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class JtKoinInitializer {
    actual fun init() {
        startKoin {
            modules(networkModule,commonModule)
        }
    }

}