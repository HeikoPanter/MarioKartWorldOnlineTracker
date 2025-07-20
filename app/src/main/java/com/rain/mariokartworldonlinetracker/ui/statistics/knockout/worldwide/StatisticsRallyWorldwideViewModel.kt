package com.rain.mariokartworldonlinetracker.ui.statistics.knockout.worldwide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rain.mariokartworldonlinetracker.MkwotSettings
import com.rain.mariokartworldonlinetracker.SortColumn
import com.rain.mariokartworldonlinetracker.SortDirection
import com.rain.mariokartworldonlinetracker.TrackAndKnockoutHelper
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.data.pojo.RallyDetailedData
import com.rain.mariokartworldonlinetracker.ui.statistics.TrackSortState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatisticsRallyWorldwideViewModel(
    private val raceResultRepository: RaceResultRepository,
    private val onlineSessionRepository: OnlineSessionRepository
) : ViewModel() {

    private val _showAllTracksSetting: StateFlow<Boolean> = MkwotSettings.showAllEntries

    //<editor-fold desc="Knockout values">

    private val _knockoutWorldwideSortState = MutableStateFlow(TrackSortState())
    private val originalRallyDetailedData: Flow<List<RallyDetailedData>> = raceResultRepository.getRallyDetailedDataList()

    val rallyDetailedData: StateFlow<List<RallyDetailedData>> =
        combine(
            originalRallyDetailedData,
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
        }.combine(_knockoutWorldwideSortState) { tracks, sortState ->
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

class StatisticsRallyWorldwideViewModelFactory(private val raceResultRepository: RaceResultRepository, private val onlineSessionRepository: OnlineSessionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsRallyWorldwideViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatisticsRallyWorldwideViewModel(raceResultRepository, onlineSessionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}