package com.rain.mariokartworldonlinetracker.ui.statistics.race.worldwide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.data.pojo.AveragePositionByType
import com.rain.mariokartworldonlinetracker.data.pojo.MedianRaceCountPerSessionByType
import com.rain.mariokartworldonlinetracker.data.pojo.RaceCountByType
import com.rain.mariokartworldonlinetracker.databinding.FragmentStatisticsRaceWorldwideGeneralBinding
import com.rain.mariokartworldonlinetracker.ui.statistics.race.StatisticsRaceViewModel
import com.rain.mariokartworldonlinetracker.ui.statistics.race.StatisticsRaceViewModelFactory
import kotlinx.coroutines.launch

class StatisticsRaceWorldwideGeneralFragment : Fragment() {
    private var _binding: FragmentStatisticsRaceWorldwideGeneralBinding? = null
    private val binding get() = _binding!!

    private lateinit var statisticsViewModel: StatisticsRaceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsRaceWorldwideGeneralBinding.inflate(inflater, container, false)

        // Repository und ViewModel initialisieren
        // Owner des Repos ist die Activity, damit untergeordnete Fragments auch auf die gleiche Instanz zugreifen
        val raceResultDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.raceResultDao()
        val sessionDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.onlineSessionDao()
        statisticsViewModel = ViewModelProvider(requireActivity(), StatisticsRaceViewModelFactory(
            RaceCategory.RACE,
            RaceResultRepository(raceResultDao),
            OnlineSessionRepository(sessionDao)
        )
        )
            .get(RaceCategory.RACE.name, StatisticsRaceViewModel::class.java)

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
//
                //// Session Count
                launch {
                    statisticsViewModel.sessionCount.collect { sessionCount ->
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
}