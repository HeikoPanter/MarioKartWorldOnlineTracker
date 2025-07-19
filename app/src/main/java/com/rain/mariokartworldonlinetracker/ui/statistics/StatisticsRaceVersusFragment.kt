package com.rain.mariokartworldonlinetracker.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rain.mariokartworldonlinetracker.KnockoutCupName
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.TrackAndKnockoutHelper
import com.rain.mariokartworldonlinetracker.TrackName
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.data.pojo.AveragePositionByType
import com.rain.mariokartworldonlinetracker.data.pojo.MedianRaceCountPerSessionByType
import com.rain.mariokartworldonlinetracker.data.pojo.MostPlayedRaceRoute
import com.rain.mariokartworldonlinetracker.data.pojo.RaceCountByType
import com.rain.mariokartworldonlinetracker.databinding.FragmentStatisticsRaceVersusBinding
import kotlinx.coroutines.launch

class StatisticsRaceVersusFragment : Fragment() {

    private var _binding: FragmentStatisticsRaceVersusBinding? = null
    private val binding get() = _binding!!

    private lateinit var statisticsViewModel: StatisticsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsRaceVersusBinding.inflate(inflater, container, false)

        // Repository und ViewModel initialisieren
        val raceResultDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.raceResultDao()
        val sessionDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.onlineSessionDao()
        statisticsViewModel = ViewModelProvider(this, StatisticsViewModelFactory(RaceResultRepository(raceResultDao),
            OnlineSessionRepository(sessionDao)))
            .get(StatisticsViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                //<editor-fold descr="Race VS Statistics">
                launch {
                    statisticsViewModel.raceVsCountPOJO.collect { raceCounts ->
                        // UI mit den neuen Werten aktualisieren
                        updateRaceVsCountUI(raceCounts)
                    }
                }
//
                //// Session Count
                launch {
                    statisticsViewModel.raceVsSessionCount.collect { sessionCount ->
                        updateSessionVsCountUI(sessionCount)
                    }
                }
//
                //// Average Race per Session Count
                launch {
                    statisticsViewModel.medianRaceVsCountPerSessionPOJO.collect { averageRaceCountPerSession ->
                        updateAverageRaceVsCountPerSessionUI(averageRaceCountPerSession)
                    }
                }
//
                //// Average Position
                launch {
                    statisticsViewModel.averageRaceVsPositionPOJO.collect { averagePosition ->
                        updateAverageRaceVsPositionUI(averagePosition)
                    }
                }
//
                //// Most played threelap track
                launch {
                    statisticsViewModel.mostPlayedThreelapVsTrackName.collect { trackName ->
                        updateMostPlayedThreelapVsTrack(trackName)
                    }
                }

                // Most played route
                launch {
                    statisticsViewModel.mostPlayedRaceVsRoute.collect { route ->
                        updateMostPlayedVsRoute(route)
                    }
                }
                //</editor-fold>

            }
        }
    }

    //<editor-fold descr="Race VS methods">
    private fun updateRaceVsCountUI(raceCounts: RaceCountByType) {
        binding.cardRaceVsStatistics.statisticsRaceVsTotalRacesTotal.text = raceCounts.raceCountTotal.toString()
        binding.cardRaceVsStatistics.statisticsRaceVsTotalRacesThreelap.text = raceCounts.raceCountThreelap.toString()
        binding.cardRaceVsStatistics.statisticsRaceVsTotalRacesRoute.text = raceCounts.raceCountRoute.toString()
    }

    private fun updateSessionVsCountUI(sessionCount: Int) {
        binding.cardRaceVsStatistics.statisticsRaceVsTotalSessions.text = sessionCount.toString()
    }

    private fun updateAverageRaceVsCountPerSessionUI(averageRaceCountPerSession: MedianRaceCountPerSessionByType) {
        binding.cardRaceVsStatistics.statisticsRaceVsAverageRacesPerSessionTotal.text = averageRaceCountPerSession.raceCountTotal.toString()
        binding.cardRaceVsStatistics.statisticsRaceVsAverageRacesPerSessionThreelap.text = averageRaceCountPerSession.raceCountThreelap.toString()
        binding.cardRaceVsStatistics.statisticsRaceVsAverageRacesPerSessionRoute.text = averageRaceCountPerSession.raceCountRoute.toString()
    }

    private fun updateAverageRaceVsPositionUI(averagePosition: AveragePositionByType) {
        binding.cardRaceVsStatistics.statisticsRaceVsAveragePositionTotal.text = averagePosition.averagePositionTotal.toString()
        binding.cardRaceVsStatistics.statisticsRaceVsAveragePositionThreelap.text = averagePosition.averagePositionThreelap.toString()
        binding.cardRaceVsStatistics.statisticsRaceVsAveragePositionRoute.text = averagePosition.averagePositionRoute.toString()
    }

    private fun updateMostPlayedThreelapVsTrack(trackName: TrackName?) {
        binding.cardRaceVsStatistics.statisticsRaceVsMostPlayedThreeLapTrack.setImageResource(TrackAndKnockoutHelper.getTrackResId(trackName))
    }

    private fun updateMostPlayedVsRoute(route: MostPlayedRaceRoute?) {
        binding.cardRaceVsStatistics.statisticsRaceVsMostPlayedRouteFrom.setImageResource(TrackAndKnockoutHelper.getTrackResId(route?.drivingFromTrackName))
        binding.cardRaceVsStatistics.statisticsRaceVsMostPlayedRouteTo.setImageResource(TrackAndKnockoutHelper.getTrackResId(route?.drivingToTrackName))
    }
    //</editor-fold>
}