import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.rain.mariokartworldonlinetracker.RaceResult // Ihre Entitätsklasse
import com.rain.mariokartworldonlinetracker.RaceResultRepository // Ihr Repository

class StatisticsViewModel(private val repository: RaceResultRepository) : ViewModel() {

    // Wandelt den Flow in LiveData um, wenn Sie LiveData in Ihrem Fragment bevorzugen.
    // LiveData ist Lifecycle-aware und gut für die UI.
    val allRaceResultsLiveData = repository.allRaceResults.asLiveData()
}

class StatisticsViewModelFactory(private val repository: RaceResultRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatisticsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}