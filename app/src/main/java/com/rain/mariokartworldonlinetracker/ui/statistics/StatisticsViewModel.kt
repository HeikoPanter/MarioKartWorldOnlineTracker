package com.rain.mariokartworldonlinetracker.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rain.mariokartworldonlinetracker.KnockoutCupName
import com.rain.mariokartworldonlinetracker.TrackName
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository // Ihr Repository
import com.rain.mariokartworldonlinetracker.data.pojo.AveragePositionByType
import com.rain.mariokartworldonlinetracker.data.pojo.MedianRaceCountPerSessionByType
import com.rain.mariokartworldonlinetracker.data.pojo.MostPlayedRaceRoute
import com.rain.mariokartworldonlinetracker.data.pojo.RaceCountByType
import kotlinx.coroutines.flow.Flow

class StatisticsViewModel(
    private val raceResultRepository: RaceResultRepository,
    private val onlineSessionRepository: OnlineSessionRepository
) : ViewModel() {

    /*
    Race values
     */
    val raceCountPOJO: Flow<RaceCountByType> = raceResultRepository.getRaceCountPOJO()
    val medianRaceCountPerSessionPOJO: Flow<MedianRaceCountPerSessionByType> = raceResultRepository.getMedianRaceCountPerSessionPOJO()
    val averagePositionPOJO: Flow<AveragePositionByType> = raceResultRepository.getAveragePositionPOJO()
    val mostPlayedThreelapTrackName: Flow<TrackName?> = raceResultRepository.getMostFrequentThreelapTrackName()
    val mostPlayedRaceRoute: Flow<MostPlayedRaceRoute?> = raceResultRepository.getMostFrequentRouteTrackName()
    val raceSessionCount: Flow<Int> = onlineSessionRepository.raceSessionCount

    /*
    Knockout values
     */
    val knockoutSessionCount: Flow<Int> = onlineSessionRepository.knockoutSessionCount
    val knockoutCount: Flow<Int> = raceResultRepository.knockoutCountTotal
    val knockoutAveragePosition: Flow<Int?> = raceResultRepository.knockoutAveragePosition
    val mostFrequentKnockoutCup: Flow<KnockoutCupName?> = raceResultRepository.mostFrequentKnockoutCup
    val medianKnockoutCountPerSession: Flow<Int> = raceResultRepository.getMedianKnockoutCountPerSession()
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