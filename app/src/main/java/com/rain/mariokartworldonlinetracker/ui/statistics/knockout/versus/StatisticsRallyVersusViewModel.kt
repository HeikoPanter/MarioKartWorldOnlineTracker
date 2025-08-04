package com.rain.mariokartworldonlinetracker.ui.statistics.knockout.versus

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
import com.rain.mariokartworldonlinetracker.data.pojo.RallyDetailedData
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

class StatisticsRallyVersusViewModel(
    private val raceResultRepository: RaceResultRepository,
    private val onlineSessionRepository: OnlineSessionRepository
) : ViewModel() {

    private val _showAllTracksSetting: StateFlow<Boolean> = MkwotSettings.showAllEntries

    private val _knockoutVersusHistorySortState = MutableStateFlow(TrackSortState(column = SortColumn.AMOUNT, direction = SortDirection.DESCENDING))
    private val originalResultHistory: Flow<List<ResultHistory>> = raceResultRepository.getResultHistory(RaceCategory.KNOCKOUT_VS)

    val resultHistory: StateFlow<List<HistoryListItem>> =
        combine(originalResultHistory, _knockoutVersusHistorySortState) { tracks, sortState ->
            sortHistory(tracks, sortState)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()

        )

    //<editor-fold desc="Knockout VS values">

    private val _knockoutVersusSortState = MutableStateFlow(TrackSortState())
    private val originalVersusRallyDetailedData: Flow<List<RallyDetailedData>> = raceResultRepository.getVsRallyDetailedDataList()

    val versusRallyDetailedData: StateFlow<List<RallyDetailedData>> =
        combine(
            originalVersusRallyDetailedData,
            _showAllTracksSetting
        ) { wwTracks, showAll ->
            val trackMap = wwTracks.associateBy { it.knockoutCupName }

            if (showAll) {
                TrackAndKnockoutHelper.getRallyList().map { track ->
                    val dbData = trackMap[track.knockoutCupName]
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
        }.combine(_knockoutVersusSortState) { tracks, sortState ->
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

    fun getHistorySortState(): TrackSortState {
        return _knockoutVersusHistorySortState.value
    }

    fun requestHistorySort(requestedColumn: SortColumn) {
        viewModelScope.launch { // In einer Coroutine, da _sortState ein StateFlow ist
            _knockoutVersusHistorySortState.update { currentState ->
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

class StatisticsRallyVersusViewModelFactory(private val raceResultRepository: RaceResultRepository, private val onlineSessionRepository: OnlineSessionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsRallyVersusViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatisticsRallyVersusViewModel(raceResultRepository, onlineSessionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}