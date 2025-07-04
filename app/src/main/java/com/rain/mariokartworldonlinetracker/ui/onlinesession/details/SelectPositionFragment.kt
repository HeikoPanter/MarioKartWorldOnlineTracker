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
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.databinding.FragmentSelectPositionBinding
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModel
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModelFactory
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModelProvider

class SelectPositionFragment : Fragment() {
    private var _binding: FragmentSelectPositionBinding? = null
    private lateinit var newOnlineSessionViewModel: NewOnlineSessionViewModel

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSelectPositionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        newOnlineSessionViewModel = NewOnlineSessionViewModelProvider.getViewModel(
            requireActivity().application as MarioKartWorldOnlineTrackerApplication,
            requireActivity()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonPosition1.setOnClickListener {
            navigateNext(1)
        }

        binding.buttonPosition2.setOnClickListener {
            navigateNext(2)
        }

        binding.buttonPosition3.setOnClickListener {
            navigateNext(3)
        }
    }

    fun navigateNext(position: Short?) {
        newOnlineSessionViewModel.setPosition(position)
        newOnlineSessionViewModel.saveNewRace()
        findNavController().navigate(R.id.action_selectPosition_back_to_selectEngineClass)

    }
}