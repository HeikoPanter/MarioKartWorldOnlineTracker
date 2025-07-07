package com.rain.mariokartworldonlinetracker

import android.app.Application

class MarioKartWorldOnlineTrackerApplication : Application() {

    // Lazy Initialization der Datenbank
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        MkwotSettings.init(applicationContext)
    }
}