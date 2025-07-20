package com.rain.mariokartworldonlinetracker.ui.onlinesession.details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.EngineClass
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.TrackAndKnockoutHelper
import com.rain.mariokartworldonlinetracker.databinding.FragmentSelectEngineClassBinding
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModel
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModelFactory
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModelProvider

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

        newOnlineSessionViewModel = NewOnlineSessionViewModelProvider.getViewModel(
            requireActivity().application as MarioKartWorldOnlineTrackerApplication,
            requireActivity()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageMarginDp = 4
        val imageMarginPx = (imageMarginDp * resources.displayMetrics.density).toInt()
        val isRace = newOnlineSessionViewModel.getRaceCategory() == RaceCategory.RACE || newOnlineSessionViewModel.getRaceCategory() == RaceCategory.RACE_VS

        var button150cc = TrackAndKnockoutHelper.createImageView(
            requireContext(),
            "150cc",
            if (isRace) R.drawable.engine150cc_race else R.drawable.engine150cc_knockout,
            imageMarginPx
        )

        var buttonMirror = TrackAndKnockoutHelper.createImageView(
            requireContext(),
            "Mirror",
            if (isRace) R.drawable.enginemirror_race else R.drawable.enginemirror_knockout,
            imageMarginPx
        )

        var button100cc = TrackAndKnockoutHelper.createImageView(
            requireContext(),
            "100cc",
            if (isRace) R.drawable.engine100cc_race else R.drawable.engine100cc_knockout,
            imageMarginPx
        )

        button150cc.setOnClickListener {
            navigateNext(EngineClass._150CC)
        }

        buttonMirror.setOnClickListener {
            navigateNext(EngineClass.MIRROR)
        }

        button100cc.setOnClickListener {
            navigateNext(EngineClass._100CC)
        }

        binding.layout150.addView(button150cc)
        binding.layoutMirror.addView(buttonMirror)
        binding.layout100.addView(button100cc)
    }

    fun navigateNext(engineClass: EngineClass) {
        newOnlineSessionViewModel.setEngineClass(engineClass)
        if (newOnlineSessionViewModel.getRaceCategory() == RaceCategory.RACE || newOnlineSessionViewModel.getRaceCategory() == RaceCategory.RACE_VS) {
            findNavController().navigate(R.id.action_selectEngineClassFragment_to_selectDrivingFromFragment)
        }
        else if (newOnlineSessionViewModel.getRaceCategory() == RaceCategory.KNOCKOUT_VS) {
            findNavController().navigate(R.id.action_selectEngineClassFragment_to_selectKnockoutCupFragment)
        }
    }
}