package com.rain.mariokartworldonlinetracker.ui.statistics.knockout.worldwide

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 2

class StatisticsKnockoutPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StatisticsKnockoutWorldwideGeneralFragment()
            1 -> StatisticsKnockoutWorldwideRalliesFragment()
            else -> throw IllegalStateException("Invalid adapter position")
        }
    }
}