package com.rain.mariokartworldonlinetracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rain.mariokartworldonlinetracker.TrackName
import com.rain.mariokartworldonlinetracker.data.pojo.MostPlayedRaceRoute
import kotlinx.coroutines.flow.Flow

@Dao
interface RaceResultDao {
    @Insert
    suspend fun insert(raceResult: RaceResult)

    @Query("""
        SELECT count(rr.id)
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = 'RACE'
    """)
    fun getRaceCountTotal(): Flow<Int>

    @Query("""
        SELECT count(rr.id)
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = 'RACE' and rr.drivingFromTrackName is null
    """)
    fun getRaceCountThreelap(): Flow<Int>

    @Query("""
        SELECT count(rr.id)
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = 'RACE' and rr.drivingFromTrackName is not null
    """)
    fun getRaceCountRoute(): Flow<Int>

    @Query("""
        SELECT COUNT(rr.id) as races_per_session
        FROM race_results rr
        INNER JOIN online_sessions os ON os.id = rr.onlineSessionId
        WHERE os.category = 'RACE' 
        GROUP BY os.id
    """)
    fun getRaceCountPerSessionTotal(): Flow<List<Int>>

    @Query("""
        SELECT SUM(CASE WHEN rr.drivingFromTrackName is null THEN 1 ELSE 0 END) as races_per_session
        FROM race_results rr
        INNER JOIN online_sessions os ON os.id = rr.onlineSessionId
        WHERE os.category = 'RACE'
        GROUP BY os.id
    """)
    fun getRaceCountPerSessionThreelap(): Flow<List<Int>>

    @Query("""
        SELECT SUM(CASE WHEN rr.drivingFromTrackName is not null THEN 1 ELSE 0 END) as races_per_session
        FROM race_results rr
        INNER JOIN online_sessions os ON os.id = rr.onlineSessionId
        WHERE os.category = 'RACE'
        GROUP BY os.id
    """)
    fun getRaceCountPerSessionRoute(): Flow<List<Int>>

    @Query("""
        SELECT AVG(rr.position) as average_position
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = 'RACE'
    """)
    fun getAveragePositionTotal(): Flow<Int>

    @Query("""
        SELECT AVG(rr.position) as average_position
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = 'RACE' and rr.drivingFromTrackName is null
    """)
    fun getAveragePositionThreelap(): Flow<Int>

    @Query("""
        SELECT AVG(rr.position) as average_position
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = 'RACE' and rr.drivingFromTrackName is not null
    """)
    fun getAveragePositionRoute(): Flow<Int>

    @Query("""
        SELECT drivingToTrackName 
        FROM race_results
        WHERE drivingFromTrackName IS NULL AND drivingToTrackName IS NOT NULL -- Nur gültige Namen zählen
        GROUP BY drivingToTrackName
        ORDER BY
            COUNT(drivingToTrackName) DESC,
            MIN(creationDate) ASC
        LIMIT 1
    """)
    fun getMostFrequentThreelapTrackName(): Flow<TrackName>

    @Query("""
        SELECT 
            drivingFromTrackName as drivingFromTrackName, 
            drivingToTrackName as drivingToTrackName, 
            COUNT(*) as frequency
        FROM 
            race_results
        WHERE 
            drivingFromTrackName IS NOT NULL 
            AND drivingToTrackName IS NOT NULL
        GROUP BY 
            drivingFromTrackName, drivingToTrackName
        ORDER BY 
            frequency DESC,
            MIN(creationDate) ASC
        LIMIT 1
    """)
    fun getMostFrequentRouteTrackName(): Flow<MostPlayedRaceRoute>
}