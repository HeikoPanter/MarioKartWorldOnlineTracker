package com.rain.mariokartworldonlinetracker.data

import com.rain.mariokartworldonlinetracker.RaceCategory
import kotlinx.coroutines.flow.Flow

class OnlineSessionRepository(private val onlineSessionDao: OnlineSessionDao) {

    val raceSessionCount: Flow<Int> = onlineSessionDao.getSessionCount(RaceCategory.RACE)
    val raceVsSessionCount: Flow<Int> = onlineSessionDao.getSessionCount(RaceCategory.RACE_VS)
    val knockoutSessionCount: Flow<Int> = onlineSessionDao.getSessionCount(RaceCategory.KNOCKOUT)

    suspend fun insert(onlineSession: OnlineSession): Long {
        return onlineSessionDao.insert(onlineSession)
    }
}