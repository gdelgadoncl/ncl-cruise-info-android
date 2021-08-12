package com.ncl.nclcruiseinfo

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

val koinModules = listOf(
    networkModule,
    presentationModule
)

class NCLCruiseInfoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@NCLCruiseInfoApp)
            modules(koinModules)
        }
    }
}