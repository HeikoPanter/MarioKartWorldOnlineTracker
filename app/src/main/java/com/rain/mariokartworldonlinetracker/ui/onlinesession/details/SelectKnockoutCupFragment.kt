package com.rain.mariokartworldonlinetracker.ui.onlinesession.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rain.mariokartworldonlinetracker.KnockoutCupName
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.MkwotSettings
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.TrackAndKnockoutHelper
import com.rain.mariokartworldonlinetracker.databinding.FragmentSelectKnockoutCupBinding
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModel
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModelProvider

class SelectKnockoutCupFragment : Fragment() {

    private var _binding: FragmentSelectKnockoutCupBinding? = null
    private lateinit var newOnlineSessionViewModel: NewOnlineSessionViewModel

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSelectKnockoutCupBinding.inflate(inflater, container, false)
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

        val cupOptions: List<KnockoutCupName> = TrackAndKnockoutHelper.getKnockoutCups()
        val itemsPerRow = MkwotSettings.itemsPerRow
        val imageMarginDp = 4
        val imageMarginPx = (imageMarginDp * resources.displayMetrics.density).toInt()

        val selectKnockoutCupClickHandler = { cupName: KnockoutCupName ->
            navigateNext(cupName)
        }

        TrackAndKnockoutHelper.populateImageRows(
            requireContext(),
            cupOptions,
            itemsPerRow,
            binding.imageViewContainer,
            imageMarginPx,
            TrackAndKnockoutHelper::createStandardKnockoutCupImageView,
            selectKnockoutCupClickHandler
        )
    }

    fun navigateNext(knockoutCupName: KnockoutCupName) {
        newOnlineSessionViewModel.setKnockoutCupName(knockoutCupName)
        findNavController().navigate(R.id.action_selectKnockoutCupFragment_to_selectPositionFragment)
    }
}
