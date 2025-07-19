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
import com.rain.mariokartworldonlinetracker.databinding.FragmentStatisticsRaceWorldwideBinding
import kotlinx.coroutines.launch

class StatisticsRaceWorldwideFragment : Fragment() {

    private var _binding: FragmentStatisticsRaceWorldwideBinding? = null
    private val binding get() = _binding!!

    private lateinit var statisticsViewModel: StatisticsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsRaceWorldwideBinding.inflate(inflater, container, false)

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

                //<editor-fold descr="Race Statistics">
                // Race Count
                launch {
                    statisticsViewModel.raceCountPOJO.collect { raceCounts ->
                        // UI mit den neuen Werten aktualisieren
                        updateRaceCountUI(raceCounts)
                    }
                }
//
                //// Session Count
                launch {
                    statisticsViewModel.raceSessionCount.collect { sessionCount ->
                        updateSessionCountUI(sessionCount)
                    }
                }
//
                //// Average Race per Session Count
                launch {
                    statisticsViewModel.medianRaceCountPerSessionPOJO.collect { averageRaceCountPerSession ->
                        updateAverageRaceCountPerSessionUI(averageRaceCountPerSession)
                    }
                }
//
                //// Average Position
                launch {
                    statisticsViewModel.averagePositionPOJO.collect { averagePosition ->
                        updateAveragePositionUI(averagePosition)
                    }
                }
//
                //// Most played threelap track
                launch {
                    statisticsViewModel.mostPlayedThreelapTrackName.collect { trackName ->
                        updateMostPlayedThreelapTrack(trackName)
                    }
                }

                // Most played route
                launch {
                    statisticsViewModel.mostPlayedRaceRoute.collect { route ->
                        updateMostPlayedRoute(route)
                    }
                }
                //</editor-fold>
            }
        }
    }

    //<editor-fold descr="Race methods">
    private fun updateRaceCountUI(raceCounts: RaceCountByType) {
        binding.cardRaceStatistics.statisticsRaceTotalRacesTotal.text = raceCounts.raceCountTotal.toString()
        binding.cardRaceStatistics.statisticsRaceTotalRacesThreelap.text = raceCounts.raceCountThreelap.toString()
        binding.cardRaceStatistics.statisticsRaceTotalRacesRoute.text = raceCounts.raceCountRoute.toString()
    }

    private fun updateSessionCountUI(sessionCount: Int) {
        binding.cardRaceStatistics.statisticsRaceTotalSessions.text = sessionCount.toString()
    }

    private fun updateAverageRaceCountPerSessionUI(averageRaceCountPerSession: MedianRaceCountPerSessionByType) {
        binding.cardRaceStatistics.statisticsRaceAverageRacesPerSessionTotal.text = averageRaceCountPerSession.raceCountTotal.toString()
        binding.cardRaceStatistics.statisticsRaceAverageRacesPerSessionThreelap.text = averageRaceCountPerSession.raceCountThreelap.toString()
        binding.cardRaceStatistics.statisticsRaceAverageRacesPerSessionRoute.text = averageRaceCountPerSession.raceCountRoute.toString()
    }

    private fun updateAveragePositionUI(averagePosition: AveragePositionByType) {
        binding.cardRaceStatistics.statisticsRaceAveragePositionTotal.text = averagePosition.averagePositionTotal.toString()
        binding.cardRaceStatistics.statisticsRaceAveragePositionThreelap.text = averagePosition.averagePositionThreelap.toString()
        binding.cardRaceStatistics.statisticsRaceAveragePositionRoute.text = averagePosition.averagePositionRoute.toString()
    }

    private fun updateMostPlayedThreelapTrack(trackName: TrackName?) {
        binding.cardRaceStatistics.statisticsRaceMostPlayedThreeLapTrack.setImageResource(TrackAndKnockoutHelper.getTrackResId(trackName))
    }

    private fun updateMostPlayedRoute(route: MostPlayedRaceRoute?) {
        binding.cardRaceStatistics.statisticsRaceMostPlayedRouteFrom.setImageResource(TrackAndKnockoutHelper.getTrackResId(route?.drivingFromTrackName))
        binding.cardRaceStatistics.statisticsRaceMostPlayedRouteTo.setImageResource(TrackAndKnockoutHelper.getTrackResId(route?.drivingToTrackName))
    }
    //</editor-fold>

}