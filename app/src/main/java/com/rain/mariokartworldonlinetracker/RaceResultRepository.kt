package com.rain.mariokartworldonlinetracker

import kotlinx.coroutines.flow.Flow // Für asynchrone Updates

class RaceResultRepository(private val raceResultDao: RaceResultDao) {

    val allRaceResults: Flow<List<RaceResult>> = raceResultDao.getAllRaceResults()

    suspend fun insert(raceResult: RaceResult) {
        raceResultDao.insert(raceResult)
    }

    suspend fun update(raceResult: RaceResult) {
        raceResultDao.update(raceResult)
    }

    suspend fun delete(raceResult: RaceResult) {
        raceResultDao.delete(raceResult)
    }

    fun getRaceResultById(raceId: Long): Flow<RaceResult?> {
        return raceResultDao.getRaceResultById(raceId)
    }

    fun getRaceResultsByCategory(category: RaceCategory): Flow<List<RaceResult>> {
        return raceResultDao.getRaceResultsByCategory(category)
    }

    fun getLastDrivingToTrackName(): String? {
        return raceResultDao.getLastDrivingToTrackName()
    }
}