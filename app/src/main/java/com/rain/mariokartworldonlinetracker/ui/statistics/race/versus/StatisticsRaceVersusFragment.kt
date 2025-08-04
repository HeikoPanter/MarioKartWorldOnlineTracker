package com.rain.mariokartworldonlinetracker.ui.statistics.race.versus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.databinding.FragmentStatisticsRaceVersusBinding
import com.rain.mariokartworldonlinetracker.ui.statistics.race.BaseStatisticsRaceFragment
import com.rain.mariokartworldonlinetracker.ui.statistics.race.StatisticsRaceViewModel
import com.rain.mariokartworldonlinetracker.ui.statistics.race.StatisticsRaceViewModelFactory

class StatisticsRaceVersusFragment : BaseStatisticsRaceFragment<FragmentStatisticsRaceVersusBinding>(
    RaceCategory.RACE_VS
) {
    override fun createBinding(inflater: LayoutInflater,container: ViewGroup?): FragmentStatisticsRaceVersusBinding {
        return FragmentStatisticsRaceVersusBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter =
            StatisticsRaceVersusPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.statistics_tab_general)
                1 -> getString(R.string.statistics_tab_tracks)
                2 -> getString(R.string.statistics_tab_routes)
                3 -> getString(R.string.statistics_tab_history)
                else -> null
            }
        }.attach()
    }
}