package com.rain.mariokartworldonlinetracker.ui.statistics.race.versus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rain.mariokartworldonlinetracker.MkwotSettings
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.SortColumn
import com.rain.mariokartworldonlinetracker.SortDirection
import com.rain.mariokartworldonlinetracker.TrackAndKnockoutHelper
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.data.pojo.AveragePositionByType
import com.rain.mariokartworldonlinetracker.data.pojo.MedianRaceCountPerSessionByType
import com.rain.mariokartworldonlinetracker.data.pojo.RaceCountByType
import com.rain.mariokartworldonlinetracker.data.pojo.ResultHistory
import com.rain.mariokartworldonlinetracker.data.pojo.RouteDetailedData
import com.rain.mariokartworldonlinetracker.data.pojo.ThreeLapTrackDetailedData
import com.rain.mariokartworldonlinetracker.ui.statistics.TrackSortState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatisticsRaceVersusViewModel(
    private val raceResultRepository: RaceResultRepository,
    private val onlineSessionRepository: OnlineSessionRepository
) : ViewModel() {

    private val _showAllTracksSetting: StateFlow<Boolean> = MkwotSettings.showAllEntries

    private val _raceVersusHistorySortState = MutableStateFlow(TrackSortState())
    private val originalResultHistory: Flow<List<ResultHistory>> = raceResultRepository.getResultHistory(RaceCategory.RACE_VS)

    val resultHistory: StateFlow<List<ResultHistory>> =
        combine(originalResultHistory, _raceVersusHistorySortState) { tracks, sortState ->
            sortHistory(tracks, sortState)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()

        )

    //<editor-fold desc="Race VS values">

    private val _raceVersusSortState = MutableStateFlow(TrackSortState())
    private val originalVersusThreeLapTrackDetailedData: Flow<List<ThreeLapTrackDetailedData>> = raceResultRepository.getVsThreeLapTrackDetailedDataList()

    val versusThreeLapTrackDetailedData: StateFlow<List<ThreeLapTrackDetailedData>> =
        combine(
            originalVersusThreeLapTrackDetailedData,
            _showAllTracksSetting
        ) { wwTracks, showAll ->
            val trackMap = wwTracks.associateBy { it.drivingToTrackName }

            if (showAll) {
                TrackAndKnockoutHelper.getThreeLapTrackList().map { track ->
                    val dbData = trackMap[track.drivingToTrackName]
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
        }.combine(_raceVersusSortState) { tracks, sortState ->
            sortTracks(tracks, sortState)
        }.stateIn( // Konvertiert den Flow in einen StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Startet, wenn es Subscriber gibt, stoppt nach 5s ohne
            initialValue = emptyList() // Initialwert, bis der erste Wert vom Flow kommt
        )

    private val _raceVersusRouteSortState = MutableStateFlow(TrackSortState())
    private val originalVersusRouteDetailedData: Flow<List<RouteDetailedData>> = raceResultRepository.getVsRouteDetailedDataList()

    val versusRouteDetailedData: StateFlow<List<RouteDetailedData>> =
        combine(
            originalVersusRouteDetailedData,
            _showAllTracksSetting
        ) { wwTracks, showAll ->
            val trackMap = wwTracks.associateBy { it.drivingFromTrackName.name + it.drivingToTrackName.name }

            if (showAll) {
                TrackAndKnockoutHelper.getRouteList().map { track ->
                    val dbData = trackMap[track.drivingFromTrackName.name + track.drivingToTrackName.name]
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
        }.combine(_raceVersusRouteSortState) { tracks, sortState ->
            sortRoutes(tracks, sortState)
        }.stateIn( // Konvertiert den Flow in einen StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Startet, wenn es Subscriber gibt, stoppt nach 5s ohne
            initialValue = emptyList() // Initialwert, bis der erste Wert vom Flow kommt
        )

    val raceVsCountPOJO: Flow<RaceCountByType> = raceResultRepository.getRaceVsCountPOJO()
    val medianRaceVsCountPerSessionPOJO: Flow<MedianRaceCountPerSessionByType> = raceResultRepository.getMedianRaceVsCountPerSessionPOJO()
    val averageRaceVsPositionPOJO: Flow<AveragePositionByType> = raceResultRepository.getAverageRaceVsPositionPOJO()
    val raceVsSessionCount: Flow<Int> = onlineSessionRepository.raceVsSessionCount

    // Methode, die vom Fragment aufgerufen wird, um die Sortierung zu ändern
    fun requestRaceVersusSort(requestedColumn: SortColumn) {
        viewModelScope.launch { // In einer Coroutine, da _sortState ein StateFlow ist
            _raceVersusSortState.update { currentState ->
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
    fun getRaceVersusSortState(): TrackSortState {
        return _raceVersusSortState.value
    }

    fun requestRaceVersusRouteSort(requestedColumn: SortColumn) {
        viewModelScope.launch { // In einer Coroutine, da _sortState ein StateFlow ist
            _raceVersusRouteSortState.update { currentState ->
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
    fun getRaceVersusRouteSortState(): TrackSortState {
        return _raceVersusRouteSortState.value
    }
//</editor-fold>

    fun getHistorySortState(): TrackSortState {
        return _raceVersusHistorySortState.value
    }

    fun requestHistorySort(requestedColumn: SortColumn) {
        viewModelScope.launch { // In einer Coroutine, da _sortState ein StateFlow ist
            _raceVersusHistorySortState.update { currentState ->
                val newDirection = if (currentState.column == requestedColumn) {
                    if (currentState.direction == SortDirection.ASCENDING) SortDirection.DESCENDING else SortDirection.ASCENDING
                } else {
                    SortDirection.ASCENDING
                }
                TrackSortState(column = requestedColumn, direction = newDirection)
            }
        }
    }

    private fun sortHistory(
        tracks: List<ResultHistory>,
        sortState: TrackSortState
    ): List<ResultHistory> {
        if (sortState.column == SortColumn.AMOUNT) {
            if (sortState.direction == SortDirection.ASCENDING) {
                return tracks.sortedBy { it.creationDate }
            } else {
                return tracks.sortedByDescending { it.creationDate }
            }
        }
        else {
            return tracks
        }
    }

    private fun sortTracks(
        tracks: List<ThreeLapTrackDetailedData>,
        sortState: TrackSortState
    ): List<ThreeLapTrackDetailedData> {
        return when (sortState.column) {
            SortColumn.NAME -> {
                if (sortState.direction == SortDirection.ASCENDING) {
                    tracks.sortedBy { it.drivingToTrackName }
                } else {
                    tracks.sortedByDescending { it.drivingToTrackName }
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

    private fun sortRoutes(
        tracks: List<RouteDetailedData>,
        sortState: TrackSortState
    ): List<RouteDetailedData> {
        return when (sortState.column) {
            SortColumn.NAME -> {
                if (sortState.direction == SortDirection.ASCENDING) {
                    tracks.sortedBy { it.drivingFromTrackName }
                } else {
                    tracks.sortedByDescending { it.drivingFromTrackName }
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
}

class StatisticsRaceVersusViewModelFactory(private val raceResultRepository: RaceResultRepository, private val onlineSessionRepository: OnlineSessionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsRaceVersusViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatisticsRaceVersusViewModel(raceResultRepository, onlineSessionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}