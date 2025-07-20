package com.rain.mariokartworldonlinetracker.ui.statistics.knockout.versus

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
import com.rain.mariokartworldonlinetracker.SortColumn
import com.rain.mariokartworldonlinetracker.SortDirection
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.data.pojo.ResultHistory
import com.rain.mariokartworldonlinetracker.data.pojo.ThreeLapTrackDetailedData
import com.rain.mariokartworldonlinetracker.databinding.FragmentStatisticsKnockoutVersusHistoryBinding
import com.rain.mariokartworldonlinetracker.ui.statistics.HistoryDiffCallback
import com.rain.mariokartworldonlinetracker.ui.statistics.HistoryViewHolder
import com.rain.mariokartworldonlinetracker.ui.statistics.StatisticsListAdapter
import com.rain.mariokartworldonlinetracker.ui.statistics.race.TrackDiffCallback
import com.rain.mariokartworldonlinetracker.ui.statistics.race.TrackViewHolder
import com.rain.mariokartworldonlinetracker.ui.statistics.race.versus.StatisticsRaceVersusViewModel
import com.rain.mariokartworldonlinetracker.ui.statistics.race.versus.StatisticsRaceVersusViewModelFactory
import com.rain.mariokartworldonlinetracker.ui.statistics.race.worldwide.StatisticsRaceWorldwideViewModel
import com.rain.mariokartworldonlinetracker.ui.statistics.race.worldwide.StatisticsRaceWorldwideViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StatisticsKnockoutVersusHistoryFragment : Fragment() {
    private var _binding: FragmentStatisticsKnockoutVersusHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var statisticsViewModel: StatisticsRallyVersusViewModel
    private lateinit var trackListAdapter: StatisticsListAdapter<ResultHistory, HistoryViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsKnockoutVersusHistoryBinding.inflate(inflater, container, false)

        // Repository und ViewModel initialisieren
        // Owner des Repos ist die Activity, damit untergeordnete Fragments auch auf die gleiche Instanz zugreifen
        val raceResultDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.raceResultDao()
        val sessionDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.onlineSessionDao()
        statisticsViewModel = ViewModelProvider(requireActivity(),
            StatisticsRallyVersusViewModelFactory(
                RaceResultRepository(raceResultDao),
                OnlineSessionRepository(sessionDao)
            )
        )
            .get(StatisticsRallyVersusViewModel::class.java)

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
        trackListAdapter = StatisticsListAdapter<ResultHistory, HistoryViewHolder>(
            HistoryDiffCallback(),
            viewHolderCreator = { parent, _ -> HistoryViewHolder.create(parent) }
        )
        binding.tracksRecyclerview.apply {
            adapter = trackListAdapter
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
                    trackListAdapter.submitList(trackList)

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