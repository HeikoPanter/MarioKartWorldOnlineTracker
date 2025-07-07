package com.rain.mariokartworldonlinetracker.data.pojo

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
    val drivingFromTrackName: TrackName,
    val drivingToTrackName: TrackName,
    val frequency: Int
)