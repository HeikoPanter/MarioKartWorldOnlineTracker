package com.rain.mariokartworldonlinetracker.ui.home.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.RaceResultRepository
import com.rain.mariokartworldonlinetracker.databinding.FragmentEnterPositionBinding
import com.rain.mariokartworldonlinetracker.ui.home.NewRaceViewModel
import com.rain.mariokartworldonlinetracker.ui.home.NewRaceViewModelFactory

class EnterPositionFragment : Fragment() {
    private var _binding: FragmentEnterPositionBinding? = null
    private lateinit var newRaceViewModel: NewRaceViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEnterPositionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Repository und ViewModel initialisieren (Beispiel, besser mit Dependency Injection)
        val raceResultDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.raceResultDao()
        val repository = RaceResultRepository(raceResultDao)
        val factory = NewRaceViewModelFactory(repository)

        newRaceViewModel = ViewModelProvider(findNavController().getViewModelStoreOwner(R.id.new_race_flow_nav_graph), factory)
            .get(NewRaceViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Beobachte lastDrivingToTrackName, um den "Last"-Button ggf. zu aktualisieren
        newRaceViewModel.lastDrivingToTrackName.observe(viewLifecycleOwner) { lastName ->
            // binding.buttonFromLast.text = "Last (${lastName ?: "N/A"})"
        }

        binding.buttonPosition1.setOnClickListener {
            newRaceViewModel.setPosition(1)
            findNavController().popBackStack(R.id.homeFragment, false)
        }
        binding.buttonPosition2.setOnClickListener {
            newRaceViewModel.setPosition(2)
            findNavController().popBackStack(R.id.homeFragment, false)
        }
        binding.buttonPosition3.setOnClickListener {
            newRaceViewModel.setPosition(3)
            findNavController().popBackStack(R.id.homeFragment, false)
        }
    }
}