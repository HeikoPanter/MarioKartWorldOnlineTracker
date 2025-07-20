package com.rain.mariokartworldonlinetracker.ui.statistics.knockout.versus

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 2

class StatisticsKnockoutVersusPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StatisticsKnockoutVersusGeneralFragment()
            1 -> StatisticsKnockoutVersusRalliesFragment()
            else -> throw IllegalStateException("Invalid adapter position")
        }
    }
}