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
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.TrackAndKnockoutHelper
import com.rain.mariokartworldonlinetracker.TrackName
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.data.pojo.AveragePositionByType
import com.rain.mariokartworldonlinetracker.data.pojo.MedianRaceCountPerSessionByType
import com.rain.mariokartworldonlinetracker.data.pojo.MostPlayedRaceRoute
import com.rain.mariokartworldonlinetracker.data.pojo.RaceCountByType
import com.rain.mariokartworldonlinetracker.databinding.FragmentStatisticsBinding
import kotlinx.coroutines.launch

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private lateinit var statisticsViewModel: StatisticsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)

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
                // Race Count
                launch {
                    statisticsViewModel.raceCountPOJO.collect { raceCounts ->
                        // UI mit den neuen Werten aktualisieren
                        updateRaceCountUI(raceCounts)

                    }
                }

                // Session Count
                launch {
                    statisticsViewModel.raceSessionCount.collect { sessionCount ->
                        updateSessionCountUI(sessionCount)
                    }
                }

                // Average Race per Session Count
                launch {
                    statisticsViewModel.medianRaceCountPerSessionPOJO.collect { averageRaceCountPerSession ->
                        updateAverageRaceCountPerSessionUI(averageRaceCountPerSession)
                    }
                }

                // Average Position
                launch {
                    statisticsViewModel.averagePositionPOJO.collect { averagePosition ->
                        updateAveragePositionUI(averagePosition)
                    }
                }

                // Most played threelap track
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
            }
        }
    }

    private fun updateRaceCountUI(raceCounts: RaceCountByType) {
        binding.statisticsRaceTotalRacesTotal.text = raceCounts.raceCountTotal.toString()
        binding.statisticsRaceTotalRacesThreelap.text = raceCounts.raceCountThreelap.toString()
        binding.statisticsRaceTotalRacesRoute.text = raceCounts.raceCountRoute.toString()
    }

    private fun updateSessionCountUI(sessionCount: Int) {
        binding.statisticsRaceTotalSessions.text = sessionCount.toString()
    }

    private fun updateAverageRaceCountPerSessionUI(averageRaceCountPerSession: MedianRaceCountPerSessionByType) {
        binding.statisticsRaceAverageRacesPerSessionTotal.text = averageRaceCountPerSession.raceCountTotal.toString()
        binding.statisticsRaceAverageRacesPerSessionThreelap.text = averageRaceCountPerSession.raceCountThreelap.toString()
        binding.statisticsRaceAverageRacesPerSessionRoute.text = averageRaceCountPerSession.raceCountRoute.toString()
    }

    private fun updateAveragePositionUI(averagePosition: AveragePositionByType) {
        binding.statisticsRaceAveragePositionTotal.text = averagePosition.averagePositionTotal.toString()
        binding.statisticsRaceAveragePositionThreelap.text = averagePosition.averagePositionThreelap.toString()
        binding.statisticsRaceAveragePositionRoute.text = averagePosition.averagePositionRoute.toString()
    }

    private fun updateMostPlayedThreelapTrack(trackName: TrackName) {
        binding.statisticsRaceMostPlayedThreeLapTrack.setImageResource(TrackAndKnockoutHelper.getTrackResId(trackName))
    }

    private fun updateMostPlayedRoute(route: MostPlayedRaceRoute) {
        binding.statisticsRaceMostPlayedRouteFrom.setImageResource(TrackAndKnockoutHelper.getTrackResId(route.drivingFromTrackName))
        binding.statisticsRaceMostPlayedRouteTo.setImageResource(TrackAndKnockoutHelper.getTrackResId(route.drivingToTrackName))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Wichtig, um Memory Leaks zu vermeiden
    }
}