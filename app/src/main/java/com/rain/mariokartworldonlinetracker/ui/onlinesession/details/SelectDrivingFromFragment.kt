package com.rain.mariokartworldonlinetracker.ui.onlinesession.details

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
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModel
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModelFactory

class SelectDrivingFromFragment : Fragment() {

    private var _binding: FragmentSelectDrivingFromBinding? = null
    private lateinit var newOnlineSessionViewModel: NewOnlineSessionViewModel

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSelectDrivingFromBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val raceResultDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.raceResultDao()
        val repository = RaceResultRepository(raceResultDao)
        newOnlineSessionViewModel = ViewModelProvider(
            findNavController().getViewModelStoreOwner(R.id.new_race_flow_nav_graph_id),
            NewOnlineSessionViewModelFactory(repository))
            .get(NewOnlineSessionViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFromLast.setOnClickListener {
            navigateNext(DrivingFromOption.LAST)
        }

        binding.buttonFromNone.setOnClickListener {
            navigateNext(DrivingFromOption.NONE)
        }

        binding.buttonFromOther.setOnClickListener {
            navigateNext(DrivingFromOption.OTHER)
        }
    }

    fun navigateNext(drivingFromOption: DrivingFromOption) {
        newOnlineSessionViewModel.setDrivingFromOption(drivingFromOption)
        findNavController().navigate(R.id.action_selectDrivingFromFragment_to_selectDrivingToFragment)
    }
}
