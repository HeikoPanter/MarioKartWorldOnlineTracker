package com.rain.mariokartworldonlinetracker.ui.onlinesession.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rain.mariokartworldonlinetracker.KnockoutCupName
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.databinding.FragmentSelectKnockoutCupBinding
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModel
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModelFactory
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

        binding.buttonKoGr.setOnClickListener {
            navigateNext(KnockoutCupName.GR1)
        }

        binding.buttonKoIr.setOnClickListener {
            navigateNext(KnockoutCupName.IR2)
        }

        binding.buttonKoMr.setOnClickListener {
            navigateNext(KnockoutCupName.MR3)
        }
    }

    fun navigateNext(knockoutCupName: KnockoutCupName) {
        newOnlineSessionViewModel.setKnockoutCupName(knockoutCupName)
        findNavController().navigate(R.id.action_selectKnockoutCupFragment_to_selectPositionFragment)
    }
}
