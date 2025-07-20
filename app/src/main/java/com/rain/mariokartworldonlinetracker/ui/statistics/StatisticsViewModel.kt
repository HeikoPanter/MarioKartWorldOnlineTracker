package com.rain.mariokartworldonlinetracker.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rain.mariokartworldonlinetracker.KnockoutCupName
import com.rain.mariokartworldonlinetracker.SortColumn
import com.rain.mariokartworldonlinetracker.SortDirection
import com.rain.mariokartworldonlinetracker.TrackName
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository // Ihr Repository
import com.rain.mariokartworldonlinetracker.data.pojo.AveragePositionByType
import com.rain.mariokartworldonlinetracker.data.pojo.MedianRaceCountPerSessionByType
import com.rain.mariokartworldonlinetracker.data.pojo.MostPlayedRaceRoute
import com.rain.mariokartworldonlinetracker.data.pojo.RaceCountByType
import com.rain.mariokartworldonlinetracker.data.pojo.RallyDetailedData
import com.rain.mariokartworldonlinetracker.data.pojo.RouteDetailedData
import com.rain.mariokartworldonlinetracker.data.pojo.ThreeLapTrackDetailedData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TrackSortState(
    val column: SortColumn = SortColumn.NAME, // Standard-Sortierung
    val direction: SortDirection = SortDirection.ASCENDING
)

