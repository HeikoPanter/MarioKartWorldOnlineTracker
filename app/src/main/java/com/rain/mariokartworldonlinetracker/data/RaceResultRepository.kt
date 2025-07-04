package com.rain.mariokartworldonlinetracker.data

import com.rain.mariokartworldonlinetracker.RaceCategory
import kotlinx.coroutines.flow.Flow

class RaceResultRepository(private val raceResultDao: RaceResultDao) {

    val allRaceResults: Flow<List<RaceResult>> = raceResultDao.getAllRaceResults()

    suspend fun insert(raceResult: RaceResult) {
        raceResultDao.insert(raceResult)
    }
}