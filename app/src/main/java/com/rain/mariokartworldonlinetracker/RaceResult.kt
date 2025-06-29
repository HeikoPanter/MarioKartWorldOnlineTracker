package com.rain.mariokartworldonlinetracker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "race_results")
data class RaceResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // Primärschlüssel, automatisch generiert
    val category: RaceCategory,
    val mode: RaceMode,
    val knockoutCupName: String?,
    val drivingFromTrackName: String?,
    val drivingToTrackName: String?,
    val position: Short?,
    val date: Long // Zeitstempel, wann das Rennen stattgefunden hat
)