package com.rain.mariokartworldonlinetracker.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rain.mariokartworldonlinetracker.MkwotSettings
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.SortColumn
import com.rain.mariokartworldonlinetracker.SortDirection
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.data.pojo.HistoryListItem
import com.rain.mariokartworldonlinetracker.data.pojo.ResultHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseStatisticsViewModel<E : Enum<E>, DD : DetailedData<E>>(
    protected val raceCategory: RaceCategory,
    protected val raceResultRepository: RaceResultRepository,
    protected val onlineSessionRepository: OnlineSessionRepository
) : ViewModel() {
    private val _historySortState = MutableStateFlow(TrackSortState(column = SortColumn.AMOUNT, direction = SortDirection.DESCENDING))
    private val _originalResultHistory: Flow<List<ResultHistory>> = raceResultRepository.getResultHistory(raceCategory)
    private val _sortState = MutableStateFlow(TrackSortState())
    private val _originalDetailedData: Flow<List<DD>> = getDetailedData()

    protected val _showAllEntriesSetting: StateFlow<Boolean> = MkwotSettings.showAllEntries

    val sessionCount: Flow<Int> = onlineSessionRepository.getSessionCount(raceCategory)

    val resultHistory: StateFlow<List<HistoryListItem>> =
        combine(_originalResultHistory, _historySortState) { tracks, sortState ->
            sortHistory(tracks, sortState)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()

        )

    val detailedData: StateFlow<List<DD>> =
        combine(
            _originalDetailedData,
            _showAllEntriesSetting
        ) { wwTracks, showAll ->
            val trackMap = wwTracks.associateBy { it.name }

            if (showAll) {
                getDetailedDataBaseList().map { track ->
                    val dbData = trackMap[track.name]
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
        }.combine(_sortState) { tracks, sortState ->
            sort(tracks, sortState)
        }.stateIn( // Konvertiert den Flow in einen StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Startet, wenn es Subscriber gibt, stoppt nach 5s ohne
            initialValue = emptyList() // Initialwert, bis der erste Wert vom Flow kommt
        )

    protected abstract fun getDetailedDataBaseList(): List<DD>
    protected abstract fun getDetailedData(): Flow<List<DD>>

    // Methode, die vom Fragment aufgerufen wird, um die Sortierung zu ändern
    fun requestSort(requestedColumn: SortColumn) {
        viewModelScope.launch { // In einer Coroutine, da _sortState ein StateFlow ist
            _sortState.update { currentState ->
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
    fun getSortState(): TrackSortState {
        return _sortState.value
    }

    fun getHistorySortState(): TrackSortState {
        return _historySortState.value
    }

    fun requestHistorySort(requestedColumn: SortColumn) {
        viewModelScope.launch { // In einer Coroutine, da _sortState ein StateFlow ist
            _historySortState.update { currentState ->
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

    private fun sort(
        tracks: List<DD>,
        sortState: TrackSortState
    ): List<DD> {
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
}