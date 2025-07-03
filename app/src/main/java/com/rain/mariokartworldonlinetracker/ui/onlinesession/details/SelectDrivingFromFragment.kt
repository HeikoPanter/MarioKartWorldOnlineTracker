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
import com.rain.mariokartworldonlinetracker.TrackAndKnockoutHelper
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
            requireActivity(),
            NewOnlineSessionViewModelFactory(repository))
            .get(NewOnlineSessionViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lastTrackName = newOnlineSessionViewModel.lastDrivingToTrackName
        val lastTrackContentDescr = lastTrackName?.name ?: "No last track"
        var lastTrackResId = if (lastTrackName == null) R.drawable.noselected  else TrackAndKnockoutHelper.getTrackResId(lastTrackName)

        val imageMarginDp = 4
        val imageMarginPx = (imageMarginDp * resources.displayMetrics.density).toInt()

        var buttonFromLast = TrackAndKnockoutHelper.createImageView(
            requireContext(),
            lastTrackContentDescr,
            lastTrackResId,
            imageMarginPx
            )

        if (lastTrackName == null) {
            buttonFromLast.isEnabled = false
        }

        var buttonFromNone = TrackAndKnockoutHelper.createImageView(
            requireContext(),
            "Three lap track",
            R.drawable.threelap,
            imageMarginPx
        )

        var buttonFromOther = TrackAndKnockoutHelper.createImageView(
            requireContext(),
            "Other track",
            R.drawable.othertracks,
            imageMarginPx
        )

        buttonFromLast.setOnClickListener {
            navigateNext(DrivingFromOption.LAST)
        }

        buttonFromNone.setOnClickListener {
            navigateNext(DrivingFromOption.NONE)
        }

        buttonFromOther.setOnClickListener {
            navigateNext(DrivingFromOption.OTHER)
        }

        binding.fromlastLayout.addView(buttonFromLast)
        binding.fromnoneLayout.addView(buttonFromNone)
        binding.fromotherLayout.addView(buttonFromOther)
    }

    fun navigateNext(drivingFromOption: DrivingFromOption) {
        newOnlineSessionViewModel.setDrivingFromOption(drivingFromOption)

        if (drivingFromOption == DrivingFromOption.OTHER) {
            findNavController().navigate(R.id.action_selectDrivingFromFragment_to_selectDrivingFromOtherFragment)
        }
        else {
            findNavController().navigate(R.id.action_selectDrivingFromFragment_to_selectDrivingToFragment)
        }

    }
}
