package com.rain.mariokartworldonlinetracker.ui.statistics.race.worldwide

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
import com.rain.mariokartworldonlinetracker.data.pojo.HistoryListItem
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

class StatisticsRaceWorldwideViewModel(
    private val raceResultRepository: RaceResultRepository,
    private val onlineSessionRepository: OnlineSessionRepository
) : ViewModel() {

    private val _showAllTracksSetting: StateFlow<Boolean> = MkwotSettings.showAllEntries

    private val _raceWorldwideHistorySortState = MutableStateFlow(TrackSortState(column = SortColumn.AMOUNT, direction = SortDirection.DESCENDING))
    private val originalResultHistory: Flow<List<ResultHistory>> = raceResultRepository.getResultHistory(RaceCategory.RACE)

    val resultHistory: StateFlow<List<HistoryListItem>> =
        combine(originalResultHistory, _raceWorldwideHistorySortState) { tracks, sortState ->
            sortHistory(tracks, sortState)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()

        )

    //<editor-fold desc="Race Worldwide">
    private val _raceWorldwideSortState = MutableStateFlow(TrackSortState())
    private val originalThreeLapTrackDetailedData: Flow<List<ThreeLapTrackDetailedData>> = raceResultRepository.getThreeLapTrackDetailedDataList()

    val threeLapTrackDetailedData: StateFlow<List<ThreeLapTrackDetailedData>> =
        combine(
            originalThreeLapTrackDetailedData,
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
        }.combine(_raceWorldwideSortState) { tracks, sortState ->
            sortTracks(tracks, sortState)
        }.stateIn( // Konvertiert den Flow in einen StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Startet, wenn es Subscriber gibt, stoppt nach 5s ohne
            initialValue = emptyList() // Initialwert, bis der erste Wert vom Flow kommt
        )

    private val _raceWorldwideRouteSortState = MutableStateFlow(TrackSortState())
    private val originalRouteDetailedData: Flow<List<RouteDetailedData>> = raceResultRepository.getRouteDetailedDataList()

    val routeDetailedData: StateFlow<List<RouteDetailedData>> =
        combine(
            originalRouteDetailedData,
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
        }.combine(_raceWorldwideRouteSortState) { tracks, sortState ->
            sortRoutes(tracks, sortState)
        }.stateIn( // Konvertiert den Flow in einen StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Startet, wenn es Subscriber gibt, stoppt nach 5s ohne
            initialValue = emptyList() // Initialwert, bis der erste Wert vom Flow kommt
        )

    val raceCountPOJO: Flow<RaceCountByType> = raceResultRepository.getRaceCountPOJO()
    val medianRaceCountPerSessionPOJO: Flow<MedianRaceCountPerSessionByType> = raceResultRepository.getMedianRaceCountPerSessionPOJO()
    val averagePositionPOJO: Flow<AveragePositionByType> = raceResultRepository.getAveragePositionPOJO()
    val raceSessionCount: Flow<Int> = onlineSessionRepository.raceSessionCount

    // Methode, die vom Fragment aufgerufen wird, um die Sortierung zu ändern
    fun requestRaceWorldwideSort(requestedColumn: SortColumn) {
        viewModelScope.launch { // In einer Coroutine, da _sortState ein StateFlow ist
            _raceWorldwideSortState.update { currentState ->
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
    fun getRaceWorldwideSortState(): TrackSortState {
        return _raceWorldwideSortState.value
    }

    fun requestRaceWorldwideRouteSort(requestedColumn: SortColumn) {
        viewModelScope.launch { // In einer Coroutine, da _sortState ein StateFlow ist
            _raceWorldwideRouteSortState.update { currentState ->
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
    fun getRaceWorldwideRouteSortState(): TrackSortState {
        return _raceWorldwideRouteSortState.value
    }
//</editor-fold>

    fun getHistorySortState(): TrackSortState {
        return _raceWorldwideHistorySortState.value
    }

    fun requestHistorySort(requestedColumn: SortColumn) {
        viewModelScope.launch { // In einer Coroutine, da _sortState ein StateFlow ist
            _raceWorldwideHistorySortState.update { currentState ->
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
    ): List<HistoryListItem> {
        var sortedResults: List<ResultHistory>

        sortedResults = tracks.sortedWith(
            compareByDescending<ResultHistory> { it.onlineSessionId }
                .thenByDescending { it.creationDate }
        )

        if (sortState.column == SortColumn.AMOUNT) {
            if (sortState.direction == SortDirection.ASCENDING) {
                sortedResults = tracks.sortedWith(
                    compareBy<ResultHistory> { it.onlineSessionId }
                        .thenBy { it.creationDate }
                )
            }
        }

        val groupedList = mutableListOf<HistoryListItem>()
        var lastSessionId: Long? = null

        for (result in sortedResults) {
            if (result.onlineSessionId != lastSessionId) {
                // Neue Session beginnt, füge einen Header hinzu
                groupedList.add(
                    HistoryListItem.SessionHeaderItem(
                        sessionId = result.onlineSessionId,
                        sessionCreationDate = result.onlineSessionCreationDate
                    )
                )
                lastSessionId = result.onlineSessionId
            }
            // Füge das eigentliche Ergebnis hinzu
            groupedList.add(HistoryListItem.ResultHistoryItem(result))
        }

        return groupedList
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

class StatisticsRaceWorldwideViewModelFactory(private val raceResultRepository: RaceResultRepository, private val onlineSessionRepository: OnlineSessionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsRaceWorldwideViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatisticsRaceWorldwideViewModel(raceResultRepository, onlineSessionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}