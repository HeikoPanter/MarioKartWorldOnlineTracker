package com.rain.mariokartworldonlinetracker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "race_results")
data class RaceResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // Primärschlüssel, automatisch generiert
    val category: RaceCategory,
    val engineClass: EngineClass,
    val knockoutCupName: KnockoutCupName?,
    val drivingFromTrackName: TrackName?,
    val drivingToTrackName: TrackName?,
    val position: Short?,
    val date: Long // Zeitstempel, wann das Rennen stattgefunden hat
)