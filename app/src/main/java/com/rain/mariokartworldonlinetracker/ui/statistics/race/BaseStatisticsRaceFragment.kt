package com.rain.mariokartworldonlinetracker.ui.statistics.race

import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.ui.statistics.BaseStatisticsFragment

abstract class BaseStatisticsRaceFragment<VB : ViewBinding>(
    raceCategory: RaceCategory)
    : BaseStatisticsFragment<VB, StatisticsRaceViewModel>(
        raceCategory) {

    override fun getViewModelClass(): Class<StatisticsRaceViewModel> {
        return StatisticsRaceViewModel::class.java
    }

    override fun getViewModelFactory(
        raceCategory: RaceCategory,
        raceResultRepository: RaceResultRepository,
        onlineSessionRepository: OnlineSessionRepository
    ): ViewModelProvider.Factory {
        return StatisticsRaceViewModelFactory(
            raceCategory,
            raceResultRepository,
            onlineSessionRepository
        )
    }
}