package com.rain.mariokartworldonlinetracker.ui.statistics.knockout.worldwide

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
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.databinding.FragmentStatisticsKnockoutWorldwideGeneralBinding
import com.rain.mariokartworldonlinetracker.ui.statistics.knockout.StatisticsKnockoutViewModel
import com.rain.mariokartworldonlinetracker.ui.statistics.knockout.StatisticsKnockoutViewModelFactory
import kotlinx.coroutines.launch

class StatisticsKnockoutWorldwideGeneralFragment : Fragment() {

    private var _binding: FragmentStatisticsKnockoutWorldwideGeneralBinding? = null
    private val binding get() = _binding!!

    private lateinit var statisticsViewModel: StatisticsKnockoutViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsKnockoutWorldwideGeneralBinding.inflate(inflater, container, false)

        // Repository und ViewModel initialisieren
        val raceResultDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.raceResultDao()
        val sessionDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.onlineSessionDao()
        statisticsViewModel = ViewModelProvider(requireActivity(),
            StatisticsKnockoutViewModelFactory(
                RaceCategory.KNOCKOUT,
            RaceResultRepository(raceResultDao),
            OnlineSessionRepository(sessionDao)
        )
        )
            .get(RaceCategory.KNOCKOUT.name, StatisticsKnockoutViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Knockout Session Count
                launch {
                    statisticsViewModel.sessionCount.collect { sessionCount ->
                        updateKnockoutSessionCount(sessionCount)
                    }
                }
//
                // Knockout Race Count
                launch {
                    statisticsViewModel.countTotal.collect { raceCounts ->
                        // UI mit den neuen Werten aktualisieren
                        updateKnockoutCountUI(raceCounts)
                    }
                }
//
                // Average Race per Session Count
                launch {
                    statisticsViewModel.medianCountPerSession.collect { averageRaceCountPerSession ->
                        updateAverageKnockoutCountPerSessionUI(averageRaceCountPerSession)
                    }
                }
//
                // Average Position
                launch {
                    statisticsViewModel.averagePosition.collect { averagePosition ->
                        updateAverageKnockoutPositionUI(averagePosition)
                    }
                }
            }
        }
    }

    private fun updateKnockoutSessionCount(sessionCount: Int) {
        binding.statisticsKnockoutTotalSessions.text = sessionCount.toString()
    }

    private fun updateKnockoutCountUI(knockoutCount: Int) {
        binding.statisticsKnockoutTotalRacesTotal.text = knockoutCount.toString()
    }

    private fun updateAverageKnockoutCountPerSessionUI(averageKnockoutCountPerSession: Int) {
        binding.statisticsKnockoutAverageRacesPerSessionTotal.text = averageKnockoutCountPerSession.toString()
    }

    private fun updateAverageKnockoutPositionUI(averagePosition: Int?) {
        binding.statisticsKnockoutAveragePositionTotal.text = (averagePosition ?: 0).toString()
    }
}