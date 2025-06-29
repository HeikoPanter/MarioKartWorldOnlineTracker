package com.rain.mariokartworldonlinetracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rain.mariokartworldonlinetracker.RaceResult
import com.rain.mariokartworldonlinetracker.RaceResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: RaceResultRepository) : ViewModel() {

    fun insertRaceResult(raceResult: RaceResult) = viewModelScope.launch {
        repository.insert(raceResult)
    }
}

// Factory, um das ViewModel mit dem Repository zu erstellen
class HomeViewModelFactory(private val repository: RaceResultRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}