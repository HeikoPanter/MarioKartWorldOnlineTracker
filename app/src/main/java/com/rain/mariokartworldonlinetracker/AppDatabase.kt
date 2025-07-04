package com.rain.mariokartworldonlinetracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rain.mariokartworldonlinetracker.data.OnlineSession
import com.rain.mariokartworldonlinetracker.data.OnlineSessionDao
import com.rain.mariokartworldonlinetracker.data.RaceResult
import com.rain.mariokartworldonlinetracker.data.RaceResultDao

@Database(entities = [RaceResult::class, OnlineSession::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun raceResultDao(): RaceResultDao
    abstract fun onlineSessionDao(): OnlineSessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mkwot_db" // Name Ihrer Datenbankdatei
                )
                    // Hier können Migrationsstrategien hinzugefügt werden, falls sich das Schema ändert
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}