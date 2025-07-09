package com.rain.mariokartworldonlinetracker.ui.onlinesession

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.rain.mariokartworldonlinetracker.*
import com.rain.mariokartworldonlinetracker.data.OnlineSession
import com.rain.mariokartworldonlinetracker.data.OnlineSessionRepository
import com.rain.mariokartworldonlinetracker.data.RaceResult
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import kotlinx.coroutines.launch

class NewOnlineSessionViewModel(
    private val raceResultRepository: RaceResultRepository,
    private val onlineSessionRepository: OnlineSessionRepository
) : ViewModel() {

    private val _saveResultStatus = MutableLiveData<Event<Boolean>>() // Boolean: true für Erfolg, false für Fehler
    val saveResultStatus: LiveData<Event<Boolean>> = _saveResultStatus

    var lastDrivingToTrackName: TrackName? = null

    private var _raceCategory: RaceCategory = RaceCategory.UNKNOWN
    private var _sessionId: Long = 0
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

    fun resetSession(raceCategory: RaceCategory = RaceCategory.UNKNOWN) {
        _raceCategory = raceCategory
        lastDrivingToTrackName = null
        _sessionId = 0
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

    fun getRaceCategory(): RaceCategory {
        return _raceCategory
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

        viewModelScope.launch {
            try {
                if (_sessionId == 0.toLong()) {
                    var newSession = OnlineSession(
                        creationDate = System.currentTimeMillis(),
                        category = _raceCategory
                    )

                    _sessionId = onlineSessionRepository.insert(newSession)
                }

                val newRace = RaceResult(
                    engineClass = _engineClass,
                    drivingFromTrackName = fromTrack,
                    drivingToTrackName = toTrack,
                    knockoutCupName = knockoutCupName,
                    position = position,
                    creationDate = System.currentTimeMillis(),
                    onlineSessionId = _sessionId
                )

                raceResultRepository.insert(newRace)
                _saveResultStatus.value = Event(true)
            } catch (e: Exception) {
                // Loggen Sie den Fehler
                Log.e("NewOnlineSessionViewModel", "Error saving race result", e)
                _saveResultStatus.value = Event(false) // Fehler signalisieren
            }

            lastDrivingToTrackName = toTrack
            resetRace()
        }
    }
}

// Hilfsklasse für einmalige Events mit LiveData (um das Popup nicht bei Konfigurationsänderungen erneut anzuzeigen)
open class Event<out T>(private val content: T) {
    var hasBeenHandled = false
        private set // Allow external read but not write

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}

class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>) {
        event?.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }

}

class NewOnlineSessionViewModelFactory(
    private val raceResultRepository: RaceResultRepository,
    private val onlineSessionRepository: OnlineSessionRepository
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewOnlineSessionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewOnlineSessionViewModel(raceResultRepository, onlineSessionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

object NewOnlineSessionViewModelProvider {
    fun getViewModel(app: MarioKartWorldOnlineTrackerApplication, activity: FragmentActivity): NewOnlineSessionViewModel {
        val raceResultDao = app.database.raceResultDao()
        val onlineSessionDao = app.database.onlineSessionDao()
        val repository = RaceResultRepository(raceResultDao)
        val onlineSessionRepository = OnlineSessionRepository(onlineSessionDao)

        return ViewModelProvider(
            activity,
            NewOnlineSessionViewModelFactory(repository, onlineSessionRepository))
            .get(NewOnlineSessionViewModel::class.java)
    }
}
