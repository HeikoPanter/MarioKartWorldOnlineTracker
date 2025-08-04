package com.rain.mariokartworldonlinetracker.ui.statistics.race.versus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.data.pojo.AveragePositionByType
import com.rain.mariokartworldonlinetracker.data.pojo.MedianRaceCountPerSessionByType
import com.rain.mariokartworldonlinetracker.data.pojo.RaceCountByType
import com.rain.mariokartworldonlinetracker.databinding.FragmentStatisticsRaceVersusGeneralBinding
import com.rain.mariokartworldonlinetracker.ui.statistics.race.BaseStatisticsRaceFragment
import kotlinx.coroutines.launch

class StatisticsRaceVersusGeneralFragment : BaseStatisticsRaceFragment<FragmentStatisticsRaceVersusGeneralBinding>(
    RaceCategory.RACE_VS
) {

    override fun createBinding(inflater: LayoutInflater,container: ViewGroup?): FragmentStatisticsRaceVersusGeneralBinding {
        return FragmentStatisticsRaceVersusGeneralBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    statisticsViewModel.raceCountPOJO.collect { raceCounts ->
                        // UI mit den neuen Werten aktualisieren
                        updateRaceVsCountUI(raceCounts)
                    }
                }
//
                //// Session Count
                launch {
                    statisticsViewModel.sessionCount.collect { sessionCount ->
                        updateSessionVsCountUI(sessionCount)
                    }
                }
//
                //// Average Race per Session Count
                launch {
                    statisticsViewModel.medianRaceCountPerSessionPOJO.collect { averageRaceCountPerSession ->
                        updateAverageRaceVsCountPerSessionUI(averageRaceCountPerSession)
                    }
                }
//
                //// Average Position
                launch {
                    statisticsViewModel.averagePositionPOJO.collect { averagePosition ->
                        updateAverageRaceVsPositionUI(averagePosition)
                    }
                }
            }
        }
    }

    private fun updateRaceVsCountUI(raceCounts: RaceCountByType) {
        binding.statisticsRaceVsTotalRacesTotal.text = raceCounts.raceCountTotal.toString()
        binding.statisticsRaceVsTotalRacesThreelap.text = raceCounts.raceCountThreelap.toString()
        binding.statisticsRaceVsTotalRacesRoute.text = raceCounts.raceCountRoute.toString()
    }

    private fun updateSessionVsCountUI(sessionCount: Int) {
        binding.statisticsRaceVsTotalSessions.text = sessionCount.toString()
    }

    private fun updateAverageRaceVsCountPerSessionUI(averageRaceCountPerSession: MedianRaceCountPerSessionByType) {
        binding.statisticsRaceVsAverageRacesPerSessionTotal.text = averageRaceCountPerSession.raceCountTotal.toString()
        binding.statisticsRaceVsAverageRacesPerSessionThreelap.text = averageRaceCountPerSession.raceCountThreelap.toString()
        binding.statisticsRaceVsAverageRacesPerSessionRoute.text = averageRaceCountPerSession.raceCountRoute.toString()
    }

    private fun updateAverageRaceVsPositionUI(averagePosition: AveragePositionByType) {
        binding.statisticsRaceVsAveragePositionTotal.text = averagePosition.averagePositionTotal.toString()
        binding.statisticsRaceVsAveragePositionThreelap.text = averagePosition.averagePositionThreelap.toString()
        binding.statisticsRaceVsAveragePositionRoute.text = averagePosition.averagePositionRoute.toString()
    }
}