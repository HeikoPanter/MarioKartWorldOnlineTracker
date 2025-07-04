package com.rain.mariokartworldonlinetracker.ui.onlinesession.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.MkwotSettings
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.TrackAndKnockoutHelper
import com.rain.mariokartworldonlinetracker.TrackAndKnockoutHelper.createStandardTrackImageView
import com.rain.mariokartworldonlinetracker.TrackName
import com.rain.mariokartworldonlinetracker.databinding.FragmentSelectDrivingToBinding
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModel
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModelFactory
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModelProvider

class SelectDrivingToFragment : Fragment() {
    private var _binding: FragmentSelectDrivingToBinding? = null
    private lateinit var newOnlineSessionViewModel: NewOnlineSessionViewModel

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSelectDrivingToBinding.inflate(inflater, container, false)
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


        // After playing Rainbow Road, you start from Peach Stadium
        if (newOnlineSessionViewModel.drivingFromTrackName.value == TrackName.RR30) {
            newOnlineSessionViewModel.setDrivingFromTrackName(TrackName.PS15)
        }

        val trackOptions = TrackAndKnockoutHelper.getPossibleTracks(newOnlineSessionViewModel.drivingFromTrackName.value)
        val itemsPerRow = MkwotSettings.itemsPerRow
        val imageMarginDp = 4
        val imageMarginPx = (imageMarginDp * resources.displayMetrics.density).toInt()

        val drivingFromClickHandler = { trackName: TrackName ->
            navigateNext(trackName)
        }

        TrackAndKnockoutHelper.populateImageRows(
            requireContext(),
            trackOptions,
            itemsPerRow,
            binding.imageViewContainer,
            imageMarginPx,
            TrackAndKnockoutHelper::createStandardTrackImageView,
            drivingFromClickHandler
        )
    }

    fun navigateNext(drivingToTrackName: TrackName) {
        newOnlineSessionViewModel.setDrivingToTrackName(drivingToTrackName)
        findNavController().navigate(R.id.action_selectDrivingToFragment_to_enterPositionFragment)

    }
}