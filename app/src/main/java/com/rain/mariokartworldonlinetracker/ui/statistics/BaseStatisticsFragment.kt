package com.rain.mariokartworldonlinetracker.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository

abstract class BaseStatisticsFragment<VB : ViewBinding, VM : BaseStatisticsViewModel<*, *>>(
    protected val raceCategory: RaceCategory) :
    Fragment() {

    private var _binding: VB? = null
    private lateinit var raceResultRepository: RaceResultRepository
    private lateinit var onlineSessionRepository: OnlineSessionRepository

    protected val binding get() = _binding!!
    protected lateinit var statisticsViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialisiere die Repositories einmal hier
        val application = requireActivity().application as MarioKartWorldOnlineTrackerApplication
        raceResultRepository = RaceResultRepository(application.database.raceResultDao())
        onlineSessionRepository = OnlineSessionRepository(application.database.onlineSessionDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createBinding(inflater, container)
        initializeViewModelInternal() // Interne Methode zur ViewModel-Initialisierung
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected abstract fun createBinding(inflater: LayoutInflater, container: ViewGroup?): VB
    protected abstract fun getViewModelClass(): Class<VM>
    protected abstract fun getViewModelFactory(raceCategory: RaceCategory, raceResultRepository: RaceResultRepository, onlineSessionRepository: OnlineSessionRepository): ViewModelProvider.Factory

    private fun initializeViewModelInternal() {
        val factory = getViewModelFactory(
            raceCategory,
            raceResultRepository,
            onlineSessionRepository
        )

        val viewModelClass = getViewModelClass()
        val viewModelKey = raceCategory.name
        statisticsViewModel = ViewModelProvider(
            requireActivity(),
            factory
        )
            .get(
                viewModelKey,
                viewModelClass
            )
    }
}