class StatisticsViewModel(
    private val raceResultRepository: RaceResultRepository,
    private val onlineSessionRepository: OnlineSessionRepository
) : ViewModel() {


    //<editor-fold desc="Race Worldwide">
    private val _raceWorldwideSortState = MutableStateFlow(TrackSortState())
    private val originalThreeLapTrackDetailedData: Flow<List<ThreeLapTrackDetailedData>> = raceResultRepository.getThreeLapTrackDetailedDataList()

    val threeLapTrackDetailedData: StateFlow<List<ThreeLapTrackDetailedData>> =
        combine(originalThreeLapTrackDetailedData, _raceWorldwideSortState) { tracks, sortState ->
            sortTracks(tracks, sortState)
        }.stateIn( // Konvertiert den Flow in einen StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Startet, wenn es Subscriber gibt, stoppt nach 5s ohne
            initialValue = emptyList() // Initialwert, bis der erste Wert vom Flow kommt
        )

    private val _raceWorldwideRouteSortState = MutableStateFlow(TrackSortState())
    private val originalRouteDetailedData: Flow<List<RouteDetailedData>> = raceResultRepository.getRouteDetailedDataList()

    val routeDetailedData: StateFlow<List<RouteDetailedData>> =
        combine(originalRouteDetailedData, _raceWorldwideRouteSortState) { tracks, sortState ->
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


    //<editor-fold desc="Race VS values">

    private val _raceVersusSortState = MutableStateFlow(TrackSortState())
    private val originalVersusThreeLapTrackDetailedData: Flow<List<ThreeLapTrackDetailedData>> = raceResultRepository.getVsThreeLapTrackDetailedDataList()

    val versusThreeLapTrackDetailedData: StateFlow<List<ThreeLapTrackDetailedData>> =
        combine(originalVersusThreeLapTrackDetailedData, _raceVersusSortState) { tracks, sortState ->
            sortTracks(tracks, sortState)
        }.stateIn( // Konvertiert den Flow in einen StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Startet, wenn es Subscriber gibt, stoppt nach 5s ohne
            initialValue = emptyList() // Initialwert, bis der erste Wert vom Flow kommt
        )

    private val _raceVersusRouteSortState = MutableStateFlow(TrackSortState())
    private val originalVersusRouteDetailedData: Flow<List<RouteDetailedData>> = raceResultRepository.getVsRouteDetailedDataList()

    val versusRouteDetailedData: StateFlow<List<RouteDetailedData>> =
        combine(originalVersusRouteDetailedData, _raceVersusRouteSortState) { tracks, sortState ->
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

    //<editor-fold desc="Knockout values">

    private val _knockoutWorldwideSortState = MutableStateFlow(TrackSortState())
    private val originalRallyDetailedData: Flow<List<RallyDetailedData>> = raceResultRepository.getRallyDetailedDataList()

    val rallyDetailedData: StateFlow<List<RallyDetailedData>> =
        combine(originalRallyDetailedData, _knockoutWorldwideSortState) { tracks, sortState ->
            sortRallies(tracks, sortState)
        }.stateIn( // Konvertiert den Flow in einen StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Startet, wenn es Subscriber gibt, stoppt nach 5s ohne
            initialValue = emptyList() // Initialwert, bis der erste Wert vom Flow kommt
        )

    val knockoutSessionCount: Flow<Int> = onlineSessionRepository.knockoutSessionCount
    val knockoutCount: Flow<Int> = raceResultRepository.knockoutCountTotal
    val knockoutAveragePosition: Flow<Int?> = raceResultRepository.knockoutAveragePosition
    val medianKnockoutCountPerSession: Flow<Int> = raceResultRepository.getMedianKnockoutCountPerSession()

    // Methode, die vom Fragment aufgerufen wird, um die Sortierung zu ändern
    fun requestKnockoutWorldwideSort(requestedColumn: SortColumn) {
        viewModelScope.launch { // In einer Coroutine, da _sortState ein StateFlow ist
            _knockoutWorldwideSortState.update { currentState ->
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
    fun getKnockoutWorldwideSortState(): TrackSortState {
        return _knockoutWorldwideSortState.value
    }

    //</editor-fold>

    //<editor-fold desc="Knockout VS values">

    private val _knockoutVersusSortState = MutableStateFlow(TrackSortState())
    private val originalVersusRallyDetailedData: Flow<List<RallyDetailedData>> = raceResultRepository.getVsRallyDetailedDataList()

    val versusRallyDetailedData: StateFlow<List<RallyDetailedData>> =
        combine(originalVersusRallyDetailedData, _knockoutVersusSortState) { tracks, sortState ->
            sortRallies(tracks, sortState)
        }.stateIn( // Konvertiert den Flow in einen StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Startet, wenn es Subscriber gibt, stoppt nach 5s ohne
            initialValue = emptyList() // Initialwert, bis der erste Wert vom Flow kommt
        )

    val knockoutVsSessionCount: Flow<Int> = onlineSessionRepository.knockoutVsSessionCount
    val knockoutVsCount: Flow<Int> = raceResultRepository.knockoutVsCountTotal
    val knockoutVsAveragePosition: Flow<Int?> = raceResultRepository.knockoutVsAveragePosition
    val medianKnockoutVsCountPerSession: Flow<Int> = raceResultRepository.getMedianKnockoutVsCountPerSession()

    // Methode, die vom Fragment aufgerufen wird, um die Sortierung zu ändern
    fun requestKnockoutVersusSort(requestedColumn: SortColumn) {
        viewModelScope.launch { // In einer Coroutine, da _sortState ein StateFlow ist
            _knockoutVersusSortState.update { currentState ->
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
    fun getKnockoutVersusSortState(): TrackSortState {
        return _knockoutVersusSortState.value
    }

    //</editor-fold>

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

    private fun sortRallies(
        tracks: List<RallyDetailedData>,
        sortState: TrackSortState
    ): List<RallyDetailedData> {
        return when (sortState.column) {
            SortColumn.NAME -> {
                if (sortState.direction == SortDirection.ASCENDING) {
                    tracks.sortedBy { it.knockoutCupName }
                } else {
                    tracks.sortedByDescending { it.knockoutCupName }
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

class StatisticsViewModelFactory(private val raceResultRepository: RaceResultRepository, private val onlineSessionRepository: OnlineSessionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatisticsViewModel(raceResultRepository, onlineSessionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}