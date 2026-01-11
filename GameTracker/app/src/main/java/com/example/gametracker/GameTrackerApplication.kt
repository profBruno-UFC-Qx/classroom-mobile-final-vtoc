package com.example.gametracker

import android.app.Application
import com.example.gametracker.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GameTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GameTrackerApplication)
            modules(appModule)
        }
    }
}