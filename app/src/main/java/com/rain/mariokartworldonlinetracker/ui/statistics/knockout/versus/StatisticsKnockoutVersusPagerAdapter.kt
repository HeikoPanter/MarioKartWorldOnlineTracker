package com.rain.mariokartworldonlinetracker.ui.statistics.knockout.versus

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 3

class StatisticsKnockoutVersusPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StatisticsKnockoutVersusGeneralFragment()
            1 -> StatisticsKnockoutVersusRalliesFragment()
            2 -> StatisticsKnockoutVersusHistoryFragment()
            else -> throw IllegalStateException("Invalid adapter position")
        }
    }
}