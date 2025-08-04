package com.rain.mariokartworldonlinetracker.ui.statistics.knockout

import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.ui.statistics.BaseStatisticsFragment

abstract class BaseStatisticsKnockoutFragment<VB : ViewBinding>(
    raceCategory: RaceCategory)
    : BaseStatisticsFragment<VB, StatisticsKnockoutViewModel>(
    raceCategory) {

    override fun getViewModelClass(): Class<StatisticsKnockoutViewModel> {
        return StatisticsKnockoutViewModel::class.java
    }

    override fun getViewModelFactory(
        raceCategory: RaceCategory,
        raceResultRepository: RaceResultRepository,
        onlineSessionRepository: OnlineSessionRepository
    ): ViewModelProvider.Factory {
        return StatisticsKnockoutViewModelFactory(
            raceCategory,
            raceResultRepository,
            onlineSessionRepository
        )
    }
}