package com.rain.mariokartworldonlinetracker.ui.statistics

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 3

class StatisticsRaceVersusPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StatisticsRaceVersusGeneralFragment()
            1 -> StatisticsRaceVersusTracksFragment()
            2 -> StatisticsRaceVersusRoutesFragment()
            else -> throw IllegalStateException("Invalid adapter position")
        }
    }
}