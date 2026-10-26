package com.bytheour

import android.app.Application
import com.bytheour.di.JtKoinInitializer
import com.google.firebase.FirebaseApp

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        initAppContext(this)
        JtKoinInitializer(applicationContext).init()
    }
}