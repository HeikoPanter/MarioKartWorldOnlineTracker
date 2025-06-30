package com.rain.mariokartworldonlinetracker.ui.home

import androidx.lifecycle.*
import com.rain.mariokartworldonlinetracker.* // Ihre Enums und Entitäten
import kotlinx.coroutines.launch

class NewRaceViewModel(private val repository: RaceResultRepository) : ViewModel() {

    val raceCategory: RaceCategory = RaceCategory.RACE // Standardwert
    val raceMode: RaceMode = RaceMode._150CC // Standardwert

    private val _drivingFromOption = MutableLiveData<DrivingFromOption>()
    // Kein öffentliches LiveData für die Option selbst, wenn sie nur intern verwendet wird

    private val _drivingFromTrackName = MutableLiveData<String?>()
    val drivingFromTrackName: LiveData<String?> = _drivingFromTrackName

    private val _drivingToTrackName = MutableLiveData<String?>()
    val drivingToTrackName: LiveData<String?> = _drivingToTrackName

    private val _knockoutCupName = MutableLiveData<String?>()
    val knockoutCupName: LiveData<String?> = _knockoutCupName

    private val _position = MutableLiveData<Short?>()
    val position: LiveData<Short?> = _position

    // Zum Abrufen des letzten "drivingToTrackName"
    private val _lastDrivingToTrackName = MutableLiveData<String?>()
    val lastDrivingToTrackName: LiveData<String?> = _lastDrivingToTrackName

    init {
        // Beispiel: Laden des letzten "drivingToTrackName" beim Initialisieren
        // Dies erfordert eine entsprechende Methode im DAO/Repository
         viewModelScope.launch {
             _lastDrivingToTrackName.value = repository.getLastDrivingToTrackName()
         }
    }

    fun setDrivingFromOption(option: DrivingFromOption) {
        _drivingFromOption.value = option
        when (option) {
            DrivingFromOption.LAST -> _drivingFromTrackName.value = _lastDrivingToTrackName.value // Setzen, wenn verfügbar
            DrivingFromOption.NONE -> _drivingFromTrackName.value = null // Oder ein spezifischer "None"-String
            DrivingFromOption.OTHER -> _drivingFromTrackName.value = null // Wird später gesetzt oder erfordert weitere UI
        }
    }

    fun setDrivingFromTrackNameManually(name: String?) { // Für "Other" oder direkte Eingabe
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

    fun saveNewRace(): Boolean {
        val fromTrack = _drivingFromTrackName.value // Kann null sein, je nach Logik
        val toTrack = _drivingToTrackName.value
        val knockoutCupName = _knockoutCupName.value
        val currentPosition = _position.value

        val newRace = RaceResult(
            category = raceCategory, // Aus dem ViewModel
            mode = raceMode,         // Aus dem ViewModel
            drivingFromTrackName = fromTrack,
            drivingToTrackName = toTrack,
            knockoutCupName = knockoutCupName,
            position = currentPosition,
            date = System.currentTimeMillis()
        )
        viewModelScope.launch {
            repository.insert(newRace)
        }
        return true // Erfolg
    }
}

class NewRaceViewModelFactory(private val repository: RaceResultRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewRaceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewRaceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
