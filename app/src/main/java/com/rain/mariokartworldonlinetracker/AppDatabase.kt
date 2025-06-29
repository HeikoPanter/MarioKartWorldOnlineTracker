package com.rain.mariokartworldonlinetracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RaceResult::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun raceResultDao(): RaceResultDao

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