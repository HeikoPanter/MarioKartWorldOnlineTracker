package com.rain.mariokartworldonlinetracker.data.pojo

import com.rain.mariokartworldonlinetracker.EngineClass
import com.rain.mariokartworldonlinetracker.KnockoutCupName
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.TrackName
import com.rain.mariokartworldonlinetracker.ui.statistics.DetailedData

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

data class ThreeLapTrackDetailedData(
    override val name: TrackName,
    override val amountOfRaces: Int,
    override val averagePosition: Int
) : DetailedData<TrackName>

data class RouteDetailedData(
    override val name: TrackName,
    val drivingToTrackName: TrackName,
    override val amountOfRaces: Int,
    override val averagePosition: Int
) : DetailedData<TrackName>

data class RallyDetailedData(
    override val name: KnockoutCupName,
    override val amountOfRaces: Int,
    override val averagePosition: Int
) : DetailedData<KnockoutCupName>

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
    val onlineSessionCategory: RaceCategory,
    val threeLapCountPerSession: Int,
    val routeCountPerSession: Int,
    val rallyCountPerSession: Int
)

sealed class HistoryListItem {
    data class SessionHeaderItem(
        val sessionId: Long,
        val sessionCreationDate: Long,
        val sessionCategory: RaceCategory,
        val threeLapCountPerSession: Int,
        val routeCountPerSession: Int,
        val rallyCountPerSession: Int

    ) : HistoryListItem() {
        // Eindeutige ID für DiffUtil, da sessionId und sessionCreationDate zusammen einzigartig sein sollten für einen Header
        val id: String = "header_${sessionId}_${sessionCreationDate}"
    }

    data class ResultHistoryItem(
        val resultHistory: ResultHistory
    ) : HistoryListItem() {
        // Die ID des ResultHistory-Objekts für DiffUtil
        val id: Long = resultHistory.id
    }
}