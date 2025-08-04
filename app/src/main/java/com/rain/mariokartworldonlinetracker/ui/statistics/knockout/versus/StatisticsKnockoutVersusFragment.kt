package com.rain.mariokartworldonlinetracker.ui.statistics.knockout.versus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.databinding.FragmentStatisticsKnockoutVersusBinding
import com.rain.mariokartworldonlinetracker.databinding.FragmentStatisticsRaceVersusBinding
import com.rain.mariokartworldonlinetracker.ui.statistics.knockout.BaseStatisticsKnockoutFragment
import com.rain.mariokartworldonlinetracker.ui.statistics.knockout.StatisticsKnockoutViewModel
import com.rain.mariokartworldonlinetracker.ui.statistics.knockout.StatisticsKnockoutViewModelFactory

class StatisticsKnockoutVersusFragment : BaseStatisticsKnockoutFragment<FragmentStatisticsKnockoutVersusBinding>(
    RaceCategory.KNOCKOUT_VS
) {

    override fun createBinding(inflater: LayoutInflater,container: ViewGroup?): FragmentStatisticsKnockoutVersusBinding {
        return FragmentStatisticsKnockoutVersusBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter =
            StatisticsKnockoutVersusPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.statistics_tab_general)
                1 -> getString(R.string.statistics_tab_rallies)
                2 -> getString(R.string.statistics_tab_history)
                else -> null
            }
        }.attach()
    }
}