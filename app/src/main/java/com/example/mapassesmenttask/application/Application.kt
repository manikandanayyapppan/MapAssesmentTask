package com.example.mapassesmenttask.application

import android.app.Application
import com.example.mapassesmenttask.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)
            modules(appModules)
        }
    }
}

