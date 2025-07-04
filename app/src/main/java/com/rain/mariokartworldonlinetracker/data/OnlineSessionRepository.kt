package com.rain.mariokartworldonlinetracker.data

import com.rain.mariokartworldonlinetracker.RaceCategory
import kotlinx.coroutines.flow.Flow

class OnlineSessionRepository(private val onlineSessionDao: OnlineSessionDao) {

    suspend fun insert(onlineSession: OnlineSession): Long {
        return onlineSessionDao.insert(onlineSession)
    }
}