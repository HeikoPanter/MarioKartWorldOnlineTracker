package com.rain.mariokartworldonlinetracker.ui.onlinesession

import androidx.lifecycle.*
import com.rain.mariokartworldonlinetracker.*
import kotlinx.coroutines.launch

class NewOnlineSessionViewModel(private val repository: RaceResultRepository) : ViewModel() {

    private var _raceCategory: RaceCategory = RaceCategory.UNKNOWN
    private var _raceMode: RaceMode = RaceMode.UNKNOWN

    private val _lastDrivingToTrackName = MutableLiveData<String?>()
    private val _drivingFromOption = MutableLiveData<DrivingFromOption>()

    private val _drivingFromTrackName = MutableLiveData<String?>()
    val drivingFromTrackName: LiveData<String?> = _drivingFromTrackName

    private val _drivingToTrackName = MutableLiveData<String?>()
    val drivingToTrackName: LiveData<String?> = _drivingToTrackName

    private val _knockoutCupName = MutableLiveData<String?>()
    val knockoutCupName: LiveData<String?> = _knockoutCupName

    private val _position = MutableLiveData<Short?>()
    val position: LiveData<Short?> = _position

    init {
        resetSession()
    }

    fun resetSession() {
        setRaceCategory(RaceCategory.UNKNOWN)
        setLastDrivingToTrackName(null)
        resetRace()
    }

    fun resetRace() {
        setPosition(null)
        setDrivingFromOption(DrivingFromOption.UNKNOWN)
        setRaceMode(RaceMode.UNKNOWN)
        setDrivingFromTrackName(null)
        setDrivingToTrackName(null)
        setKnockoutCupName(null)
    }

    fun setRaceCategory(category: RaceCategory) {
        _raceCategory = category
    }

    fun setRaceMode(mode: RaceMode) {
        _raceMode = mode
    }

    fun setLastDrivingToTrackName(name: String?) {
        _lastDrivingToTrackName.value = name
    }

    fun setDrivingFromOption(option: DrivingFromOption) {
        _drivingFromOption.value = option
        when (option) {
            DrivingFromOption.LAST -> setDrivingFromTrackName(_lastDrivingToTrackName.value)
            DrivingFromOption.NONE -> setDrivingFromTrackName(null)
            DrivingFromOption.OTHER -> setDrivingFromTrackName(null)
            DrivingFromOption.UNKNOWN -> setDrivingFromTrackName(null)
        }
    }

    fun setDrivingFromTrackName(name: String?) {
        _drivingFromTrackName.value = name
    }

    fun setDrivingToTrackName(name: String?) {
        _drivingToTrackName.value = name
    }

    fun setKnockoutCupName(name: String?) {
        _knockoutCupName.value = name
    }

    fun setPosition(pos: Short?) {
        _position.value = pos
    }

    fun saveNewRace() {
        val fromTrack = _drivingFromTrackName.value
        val toTrack = _drivingToTrackName.value
        val knockoutCupName = _knockoutCupName.value
        val position = _position.value

        val newRace = RaceResult(
            category = _raceCategory,
            mode = _raceMode,
            drivingFromTrackName = fromTrack,
            drivingToTrackName = toTrack,
            knockoutCupName = knockoutCupName,
            position = position,
            date = System.currentTimeMillis()
        )
        viewModelScope.launch {
            repository.insert(newRace)
        }

        resetRace()
    }
}

class NewOnlineSessionViewModelFactory(private val repository: RaceResultRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewOnlineSessionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewOnlineSessionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
