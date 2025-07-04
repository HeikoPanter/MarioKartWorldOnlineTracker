package com.rain.mariokartworldonlinetracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.rain.mariokartworldonlinetracker.EngineClass
import com.rain.mariokartworldonlinetracker.KnockoutCupName
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.TrackName

@Entity(
    tableName = "race_results",
    foreignKeys = [
        ForeignKey(
            entity = OnlineSession::class,
            parentColumns = ["id"],
            childColumns = ["onlineSessionId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        )
    ]
    )
data class RaceResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // Primärschlüssel, automatisch generiert
    val engineClass: EngineClass,
    val knockoutCupName: KnockoutCupName?,
    val drivingFromTrackName: TrackName?,
    val drivingToTrackName: TrackName?,
    val position: Short?,
    val creationDate: Long, // Zeitstempel, wann das Rennen stattgefunden hat

    @ColumnInfo(index = true)
    val onlineSessionId: Long
)