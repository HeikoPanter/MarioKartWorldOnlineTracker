package com.rain.mariokartworldonlinetracker.ui.statistics.race.worldwide

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
import com.rain.mariokartworldonlinetracker.data.pojo.RouteDetailedData
import com.rain.mariokartworldonlinetracker.databinding.FragmentStatisticsRaceWorldwideRoutesBinding
import com.rain.mariokartworldonlinetracker.ui.statistics.StatisticsListAdapter
import com.rain.mariokartworldonlinetracker.ui.statistics.race.RouteDiffCallback
import com.rain.mariokartworldonlinetracker.ui.statistics.race.RouteViewHolder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StatisticsRaceWorldwideRoutesFragment : Fragment() {
    private var _binding: FragmentStatisticsRaceWorldwideRoutesBinding? = null
    private val binding get() = _binding!!

    private lateinit var statisticsViewModel: StatisticsRaceWorldwideViewModel
    private lateinit var trackListAdapter: StatisticsListAdapter<RouteDetailedData, RouteViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsRaceWorldwideRoutesBinding.inflate(inflater, container, false)

        // Repository und ViewModel initialisieren
        // Owner des Repos ist die Activity, damit untergeordnete Fragments auch auf die gleiche Instanz zugreifen
        val raceResultDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.raceResultDao()
        val sessionDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.onlineSessionDao()
        statisticsViewModel = ViewModelProvider(requireActivity(), StatisticsRaceWorldwideViewModelFactory(
            RaceResultRepository(raceResultDao),
            OnlineSessionRepository(sessionDao)
        )
        )
            .get(StatisticsRaceWorldwideViewModel::class.java)

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
        trackListAdapter = StatisticsListAdapter<RouteDetailedData, RouteViewHolder>(
            RouteDiffCallback(),
            viewHolderCreator = { parent, _ -> RouteViewHolder.create(parent) }
        )
        binding.tracksRecyclerview.apply {
            adapter = trackListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupHeaderClickListeners() {
        binding.trackListHeader.headerName.setOnClickListener {
            statisticsViewModel.requestRaceWorldwideRouteSort(SortColumn.NAME)
        }
        binding.trackListHeader.headerPosition.setOnClickListener {
            statisticsViewModel.requestRaceWorldwideRouteSort(SortColumn.POSITION)
        }
        binding.trackListHeader.headerAmount.setOnClickListener {
            statisticsViewModel.requestRaceWorldwideRouteSort(SortColumn.AMOUNT)
        }
    }

    private fun observeSortedTrackData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                statisticsViewModel.routeDetailedData.collectLatest { trackList ->
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
        val sortState = statisticsViewModel.getRaceWorldwideRouteSortState()

        // Setze alle Header-Texte zurück
        binding.trackListHeader.headerName.text = getString(R.string.statistics_list_header_route)
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