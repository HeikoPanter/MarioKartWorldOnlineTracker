package com.rain.mariokartworldonlinetracker

import android.app.Application

class MarioKartWorldOnlineTrackerApplication : Application() {

    // Lazy Initialization der Datenbank
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()

        val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(
            GlobalExceptionHandler(
                applicationContext,
                defaultExceptionHandler,
                MainActivity::class.java // Ersetze MainActivity::class.java durch CrashActivity::class.java wenn du diese verwendest
            )
        )

        MkwotSettings.init(applicationContext)
    }
}