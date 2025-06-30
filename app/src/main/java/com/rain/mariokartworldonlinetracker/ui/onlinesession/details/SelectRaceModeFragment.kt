package com.rain.mariokartworldonlinetracker.ui.onlinesession.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.RaceMode
import com.rain.mariokartworldonlinetracker.RaceResultRepository
import com.rain.mariokartworldonlinetracker.databinding.FragmentSelectRaceModeBinding
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModel
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModelFactory

class SelectRaceModeFragment : Fragment() {
    private var _binding: FragmentSelectRaceModeBinding? = null
    private lateinit var newOnlineSessionViewModel: NewOnlineSessionViewModel

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSelectRaceModeBinding.inflate(inflater, container, false)
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

        binding.buttonMode150cc.setOnClickListener {
            navigateNext(RaceMode._150CC)
        }

        binding.buttonModeMirror.setOnClickListener {
            navigateNext(RaceMode.MIRROR)
        }

        binding.buttonMode100cc.setOnClickListener {
            navigateNext(RaceMode._100CC)
        }
    }

    fun navigateNext(raceMode: RaceMode) {
        newOnlineSessionViewModel.setRaceMode(raceMode)
        findNavController().navigate(R.id.action_selectRaceModeFragment_to_selectDrivingFromFragment)
    }
}