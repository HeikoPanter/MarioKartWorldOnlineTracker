package com.rain.mariokartworldonlinetracker.ui.onlinesession

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rain.mariokartworldonlinetracker.EngineClass
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.TrackAndKnockoutHelper
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.databinding.FragmentStartSessionBinding

class StartSessionFragment : Fragment() {

    private var _binding: FragmentStartSessionBinding? = null
    private lateinit var newOnlineSessionViewModel: NewOnlineSessionViewModel

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {

        _binding = FragmentStartSessionBinding.inflate(inflater, container, false)
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

        val imageMarginDp = 4
        val imageMarginPx = (imageMarginDp * resources.displayMetrics.density).toInt()

        var buttonRace = TrackAndKnockoutHelper.createImageView(
            requireContext(),
            "Race",
            R.drawable.button_race_2,
            imageMarginPx
        )

        var buttonVsRace = TrackAndKnockoutHelper.createImageView(
            requireContext(),
            "RaceVersus",
            R.drawable.button_race_vs_2,
            imageMarginPx
        )

        var buttonKnockout = TrackAndKnockoutHelper.createImageView(
            requireContext(),
            "Knockout",
            R.drawable.button_knockout_2,
            imageMarginPx
        )

        var buttonVsKnockout = TrackAndKnockoutHelper.createImageView(
            requireContext(),
            "KnockoutVersus",
            R.drawable.button_knockout_vs_2,
            imageMarginPx
        )

        buttonRace.setOnClickListener {
            navigateNext(RaceCategory.RACE)
        }

        buttonVsRace.setOnClickListener {
            navigateNext(RaceCategory.RACE_VS)
        }

        buttonKnockout.setOnClickListener {
            navigateNext(RaceCategory.KNOCKOUT)
        }

        buttonVsKnockout.setOnClickListener {
            navigateNext(RaceCategory.KNOCKOUT_VS)
        }

        binding.raceLayout.addView(buttonRace)
        binding.raceVsLayout.addView(buttonVsRace)
        binding.knockoutLayout.addView(buttonKnockout)
        binding.knockoutVsLayout.addView(buttonVsKnockout)
    }

    fun navigateNext(raceCategory: RaceCategory) {
        newOnlineSessionViewModel.resetSession(raceCategory)
        findNavController().navigate(R.id.action_startSessionFragment_to_newRaceFlowNavGraph)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}