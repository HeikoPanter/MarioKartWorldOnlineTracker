package com.rain.mariokartworldonlinetracker.ui.statistics

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 2

class StatisticsKnockoutPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StatisticsKnockoutGeneralFragment()
            1 -> StatisticsKnockoutRalliesFragment()
            else -> throw IllegalStateException("Invalid adapter position")
        }
    }
}