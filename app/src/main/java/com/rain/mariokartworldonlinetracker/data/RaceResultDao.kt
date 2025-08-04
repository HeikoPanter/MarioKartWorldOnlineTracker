package com.rain.mariokartworldonlinetracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rain.mariokartworldonlinetracker.KnockoutCupName
import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.TrackName
import com.rain.mariokartworldonlinetracker.data.pojo.RallyDetailedData
import com.rain.mariokartworldonlinetracker.data.pojo.ResultHistory
import com.rain.mariokartworldonlinetracker.data.pojo.RouteDetailedData
import com.rain.mariokartworldonlinetracker.data.pojo.ThreeLapTrackDetailedData
import kotlinx.coroutines.flow.Flow

@Dao
interface RaceResultDao {
    @Insert
    suspend fun insert(raceResult: RaceResult)

    @Query("""
        SELECT count(rr.id)
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = :raceCategory
    """)
    fun getCountTotal(raceCategory: RaceCategory): Flow<Int>

    @Query("""
        SELECT count(rr.id)
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = :raceCategory 
            and rr.drivingFromTrackName is null
            and rr.drivingToTrackName is not null
            and rr.knockoutCupName is null
    """)
    fun getCountThreelap(raceCategory: RaceCategory): Flow<Int>

    @Query("""
        SELECT count(rr.id)
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = :raceCategory
            and rr.drivingFromTrackName is not null
            and rr.drivingToTrackName is not null
            and rr.knockoutCupName is null            
    """)
    fun getCountRoute(raceCategory: RaceCategory): Flow<Int>

    @Query("""
        SELECT COUNT(rr.id) as races_per_session
        FROM race_results rr
        INNER JOIN online_sessions os ON os.id = rr.onlineSessionId
        WHERE os.category = :raceCategory
        GROUP BY os.id
    """)
    fun getCountPerSessionTotal(raceCategory: RaceCategory): Flow<List<Int>>

    @Query("""
        SELECT SUM(CASE WHEN rr.drivingFromTrackName is null THEN 1 ELSE 0 END) as races_per_session
        FROM race_results rr
        INNER JOIN online_sessions os ON os.id = rr.onlineSessionId
        WHERE os.category = :raceCategory
            and rr.drivingFromTrackName is null
            and rr.drivingToTrackName is not null
            and rr.knockoutCupName is null
        GROUP BY os.id
    """)
    fun getCountPerSessionThreelap(raceCategory: RaceCategory): Flow<List<Int>>

    @Query("""
        SELECT SUM(CASE WHEN rr.drivingFromTrackName is not null THEN 1 ELSE 0 END) as races_per_session
        FROM race_results rr
        INNER JOIN online_sessions os ON os.id = rr.onlineSessionId
        WHERE os.category = :raceCategory
            and rr.drivingFromTrackName is not null
            and rr.drivingToTrackName is not null
            and rr.knockoutCupName is null
        GROUP BY os.id
    """)
    fun getCountPerSessionRoute(raceCategory: RaceCategory): Flow<List<Int>>

    @Query("""
        SELECT AVG(rr.position) as average_position
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = :raceCategory
    """)
    fun getAveragePositionTotal(raceCategory: RaceCategory): Flow<Int?>

    @Query("""
        SELECT AVG(rr.position) as average_position
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = :raceCategory
            and rr.drivingFromTrackName is null
            and rr.drivingToTrackName is not null
            and rr.knockoutCupName is null
    """)
    fun getAveragePositionThreelap(raceCategory: RaceCategory): Flow<Int?>

    @Query("""
        SELECT AVG(rr.position) as average_position
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = :raceCategory 
            and rr.drivingFromTrackName is not null
            and rr.drivingToTrackName is not null
            and rr.knockoutCupName is null
    """)
    fun getAveragePositionRoute(raceCategory: RaceCategory): Flow<Int?>

    @Query("""
        SELECT rr.drivingToTrackName as name, COUNT(rr.drivingToTrackName) as amountOfRaces, AVG(rr.position) as averagePosition
        FROM race_results rr
        LEFT JOIN online_sessions os on rr.onlineSessionId = os.id
        WHERE os.category = :raceCategory 
            AND drivingFromTrackName IS NULL 
            AND drivingToTrackName IS NOT NULL
            AND knockoutCupName IS NULL
        GROUP BY drivingToTrackName
    """)
    fun getThreeLapTrackDetailedDataList(raceCategory: RaceCategory): Flow<List<ThreeLapTrackDetailedData>>

    @Query("""
        SELECT 
            rr.drivingFromTrackName as name, 
            rr.drivingToTrackName as drivingToTrackName, 
            COUNT(*) as amountOfRaces,
            AVG(rr.position) as averagePosition
        FROM 
            race_results rr
        LEFT JOIN online_sessions os on rr.onlineSessionId = os.id
        WHERE 
            os.category = :raceCategory
            AND drivingFromTrackName IS NOT NULL
            AND drivingToTrackName IS NOT NULL
            AND knockoutCupName IS NULL
        GROUP BY 
            drivingFromTrackName, drivingToTrackName
    """)
    fun getRouteDetailedDataList(raceCategory: RaceCategory): Flow<List<RouteDetailedData>>

    @Query("""
        SELECT rr.knockoutCupName as name,
        COUNT(*) as amountOfRaces,
        AVG(rr.position) as averagePosition
        FROM race_results rr
        LEFT JOIN online_sessions os on rr.onlineSessionId = os.id
        WHERE os.category = :raceCategory
            AND rr.drivingFromTrackName IS NULL
            AND rr.drivingToTrackName IS NULL
            AND rr.knockoutCupName IS NOT NULL -- Nur gültige Namen zählen
        GROUP BY rr.knockoutCupName
    """)
    fun getRallyDetailedDataList(raceCategory: RaceCategory): Flow<List<RallyDetailedData>>

    @Query("""
        SELECT
         rr.id as id,
         rr.creationDate as creationDate,
         rr.drivingFromTrackName as drivingFromTrackName,
         rr.drivingToTrackName as drivingToTrackName,
         rr.engineClass as engineClass,
         rr.knockoutCupName as knockoutCupName,
         rr.position as position,
         rr.onlineSessionId as onlineSessionId,
         os.creationDate as onlineSessionCreationDate,
         os.category as onlineSessionCategory
        FROM
            race_results rr
        LEFT JOIN
            online_sessions os
        ON rr.onlineSessionId = os.id
        ORDER BY
            creationDate DESC
    """)
    suspend fun getResultHistory(): List<ResultHistory>

    @Query("""
        SELECT
         rr.id as id,
         rr.creationDate as creationDate,
         rr.drivingFromTrackName as drivingFromTrackName,
         rr.drivingToTrackName as drivingToTrackName,
         rr.engineClass as engineClass,
         rr.knockoutCupName as knockoutCupName,
         rr.position as position,
         rr.onlineSessionId as onlineSessionId,
         os.creationDate as onlineSessionCreationDate,
         os.category as onlineSessionCategory
        FROM
            race_results rr
        LEFT JOIN
            online_sessions os
        ON rr.onlineSessionId = os.id
        WHERE os.category = :raceCategory
        ORDER BY
            creationDate DESC
    """)
    fun getResultHistory(raceCategory: RaceCategory) : Flow<List<ResultHistory>>
}