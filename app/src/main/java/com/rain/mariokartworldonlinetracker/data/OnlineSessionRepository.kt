package com.rain.mariokartworldonlinetracker.data

import com.rain.mariokartworldonlinetracker.RaceCategory
import kotlinx.coroutines.flow.Flow

class OnlineSessionRepository(private val onlineSessionDao: OnlineSessionDao) {

    fun getSessionCount(raceCategory: RaceCategory): Flow<Int> {
        return onlineSessionDao.getSessionCount(raceCategory)
    }

    suspend fun insert(onlineSession: OnlineSession): Long {
        return onlineSessionDao.insert(onlineSession)
    }
}