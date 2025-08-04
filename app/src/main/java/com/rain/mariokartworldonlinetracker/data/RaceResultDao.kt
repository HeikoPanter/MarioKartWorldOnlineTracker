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
        SELECT rr.drivingToTrackName as drivingToTrackName, COUNT(rr.drivingToTrackName) as amountOfRaces, AVG(rr.position) as averagePosition
        FROM race_results rr
        LEFT JOIN online_sessions os on rr.onlineSessionId = os.id
        WHERE os.category = 'RACE' AND drivingFromTrackName IS NULL AND drivingToTrackName IS NOT NULL
        GROUP BY drivingToTrackName
    """)
    fun getThreeLapTrackDetailedDataList(): Flow<List<ThreeLapTrackDetailedData>>

    @Query("""
        SELECT 
            rr.drivingFromTrackName as drivingFromTrackName, 
            rr.drivingToTrackName as drivingToTrackName, 
            COUNT(*) as amountOfRaces,
            AVG(rr.position) as averagePosition
        FROM 
            race_results rr
        LEFT JOIN online_sessions os on rr.onlineSessionId = os.id
        WHERE 
            os.category = 'RACE' AND
            drivingFromTrackName IS NOT NULL 
            AND drivingToTrackName IS NOT NULL
        GROUP BY 
            drivingFromTrackName, drivingToTrackName
    """)
    fun getRouteDetailedDataList(): Flow<List<RouteDetailedData>>
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
        SELECT rr.drivingToTrackName as drivingToTrackName,
            COUNT(*) as amountOfRaces,
            AVG(rr.position) as averagePosition
        FROM race_results rr
        LEFT JOIN online_sessions os on rr.onlineSessionId = os.id
        WHERE os.category = 'RACE_VS' AND drivingFromTrackName IS NULL AND drivingToTrackName IS NOT NULL
        GROUP BY drivingToTrackName
    """)
    fun getVsThreeLapTrackDetailedDataList(): Flow<List<ThreeLapTrackDetailedData>>

    @Query("""
        SELECT 
            rr.drivingFromTrackName as drivingFromTrackName, 
            rr.drivingToTrackName as drivingToTrackName, 
            COUNT(*) as amountOfRaces,
            AVG(rr.position) as averagePosition
        FROM 
            race_results rr
        LEFT JOIN online_sessions os on rr.onlineSessionId = os.id
        WHERE 
            os.category = 'RACE_VS'
            AND drivingFromTrackName IS NOT NULL 
            AND drivingToTrackName IS NOT NULL
        GROUP BY 
            drivingFromTrackName, drivingToTrackName
    """)
    fun getVsRouteDetailedDataList(): Flow<List<RouteDetailedData>>
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
        SELECT rr.knockoutCupName as knockoutCupName,
        COUNT(*) as amountOfRaces,
        AVG(rr.position) as averagePosition
        FROM race_results rr
        LEFT JOIN online_sessions os on rr.onlineSessionId = os.id
        WHERE os.category = 'KNOCKOUT' AND rr.knockoutCupName IS NOT NULL -- Nur gültige Namen zählen
        GROUP BY rr.knockoutCupName
    """)
    fun getRallyDetailedDataList(): Flow<List<RallyDetailedData>>
    //</editor-fold>

    //<editor-fold desc="Knockout VS Queries">
    @Query("""
        SELECT count(rr.id)
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = 'KNOCKOUT_VS'
    """)
    fun getKnockoutVsCountTotal(): Flow<Int>

    @Query("""
        SELECT COUNT(rr.id) as races_per_session
        FROM race_results rr
        INNER JOIN online_sessions os ON os.id = rr.onlineSessionId
        WHERE os.category = 'KNOCKOUT_VS' 
        GROUP BY os.id
    """)
    fun getKnockoutVsCountPerSessionTotal(): Flow<List<Int>>

    @Query("""
        SELECT AVG(rr.position) as average_position
        FROM race_results rr
        LEFT JOIN online_sessions os ON rr.onlineSessionId = os.id
        WHERE os.category = 'KNOCKOUT_VS'
    """)
    fun getKnockoutVsAveragePositionTotal(): Flow<Int?>

    @Query("""
        SELECT rr.knockoutCupName as knockoutCupName,
        COUNT(*) as amountOfRaces,
        AVG(rr.position) as averagePosition
        FROM race_results rr
        LEFT JOIN online_sessions os on rr.onlineSessionId = os.id
        WHERE os.category = 'KNOCKOUT_VS' AND rr.knockoutCupName IS NOT NULL -- Nur gültige Namen zählen
        GROUP BY rr.knockoutCupName
    """)
    fun getVsRallyDetailedDataList(): Flow<List<RallyDetailedData>>
    //</editor-fold>

    //<editor-fold desc="Misc queries">
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
    //</editor-fold>
}