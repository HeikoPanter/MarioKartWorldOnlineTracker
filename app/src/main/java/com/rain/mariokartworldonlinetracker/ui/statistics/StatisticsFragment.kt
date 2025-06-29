package com.rain.mariokartworldonlinetracker.ui.statistics

import StatisticsViewModel
import StatisticsViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.RaceResultRepository
import com.rain.mariokartworldonlinetracker.databinding.FragmentStatisticsBinding

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private lateinit var statisticsViewModel: StatisticsViewModel
    private lateinit var raceResultAdapter: RaceResultAdapter // Sie müssen diesen Adapter erstellen

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)

        // Repository und ViewModel initialisieren
        val raceResultDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.raceResultDao()
        val repository = RaceResultRepository(raceResultDao)
        statisticsViewModel = ViewModelProvider(this, StatisticsViewModelFactory(repository))
            .get(StatisticsViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // Beobachten der LiveData vom ViewModel
        statisticsViewModel.allRaceResultsLiveData.observe(viewLifecycleOwner) { results ->
            // results ist eine List<RaceResult>
            if (results.isNullOrEmpty()) {
                binding.recyclerViewRaceResults.visibility = View.GONE
                binding.textViewNoResults.visibility = View.VISIBLE
            } else {
                binding.recyclerViewRaceResults.visibility = View.VISIBLE
                binding.textViewNoResults.visibility = View.GONE
                raceResultAdapter.submitList(results)
            }
        }
    }

    private fun setupRecyclerView() {
        raceResultAdapter = RaceResultAdapter { raceResult ->
            // Hier können Sie eine Aktion definieren, wenn auf ein Item geklickt wird (optional)
            // z.B. Navigiere zu einem Detailbildschirm
        }
        binding.recyclerViewRaceResults.apply {
            adapter = raceResultAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Wichtig, um Memory Leaks zu vermeiden
    }
}