package com.example.graphqlandcomposepoc

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Experts: Daniel_Zombori
 * Created: 28/02/2022
 */
class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }

    private fun setupKoin() {
        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            modules(Koin.module)
        }
    }
}