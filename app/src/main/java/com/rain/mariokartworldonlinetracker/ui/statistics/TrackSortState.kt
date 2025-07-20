package com.rain.mariokartworldonlinetracker.ui.statistics

import com.rain.mariokartworldonlinetracker.SortColumn
import com.rain.mariokartworldonlinetracker.SortDirection

data class TrackSortState(
    val column: SortColumn = SortColumn.NAME, // Standard-Sortierung
    val direction: SortDirection = SortDirection.ASCENDING
)