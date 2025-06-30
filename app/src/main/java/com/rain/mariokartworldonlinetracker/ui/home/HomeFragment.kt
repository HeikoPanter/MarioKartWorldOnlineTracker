package com.rain.mariokartworldonlinetracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.RaceMode
import com.rain.mariokartworldonlinetracker.RaceResult
import com.rain.mariokartworldonlinetracker.RaceResultRepository
// Import für MaterialButton, falls noch nicht vorhanden
// import com.google.android.material.button.MaterialButton
import com.rain.mariokartworldonlinetracker.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var newRaceViewModel: NewRaceViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Repository und ViewModel initialisieren (Beispiel, besser mit Dependency Injection)
        val raceResultDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.raceResultDao()
        val repository = RaceResultRepository(raceResultDao)
        newRaceViewModel = ViewModelProvider(this, NewRaceViewModelFactory(repository)).get(NewRaceViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Direkter Zugriff auf die Buttons über das binding-Objekt
        // Die IDs (button_new_race, button_new_knockout) werden automatisch
        // in CamelCase-Properties im binding-Objekt umgewandelt.
        binding.buttonNewRace150cc.setOnClickListener {
            // Navigiert zum Start des "New Race"-Flows
            // Annahme: action_homeFragment_to_newRaceFlow ist eine Action zu new_race_flow_nav_graph
            // oder direkt zu selectDrivingFromFragment, wenn es im Hauptgraphen ist.
            findNavController().navigate(R.id.action_homeFragment_to_newRaceFlow)
        }

        binding.buttonNewKnockout150cc.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Wichtig, um Memory Leaks zu vermeiden
    }
}