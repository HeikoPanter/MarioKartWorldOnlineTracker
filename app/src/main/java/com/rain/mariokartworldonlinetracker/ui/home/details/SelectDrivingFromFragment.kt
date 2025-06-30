package com.rain.mariokartworldonlinetracker.ui.home.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rain.mariokartworldonlinetracker.DrivingFromOption
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.RaceResultRepository
import com.rain.mariokartworldonlinetracker.databinding.FragmentSelectDrivingFromBinding
import com.rain.mariokartworldonlinetracker.ui.home.NewRaceViewModel
import com.rain.mariokartworldonlinetracker.ui.home.NewRaceViewModelFactory

class SelectDrivingFromFragment : Fragment() {

    private var _binding: FragmentSelectDrivingFromBinding? = null
    private lateinit var newRaceViewModel: NewRaceViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSelectDrivingFromBinding.inflate(inflater, container, false)
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

        binding.buttonFromLast.setOnClickListener {
            newRaceViewModel.setDrivingFromOption(DrivingFromOption.LAST)
            findNavController().navigate(R.id.action_selectDrivingFromFragment_to_selectDrivingToFragment)
        }
        binding.buttonFromNone.setOnClickListener {
            newRaceViewModel.setDrivingFromOption(DrivingFromOption.NONE)
            findNavController().navigate(R.id.action_selectDrivingFromFragment_to_selectDrivingToFragment)
        }
        binding.buttonFromOther.setOnClickListener {
            newRaceViewModel.setDrivingFromOption(DrivingFromOption.OTHER)
            // Hier könnten Sie entweder direkt zum nächsten Fragment navigieren
            // und dort die Eingabe für "Other" ermöglichen, oder ein Dialog/Eingabefeld hier anzeigen.
            // Für Einfachheit navigieren wir weiter und behandeln "Other" im nächsten Schritt,
            // falls dort eine manuelle Eingabe für "drivingFrom" erfolgen soll.
            // Alternativ könnte "Other" hier eine direkte Eingabe erfordern.
            // Wenn "Other" bedeutet, dass "from" im nächsten Schritt wie "to" ausgewählt wird, dann ist es ok.
            // Wenn "Other" hier eine explizite Eingabe für "from" bedeuten soll, muss das UI angepasst werden.
            findNavController().navigate(R.id.action_selectDrivingFromFragment_to_selectDrivingToFragment)
        }
    }
}
