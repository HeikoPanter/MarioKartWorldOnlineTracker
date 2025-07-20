package com.rain.mariokartworldonlinetracker.ui.statistics.race.worldwide

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 3

class StatisticsRaceWorldwidePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StatisticsRaceWorldwideGeneralFragment()
            1 -> StatisticsRaceWorldwideTracksFragment()
            2 -> StatisticsRaceWorldwideRoutesFragment()
            else -> throw IllegalStateException("Invalid adapter position")
        }
    }
}