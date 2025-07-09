package com.rain.mariokartworldonlinetracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rain.mariokartworldonlinetracker.KnockoutCupName
import com.rain.mariokartworldonlinetracker.TrackName
import com.rain.mariokartworldonlinetracker.data.pojo.MostPlayedRaceRoute
import com.rain.mariokartworldonlinetracker.data.pojo.ResultHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface RaceResultDao {
    @Insert
    suspend fun insert(raceResult: RaceResult)

    //<editor-fold desc="Race Queries">
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
    fun getAveragePositionTotal(): Flow<Int?>

    @Query("""
        SELECT AVG(rr.position) as average_position
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = 'RACE' and rr.drivingFromTrackName is null
    """)
    fun getAveragePositionThreelap(): Flow<Int?>

    @Query("""
        SELECT AVG(rr.position) as average_position
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = 'RACE' and rr.drivingFromTrackName is not null
    """)
    fun getAveragePositionRoute(): Flow<Int?>

    @Query("""
        SELECT drivingToTrackName 
        FROM race_results rr
        LEFT JOIN online_sessions os on rr.onlineSessionId = os.id
        WHERE os.category = 'RACE' AND drivingFromTrackName IS NULL AND drivingToTrackName IS NOT NULL
        GROUP BY drivingToTrackName
        ORDER BY
            COUNT(drivingToTrackName) DESC,
            MIN(rr.creationDate) ASC
        LIMIT 1
    """)
    fun getMostFrequentThreelapTrackName(): Flow<TrackName?>

    @Query("""
        SELECT 
            drivingFromTrackName as drivingFromTrackName, 
            drivingToTrackName as drivingToTrackName, 
            COUNT(*) as frequency
        FROM 
            race_results rr
        LEFT JOIN online_sessions os on rr.onlineSessionId = os.id
        WHERE 
            os.category = 'RACE' AND
            drivingFromTrackName IS NOT NULL 
            AND drivingToTrackName IS NOT NULL
        GROUP BY 
            drivingFromTrackName, drivingToTrackName
        ORDER BY 
            frequency DESC,
            MIN(rr.creationDate) ASC
        LIMIT 1
    """)
    fun getMostFrequentRouteTrackName(): Flow<MostPlayedRaceRoute?>
    //</editor-fold>

    //<editor-fold desc="Race VS Queries">
    @Query("""
        SELECT count(rr.id)
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = 'RACE_VS'
    """)
    fun getRaceVsCountTotal(): Flow<Int>

    @Query("""
        SELECT count(rr.id)
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = 'RACE_VS' and rr.drivingFromTrackName is null
    """)
    fun getRaceVsCountThreelap(): Flow<Int>

    @Query("""
        SELECT count(rr.id)
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = 'RACE_VS' and rr.drivingFromTrackName is not null
    """)
    fun getRaceVsCountRoute(): Flow<Int>

    @Query("""
        SELECT COUNT(rr.id) as races_per_session
        FROM race_results rr
        INNER JOIN online_sessions os ON os.id = rr.onlineSessionId
        WHERE os.category = 'RACE_VS' 
        GROUP BY os.id
    """)
    fun getRaceVsCountPerSessionTotal(): Flow<List<Int>>

    @Query("""
        SELECT SUM(CASE WHEN rr.drivingFromTrackName is null THEN 1 ELSE 0 END) as races_per_session
        FROM race_results rr
        INNER JOIN online_sessions os ON os.id = rr.onlineSessionId
        WHERE os.category = 'RACE_VS'
        GROUP BY os.id
    """)
    fun getRaceVsCountPerSessionThreelap(): Flow<List<Int>>

    @Query("""
        SELECT SUM(CASE WHEN rr.drivingFromTrackName is not null THEN 1 ELSE 0 END) as races_per_session
        FROM race_results rr
        INNER JOIN online_sessions os ON os.id = rr.onlineSessionId
        WHERE os.category = 'RACE_VS'
        GROUP BY os.id
    """)
    fun getRaceVsCountPerSessionRoute(): Flow<List<Int>>

    @Query("""
        SELECT AVG(rr.position) as average_position
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = 'RACE_VS'
    """)
    fun getAverageRaceVsPositionTotal(): Flow<Int?>

    @Query("""
        SELECT AVG(rr.position) as average_position
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = 'RACE_VS' and rr.drivingFromTrackName is null
    """)
    fun getAverageRaceVsPositionThreelap(): Flow<Int?>

    @Query("""
        SELECT AVG(rr.position) as average_position
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = 'RACE_VS' and rr.drivingFromTrackName is not null
    """)
    fun getAverageRaceVsPositionRoute(): Flow<Int?>

    @Query("""
        SELECT drivingToTrackName 
        FROM race_results rr
        LEFT JOIN online_sessions os on rr.onlineSessionId = os.id
        WHERE os.category = 'RACE_VS' AND drivingFromTrackName IS NULL AND drivingToTrackName IS NOT NULL
        GROUP BY drivingToTrackName
        ORDER BY
            COUNT(drivingToTrackName) DESC,
            MIN(rr.creationDate) ASC
        LIMIT 1
    """)
    fun getMostFrequentThreelapVsTrackName(): Flow<TrackName?>

    @Query("""
        SELECT 
            drivingFromTrackName as drivingFromTrackName, 
            drivingToTrackName as drivingToTrackName, 
            COUNT(*) as frequency
        FROM 
            race_results rr
        LEFT JOIN online_sessions os on rr.onlineSessionId = os.id
        WHERE 
            os.category = 'RACE_VS'
            AND drivingFromTrackName IS NOT NULL 
            AND drivingToTrackName IS NOT NULL
        GROUP BY 
            drivingFromTrackName, drivingToTrackName
        ORDER BY 
            frequency DESC,
            MIN(rr.creationDate) ASC
        LIMIT 1
    """)
    fun getMostFrequentRouteVsTrackName(): Flow<MostPlayedRaceRoute?>
    //</editor-fold>

    //<editor-fold desc="Knockout Queries">
    @Query("""
        SELECT count(rr.id)
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = 'KNOCKOUT'
    """)
    fun getKnockoutCountTotal(): Flow<Int>

    @Query("""
        SELECT COUNT(rr.id) as races_per_session
        FROM race_results rr
        INNER JOIN online_sessions os ON os.id = rr.onlineSessionId
        WHERE os.category = 'KNOCKOUT' 
        GROUP BY os.id
    """)
    fun getKnockoutCountPerSessionTotal(): Flow<List<Int>>

    @Query("""
        SELECT AVG(rr.position) as average_position
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = 'KNOCKOUT'
    """)
    fun getKnockoutAveragePositionTotal(): Flow<Int?>

    @Query("""
        SELECT knockoutCupName 
        FROM race_results
        WHERE knockoutCupName IS NOT NULL -- Nur gültige Namen zählen
        GROUP BY knockoutCupName
        ORDER BY
            COUNT(knockoutCupName) DESC,
            MIN(creationDate) ASC
        LIMIT 1
    """)
    fun getMostFrequentKnockoutCupName(): Flow<KnockoutCupName?>
    //</editor-fold>

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
}