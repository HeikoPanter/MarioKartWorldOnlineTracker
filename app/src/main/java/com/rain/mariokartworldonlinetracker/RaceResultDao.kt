package com.rain.mariokartworldonlinetracker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow // Für asynchrone Updates

@Dao
interface RaceResultDao {
    @Insert
    suspend fun insert(raceResult: RaceResult) // suspend für Coroutines

    @Update
    suspend fun update(raceResult: RaceResult)

    @Delete
    suspend fun delete(raceResult: RaceResult)

    @Query("SELECT * FROM race_results ORDER BY date DESC")
    fun getAllRaceResults(): Flow<List<RaceResult>> // Flow für reaktive Updates der UI

    @Query("SELECT * FROM race_results WHERE id = :raceId")
    fun getRaceResultById(raceId: Long): Flow<RaceResult?>

    @Query("SELECT * FROM race_results WHERE category = :category ORDER BY date DESC")
    fun getRaceResultsByCategory(category: RaceCategory): Flow<List<RaceResult>>

    @Query("SELECT drivingToTrackName FROM race_results WHERE category = 'RACE' ORDER BY date DESC LIMIT 1")
    fun getLastDrivingToTrackName(): String?
}