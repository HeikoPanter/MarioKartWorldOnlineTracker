package com.rain.mariokartworldonlinetracker.ui.statistics.knockout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rain.mariokartworldonlinetracker.KnockoutCupName
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.TrackAndKnockoutHelper
import com.rain.mariokartworldonlinetracker.data.MathUtils
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.data.pojo.RallyDetailedData
import com.rain.mariokartworldonlinetracker.ui.statistics.BaseStatisticsViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StatisticsKnockoutViewModel(
    raceCategory: RaceCategory,
    raceResultRepository: RaceResultRepository,
    onlineSessionRepository: OnlineSessionRepository
) : BaseStatisticsViewModel<KnockoutCupName, RallyDetailedData>(
    raceCategory,
    raceResultRepository,
    onlineSessionRepository
) {
    val countTotal: Flow<Int> = raceResultRepository.getCountTotal(raceCategory)
    val averagePosition: Flow<Int?> = raceResultRepository.getAveragePositionTotal(raceCategory)
    val medianCountPerSession: Flow<Int> = getMedianKnockoutVsCountPerSession()

    private fun getMedianKnockoutVsCountPerSession(): Flow<Int> {
        return raceResultRepository.getCountPerSessionTotal(raceCategory).map { countsList -> MathUtils.calculateMedian(countsList) }
    }

    override fun getDetailedDataBaseList(): List<RallyDetailedData> {
        return TrackAndKnockoutHelper.getRallyList()
    }

    override fun getDetailedData(): Flow<List<RallyDetailedData>> {
        return raceResultRepository.getRallyDetailedDataList(raceCategory)
    }
}

class StatisticsKnockoutViewModelFactory(
    private val raceCategory: RaceCategory,
    private val raceResultRepository: RaceResultRepository,
    private val onlineSessionRepository: OnlineSessionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsKnockoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatisticsKnockoutViewModel(raceCategory, raceResultRepository, onlineSessionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}