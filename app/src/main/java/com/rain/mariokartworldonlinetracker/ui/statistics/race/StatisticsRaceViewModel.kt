package com.rain.mariokartworldonlinetracker.ui.statistics.race

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.SortColumn
import com.rain.mariokartworldonlinetracker.SortDirection
import com.rain.mariokartworldonlinetracker.TrackAndKnockoutHelper
import com.rain.mariokartworldonlinetracker.TrackName
import com.rain.mariokartworldonlinetracker.data.MathUtils
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.data.pojo.AveragePositionByType
import com.rain.mariokartworldonlinetracker.data.pojo.MedianRaceCountPerSessionByType
import com.rain.mariokartworldonlinetracker.data.pojo.RaceCountByType
import com.rain.mariokartworldonlinetracker.data.pojo.RouteDetailedData
import com.rain.mariokartworldonlinetracker.data.pojo.ThreeLapTrackDetailedData
import com.rain.mariokartworldonlinetracker.ui.statistics.BaseStatisticsViewModel
import com.rain.mariokartworldonlinetracker.ui.statistics.TrackSortState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatisticsRaceViewModel(
    raceCategory: RaceCategory,
    raceResultRepository: RaceResultRepository,
    onlineSessionRepository: OnlineSessionRepository
) : BaseStatisticsViewModel<TrackName, ThreeLapTrackDetailedData>(
    raceCategory,
    raceResultRepository,
    onlineSessionRepository
) {
    val raceCountPOJO: Flow<RaceCountByType> = getRaceCountPOJOInternal()
    val medianRaceCountPerSessionPOJO: Flow<MedianRaceCountPerSessionByType> = getMedianRaceCountPerSessionPOJOInternal()
    val averagePositionPOJO: Flow<AveragePositionByType> = getAveragePositionPOJOInternal()

    private val _routeSortState = MutableStateFlow(TrackSortState())
    private val _routeOriginalDetailedData: Flow<List<RouteDetailedData>> = getRouteDetailedData()

    val routeDetailedData: StateFlow<List<RouteDetailedData>> =
        combine(
            _routeOriginalDetailedData,
            _showAllEntriesSetting
        ) { wwTracks, showAll ->
            val trackMap = wwTracks.associateBy { it.name.name + it.drivingToTrackName.name }

            if (showAll) {
                getRouteDetailedDataBaseList().map { track ->
                    val dbData = trackMap[track.name.name + track.drivingToTrackName.name]
                    if (dbData != null) {
                        dbData
                    }
                    else {
                        track
                    }
                }
            } else {
                wwTracks
            }
        }.combine(_routeSortState) { tracks, sortState ->
            sortRoutes(tracks, sortState)
        }.stateIn( // Konvertiert den Flow in einen StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Startet, wenn es Subscriber gibt, stoppt nach 5s ohne
            initialValue = emptyList() // Initialwert, bis der erste Wert vom Flow kommt
        )

    private fun getRaceCountPOJOInternal(): Flow<RaceCountByType> {
        val raceCountTotal: Flow<Int> = raceResultRepository.getCountTotal(raceCategory)
        val raceCountThreelap: Flow<Int> = raceResultRepository.getCountThreelap(raceCategory)
        val raceCountRoute: Flow<Int> = raceResultRepository.getCountRoute(raceCategory)

        return combine(
            raceCountTotal,
            raceCountThreelap,
            raceCountRoute
        ) { total, threelap, route ->
            RaceCountByType(
                raceCountTotal = total,
                raceCountThreelap = threelap,
                raceCountRoute = route)
        }
    }

    private fun getMedianRaceCountPerSessionPOJOInternal(): Flow<MedianRaceCountPerSessionByType> {
        val averageTotal: Flow<Int> = raceResultRepository.getCountPerSessionTotal(raceCategory).map { countsList -> MathUtils.calculateMedian(countsList) }
        val averageThreelap: Flow<Int> = raceResultRepository.getCountPerSessionThreelap(raceCategory).map { countsList -> MathUtils.calculateMedian(countsList) }
        val averageRoute: Flow<Int> = raceResultRepository.getCountPerSessionRoute(raceCategory).map { countsList -> MathUtils.calculateMedian(countsList) }

        return combine(
            averageTotal,
            averageThreelap,
            averageRoute
        ) { total, threelap, route ->
            MedianRaceCountPerSessionByType(
                raceCountTotal = total,
                raceCountThreelap = threelap,
                raceCountRoute = route)
        }
    }

    private fun getAveragePositionPOJOInternal(): Flow<AveragePositionByType> {
        val averageTotal: Flow<Int?> = raceResultRepository.getAveragePositionTotal(raceCategory)
        val averageThreelap: Flow<Int?> = raceResultRepository.getAveragePositionThreelap(raceCategory)
        val averageRoute: Flow<Int?> = raceResultRepository.getAveragePositionRoute(raceCategory)

        return combine(
            averageTotal,
            averageThreelap,
            averageRoute
        ) { total, threelap, route ->
            AveragePositionByType(
                averagePositionTotal = total ?: 0,
                averagePositionThreelap = threelap ?: 0,
                averagePositionRoute = route ?: 0)
        }
    }

    private fun sortRoutes(
        tracks: List<RouteDetailedData>,
        sortState: TrackSortState
    ): List<RouteDetailedData> {
        return when (sortState.column) {
            SortColumn.NAME -> {
                if (sortState.direction == SortDirection.ASCENDING) {
                    tracks.sortedBy { it.name }
                } else {
                    tracks.sortedByDescending { it.name }
                }
            }
            SortColumn.POSITION -> {
                if (sortState.direction == SortDirection.ASCENDING) {
                    tracks.sortedBy { it.averagePosition }
                } else {
                    tracks.sortedByDescending { it.averagePosition }
                }
            }
            SortColumn.AMOUNT -> {
                if (sortState.direction == SortDirection.ASCENDING) {
                    tracks.sortedBy { it.amountOfRaces }
                } else {
                    tracks.sortedByDescending { it.amountOfRaces }
                }
            }
        }
    }

    fun requestRouteSort(requestedColumn: SortColumn) {
        viewModelScope.launch { // In einer Coroutine, da _sortState ein StateFlow ist
            _routeSortState.update { currentState ->
                val newDirection = if (currentState.column == requestedColumn) {
                    if (currentState.direction == SortDirection.ASCENDING) SortDirection.DESCENDING else SortDirection.ASCENDING
                } else {
                    SortDirection.ASCENDING
                }
                TrackSortState(column = requestedColumn, direction = newDirection)
            }
        }
    }

    // Um den UI-Header im Fragment zu aktualisieren
    fun getRouteSortState(): TrackSortState {
        return _routeSortState.value
    }

    override fun getDetailedDataBaseList(): List<ThreeLapTrackDetailedData> {
        return TrackAndKnockoutHelper.getThreeLapTrackList()
    }

    override fun getDetailedData(): Flow<List<ThreeLapTrackDetailedData>> {
        return raceResultRepository.getThreeLapTrackDetailedDataList(raceCategory)
    }

    fun getRouteDetailedDataBaseList(): List<RouteDetailedData> {
        return TrackAndKnockoutHelper.getRouteList()
    }

    fun getRouteDetailedData() : Flow<List<RouteDetailedData>> {
        return raceResultRepository.getRouteDetailedDataList(raceCategory)
    }
}

class StatisticsRaceViewModelFactory(
    private val raceCategory: RaceCategory,
    private val raceResultRepository: RaceResultRepository,
    private val onlineSessionRepository: OnlineSessionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsRaceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatisticsRaceViewModel(raceCategory, raceResultRepository, onlineSessionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}