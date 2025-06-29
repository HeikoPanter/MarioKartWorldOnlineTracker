package com.rain.mariokartworldonlinetracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView // Wird möglicherweise nicht mehr benötigt, wenn Sie nur die Buttons verwenden
import androidx.activity.result.launch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.application
import androidx.lifecycle.lifecycleScope
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.RaceMode
import com.rain.mariokartworldonlinetracker.RaceResult
import com.rain.mariokartworldonlinetracker.RaceResultRepository
// Import für MaterialButton, falls noch nicht vorhanden
// import com.google.android.material.button.MaterialButton
import com.rain.mariokartworldonlinetracker.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: HomeViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Der Code für das TextView kann bleiben, wenn Sie es noch verwenden
        // val textView: TextView = binding.textHome
        // homeViewModel.text.observe(viewLifecycleOwner) {
        //     textView.text = it
        // }

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Repository und ViewModel initialisieren (Beispiel, besser mit Dependency Injection)
        val raceResultDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.raceResultDao()
        val repository = RaceResultRepository(raceResultDao)
        homeViewModel = ViewModelProvider(this, HomeViewModelFactory(repository)).get(HomeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Direkter Zugriff auf die Buttons über das binding-Objekt
        // Die IDs (button_new_race, button_new_knockout) werden automatisch
        // in CamelCase-Properties im binding-Objekt umgewandelt.
        binding.buttonNewRace.setOnClickListener {
            val newRace = RaceResult(
                category = RaceCategory.RACE,
                mode = RaceMode._150CC,
                knockoutCupName = null,
                drivingFromTrackName = null,
                drivingToTrackName = "Mario Bros. Circuit",
                position = null,
                date = System.currentTimeMillis()
            )
            homeViewModel.insertRaceResult(newRace)
        }

        binding.buttonNewKnockout.setOnClickListener {
            val newRace = RaceResult(
                category = RaceCategory.KNOCKOUT,
                mode = RaceMode._150CC,
                knockoutCupName = "Moon Cup",
                drivingFromTrackName = null,
                drivingToTrackName = null,
                position = null,
                date = System.currentTimeMillis()
            )
            homeViewModel.insertRaceResult(newRace)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Wichtig, um Memory Leaks zu vermeiden
    }
}