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
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.EngineClass
import com.rain.mariokartworldonlinetracker.RaceResultRepository
import com.rain.mariokartworldonlinetracker.databinding.FragmentSelectEngineClassBinding
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModel
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModelFactory

class SelectEngineClassFragment : Fragment() {
    private var _binding: FragmentSelectEngineClassBinding? = null
    private lateinit var newOnlineSessionViewModel: NewOnlineSessionViewModel

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSelectEngineClassBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val raceResultDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.raceResultDao()
        val repository = RaceResultRepository(raceResultDao)
        newOnlineSessionViewModel = ViewModelProvider(
            requireActivity(),
            NewOnlineSessionViewModelFactory(repository))
            .get(NewOnlineSessionViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonMode150cc.setOnClickListener {
            navigateNext(EngineClass._150CC)
        }

        binding.buttonModeMirror.setOnClickListener {
            navigateNext(EngineClass.MIRROR)
        }

        binding.buttonMode100cc.setOnClickListener {
            navigateNext(EngineClass._100CC)
        }
    }

    fun navigateNext(engineClass: EngineClass) {
        newOnlineSessionViewModel.setEngineClass(engineClass)
        if (newOnlineSessionViewModel.raceCategory == RaceCategory.RACE) {
            findNavController().navigate(R.id.action_selectEngineClassFragment_to_selectDrivingFromFragment)
        }
        else if (newOnlineSessionViewModel.raceCategory == RaceCategory.KNOCKOUT) {
            findNavController().navigate(R.id.action_selectEngineClassFragment_to_selectKnockoutCupFragment)
        }
    }
}