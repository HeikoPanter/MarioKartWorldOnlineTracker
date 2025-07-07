package com.rain.mariokartworldonlinetracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rain.mariokartworldonlinetracker.RaceCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface OnlineSessionDao {
    @Insert
    suspend fun insert(onlineSession: OnlineSession): Long

    @Query("SELECT count(id) FROM online_sessions WHERE category = :raceCategory")
    fun getSessionCount(raceCategory: RaceCategory): Flow<Int>
}