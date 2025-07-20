package com.rain.mariokartworldonlinetracker.ui.statistics.race.versus

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 4

class StatisticsRaceVersusPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StatisticsRaceVersusGeneralFragment()
            1 -> StatisticsRaceVersusTracksFragment()
            2 -> StatisticsRaceVersusRoutesFragment()
            3 -> StatisticsRaceVersusHistoryFragment()
            else -> throw IllegalStateException("Invalid adapter position")
        }
    }
}