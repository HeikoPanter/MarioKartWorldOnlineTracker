package com.rain.mariokartworldonlinetracker.ui.onlinesession

import androidx.lifecycle.*
import com.rain.mariokartworldonlinetracker.*
import kotlinx.coroutines.launch

class NewOnlineSessionViewModel(private val repository: RaceResultRepository) : ViewModel() {

    var raceCategory: RaceCategory = RaceCategory.UNKNOWN
    var lastDrivingToTrackName: TrackName? = null

    private var _engineClass: EngineClass = EngineClass.UNKNOWN

    private val _drivingFromOption = MutableLiveData<DrivingFromOption>()

    private val _drivingFromTrackName = MutableLiveData<TrackName?>()
    val drivingFromTrackName: LiveData<TrackName?> = _drivingFromTrackName

    private val _drivingToTrackName = MutableLiveData<TrackName?>()
    val drivingToTrackName: LiveData<TrackName?> = _drivingToTrackName

    private val _knockoutCupName = MutableLiveData<KnockoutCupName?>()
    val knockoutCupName: LiveData<KnockoutCupName?> = _knockoutCupName

    private val _position = MutableLiveData<Short?>()
    val position: LiveData<Short?> = _position

    init {
        resetSession()
    }

    fun resetSession() {
        raceCategory = RaceCategory.UNKNOWN
        lastDrivingToTrackName = null
        resetRace()
    }

    fun resetRace() {
        setPosition(null)
        setDrivingFromOption(DrivingFromOption.UNKNOWN)
        setEngineClass(EngineClass.UNKNOWN)
        setDrivingFromTrackName(null)
        setDrivingToTrackName(null)
        setKnockoutCupName(null)
    }

    fun setEngineClass(engineClass: EngineClass) {
        _engineClass = engineClass
    }

    fun setDrivingFromOption(option: DrivingFromOption) {
        _drivingFromOption.value = option
        when (option) {
            DrivingFromOption.LAST -> setDrivingFromTrackName(lastDrivingToTrackName)
            DrivingFromOption.NONE -> setDrivingFromTrackName(null)
            DrivingFromOption.OTHER -> setDrivingFromTrackName(null)
            DrivingFromOption.UNKNOWN -> setDrivingFromTrackName(null)
        }
    }

    fun setDrivingFromTrackName(name: TrackName?) {
        _drivingFromTrackName.value = name
    }

    fun setDrivingToTrackName(name: TrackName?) {
        _drivingToTrackName.value = name
    }

    fun setKnockoutCupName(name: KnockoutCupName?) {
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
            category = raceCategory,
            engineClass = _engineClass,
            drivingFromTrackName = fromTrack,
            drivingToTrackName = toTrack,
            knockoutCupName = knockoutCupName,
            position = position,
            date = System.currentTimeMillis()
        )
        viewModelScope.launch {
            repository.insert(newRace)
        }

        lastDrivingToTrackName = toTrack
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
