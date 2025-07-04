package com.rain.mariokartworldonlinetracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rain.mariokartworldonlinetracker.RaceCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface RaceResultDao {
    @Insert
    suspend fun insert(raceResult: RaceResult)

    @Query("SELECT * FROM race_results ORDER BY creationDate DESC")
    fun getAllRaceResults(): Flow<List<RaceResult>> // Flow für reaktive Updates der UI
}