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
import com.rain.mariokartworldonlinetracker.databinding.FragmentStatisticsKnockoutWorldwideHistoryBinding
import com.rain.mariokartworldonlinetracker.ui.statistics.HistoryAdapter
import com.rain.mariokartworldonlinetracker.ui.statistics.knockout.StatisticsKnockoutViewModel
import com.rain.mariokartworldonlinetracker.ui.statistics.knockout.StatisticsKnockoutViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StatisticsKnockoutWorldwideHistoryFragment : Fragment() {
    private var _binding: FragmentStatisticsKnockoutWorldwideHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var statisticsViewModel: StatisticsKnockoutViewModel
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsKnockoutWorldwideHistoryBinding.inflate(inflater, container, false)

        // Repository und ViewModel initialisieren
        // Owner des Repos ist die Activity, damit untergeordnete Fragments auch auf die gleiche Instanz zugreifen
        val raceResultDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.raceResultDao()
        val sessionDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.onlineSessionDao()
        statisticsViewModel = ViewModelProvider(requireActivity(),
            StatisticsKnockoutViewModelFactory(
                RaceCategory.KNOCKOUT,
                RaceResultRepository(raceResultDao),
                OnlineSessionRepository(sessionDao)
            )
        )
            .get(RaceCategory.KNOCKOUT.name, StatisticsKnockoutViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupHeaderClickListeners()
        observeSortedTrackData()
        updateHeaderUI()
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter {
            if (isAdded && _binding != null && binding.tracksRecyclerview.adapter?.itemCount ?: 0 > 0) {
                // Post ist hier weniger kritisch, da onCurrentListChanged synchron zum Update ist,
                // aber für komplexe Layouts kann es immer noch sicherer sein.
                binding.tracksRecyclerview.post { // Versuch es erst ohne post
                    if (isAdded && _binding != null) { // Doppelter Check wegen post
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

                    if (trackList.isNotEmpty() && isAdded && _binding != null) {
                        binding.tracksRecyclerview.post { // Wichtig: .post{}
                            if (isAdded && _binding != null) {
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
        super.onDestroyView()
        binding.tracksRecyclerview.adapter = null
        _binding = null
    }
}