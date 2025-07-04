package com.rain.mariokartworldonlinetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rain.mariokartworldonlinetracker.RaceCategory

@Entity(tableName = "online_sessions")
data class OnlineSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // Primärschlüssel, automatisch generiert,
    val creationDate: Long, // Zeitstempel, wann die Session stattgefunden hat
    val category: RaceCategory,
)