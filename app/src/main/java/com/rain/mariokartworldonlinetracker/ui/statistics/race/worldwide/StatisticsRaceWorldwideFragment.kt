package com.rain.mariokartworldonlinetracker.ui.statistics.race.worldwide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.databinding.FragmentStatisticsRaceWorldwideBinding

class StatisticsRaceWorldwideFragment : Fragment() {

    private var _binding: FragmentStatisticsRaceWorldwideBinding? = null
    private val binding get() = _binding!!

    private lateinit var statisticsViewModel: StatisticsRaceWorldwideViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsRaceWorldwideBinding.inflate(inflater, container, false)

        // Repository und ViewModel initialisieren
        // Owner des Repos ist die Activity, damit untergeordnete Fragments auch auf die gleiche Instanz zugreifen
        val raceResultDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.raceResultDao()
        val sessionDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.onlineSessionDao()
        statisticsViewModel = ViewModelProvider(requireActivity(), StatisticsRaceWorldwideViewModelFactory(
            RaceResultRepository(raceResultDao),
            OnlineSessionRepository(sessionDao)
        )
        )
            .get(StatisticsRaceWorldwideViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter =
            StatisticsRaceWorldwidePagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.statistics_tab_general)
                1 -> getString(R.string.statistics_tab_tracks)
                2 -> getString(R.string.statistics_tab_routes)
                else -> null
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}