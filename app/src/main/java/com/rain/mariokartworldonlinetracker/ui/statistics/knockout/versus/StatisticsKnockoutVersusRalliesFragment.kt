package com.rain.mariokartworldonlinetracker.ui.statistics.knockout.versus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import com.rain.mariokartworldonlinetracker.data.pojo.RallyDetailedData
import com.rain.mariokartworldonlinetracker.databinding.FragmentStatisticsKnockoutVersusRalliesBinding
import com.rain.mariokartworldonlinetracker.ui.statistics.StatisticsListAdapter
import com.rain.mariokartworldonlinetracker.ui.statistics.knockout.RallyDiffCallback
import com.rain.mariokartworldonlinetracker.ui.statistics.knockout.RallyViewHolder
import com.rain.mariokartworldonlinetracker.ui.statistics.knockout.StatisticsKnockoutViewModel
import com.rain.mariokartworldonlinetracker.ui.statistics.knockout.StatisticsKnockoutViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StatisticsKnockoutVersusRalliesFragment : Fragment() {

    private var _binding: FragmentStatisticsKnockoutVersusRalliesBinding? = null
    private val binding get() = _binding!!

    private lateinit var statisticsViewModel: StatisticsKnockoutViewModel
    private lateinit var trackListAdapter: StatisticsListAdapter<RallyDetailedData, RallyViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsKnockoutVersusRalliesBinding.inflate(inflater, container, false)

        // Repository und ViewModel initialisieren
        val raceResultDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.raceResultDao()
        val sessionDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.onlineSessionDao()
        statisticsViewModel = ViewModelProvider(requireActivity(),
            StatisticsKnockoutViewModelFactory(
                RaceCategory.KNOCKOUT_VS,
            RaceResultRepository(raceResultDao),
            OnlineSessionRepository(sessionDao)
        )
        )
            .get(RaceCategory.KNOCKOUT_VS.name, StatisticsKnockoutViewModel::class.java)

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
        trackListAdapter = StatisticsListAdapter<RallyDetailedData, RallyViewHolder>(
            RallyDiffCallback(),
            viewHolderCreator = { parent, _ -> RallyViewHolder.create(parent) }
        )
        binding.tracksRecyclerview.apply {
            adapter = trackListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupHeaderClickListeners() {
        binding.trackListHeader.headerName.setOnClickListener {
            statisticsViewModel.requestSort(SortColumn.NAME)
        }
        binding.trackListHeader.headerPosition.setOnClickListener {
            statisticsViewModel.requestSort(SortColumn.POSITION)
        }
        binding.trackListHeader.headerAmount.setOnClickListener {
            statisticsViewModel.requestSort(SortColumn.AMOUNT)
        }
    }

    private fun observeSortedTrackData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                statisticsViewModel.detailedData.collectLatest { trackList ->
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
        val sortState = statisticsViewModel.getSortState()

        // Setze alle Header-Texte zurück
        binding.trackListHeader.headerName.text = getString(R.string.statistics_list_header_rally)
        binding.trackListHeader.headerPosition.text = getString(R.string.statistics_list_header_position)
        binding.trackListHeader.headerAmount.text = getString(R.string.statistics_list_header_amount)

        val arrow = if (sortState.direction == SortDirection.ASCENDING) " ↑" else " ↓"

        when (sortState.column) {
            SortColumn.NAME -> binding.trackListHeader.headerName.append(arrow)
            SortColumn.POSITION -> binding.trackListHeader.headerPosition.append(arrow)
            SortColumn.AMOUNT -> binding.trackListHeader.headerAmount.append(arrow)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.tracksRecyclerview.adapter = null
        _binding = null
    }
}