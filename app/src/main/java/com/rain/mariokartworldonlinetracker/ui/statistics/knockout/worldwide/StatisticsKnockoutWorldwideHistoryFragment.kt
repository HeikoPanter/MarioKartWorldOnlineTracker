package com.rain.mariokartworldonlinetracker.ui.statistics.knockout.worldwide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.SortColumn
import com.rain.mariokartworldonlinetracker.SortDirection
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.databinding.FragmentStatisticsKnockoutWorldwideGeneralBinding
import com.rain.mariokartworldonlinetracker.databinding.FragmentStatisticsKnockoutWorldwideHistoryBinding
import com.rain.mariokartworldonlinetracker.ui.statistics.HistoryAdapter
import com.rain.mariokartworldonlinetracker.ui.statistics.knockout.BaseStatisticsKnockoutFragment
import com.rain.mariokartworldonlinetracker.ui.statistics.knockout.StatisticsKnockoutViewModel
import com.rain.mariokartworldonlinetracker.ui.statistics.knockout.StatisticsKnockoutViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StatisticsKnockoutWorldwideHistoryFragment : BaseStatisticsKnockoutFragment<FragmentStatisticsKnockoutWorldwideHistoryBinding>(
    RaceCategory.KNOCKOUT
) {

    override fun createBinding(inflater: LayoutInflater,container: ViewGroup?): FragmentStatisticsKnockoutWorldwideHistoryBinding {
        return FragmentStatisticsKnockoutWorldwideHistoryBinding.inflate(inflater, container, false)
    }
    private lateinit var historyAdapter: HistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupHeaderClickListeners()
        observeSortedTrackData()
        updateHeaderUI()
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter {
            if (isAdded && binding != null && binding.tracksRecyclerview.adapter?.itemCount ?: 0 > 0) {
                // Post ist hier weniger kritisch, da onCurrentListChanged synchron zum Update ist,
                // aber für komplexe Layouts kann es immer noch sicherer sein.
                binding.tracksRecyclerview.post { // Versuch es erst ohne post
                    if (isAdded && binding != null) { // Doppelter Check wegen post
                        (binding.tracksRecyclerview.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(0, 0)
                    }
                }
            }
        }
        binding.tracksRecyclerview.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupHeaderClickListeners() {
        binding.trackListHeader.headerDate.setOnClickListener {
            statisticsViewModel.requestHistorySort(SortColumn.AMOUNT)
        }
    }

    private fun observeSortedTrackData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                statisticsViewModel.resultHistory.collectLatest { trackList ->
                    historyAdapter.submitList(trackList)

                    updateHeaderUI()

                    if (trackList.isNotEmpty() && isAdded && binding != null) {
                        binding.tracksRecyclerview.post { // Wichtig: .post{}
                            if (isAdded && binding != null) {
                                binding.tracksRecyclerview.scrollToPosition(0)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateHeaderUI() {
        val sortState = statisticsViewModel.getHistorySortState()

        // Setze alle Header-Texte zurück
        binding.trackListHeader.headerDate.text = getString(R.string.statistics_history_header_date)

        val arrow = if (sortState.direction == SortDirection.ASCENDING) " ↑" else " ↓"

        if (sortState.column == SortColumn.AMOUNT) {
            binding.trackListHeader.headerDate.append(arrow)
        }
    }

    override fun onDestroyView() {
        binding.tracksRecyclerview.adapter = null
        super.onDestroyView()
    }
}