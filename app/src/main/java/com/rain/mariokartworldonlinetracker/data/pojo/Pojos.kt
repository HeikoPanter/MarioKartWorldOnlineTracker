package com.rain.mariokartworldonlinetracker.data.pojo

import com.rain.mariokartworldonlinetracker.EngineClass
import com.rain.mariokartworldonlinetracker.KnockoutCupName
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.TrackName

data class RaceCountByType(
    val raceCountTotal: Int,
    val raceCountThreelap: Int,
    val raceCountRoute: Int
)

data class MedianRaceCountPerSessionByType(
    val raceCountTotal: Int,
    val raceCountThreelap: Int,
    val raceCountRoute: Int
)

data class AveragePositionByType(
    val averagePositionTotal: Int,
    val averagePositionThreelap: Int,
    val averagePositionRoute: Int
)

data class MostPlayedRaceRoute(
    val drivingFromTrackName: TrackName?,
    val drivingToTrackName: TrackName?,
    val frequency: Int?
)

data class ResultHistory(
    val id: Long,
    val engineClass: EngineClass,
    val knockoutCupName: KnockoutCupName?,
    val drivingFromTrackName: TrackName?,
    val drivingToTrackName: TrackName?,
    val position: Short?,
    val creationDate: Long,
    val onlineSessionId: Long,
    val onlineSessionCreationDate: Long,
    val onlineSessionCategory: RaceCategory
)