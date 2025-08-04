package com.rain.mariokartworldonlinetracker.data

import com.rain.mariokartworldonlinetracker.RaceCategory
import com.rain.mariokartworldonlinetracker.data.pojo.RallyDetailedData
import com.rain.mariokartworldonlinetracker.data.pojo.ResultHistory
import com.rain.mariokartworldonlinetracker.data.pojo.RouteDetailedData
import com.rain.mariokartworldonlinetracker.data.pojo.ThreeLapTrackDetailedData
import kotlinx.coroutines.flow.Flow

class RaceResultRepository(private val raceResultDao: RaceResultDao) {

    fun getThreeLapTrackDetailedDataList(raceCategory: RaceCategory): Flow<List<ThreeLapTrackDetailedData>> {
        return raceResultDao.getThreeLapTrackDetailedDataList(raceCategory)
    }

    fun getRouteDetailedDataList(raceCategory: RaceCategory): Flow<List<RouteDetailedData>> {
        return raceResultDao.getRouteDetailedDataList(raceCategory)
    }

    fun getRallyDetailedDataList(raceCategory: RaceCategory): Flow<List<RallyDetailedData>> {
        return raceResultDao.getRallyDetailedDataList(raceCategory)
    }

    suspend fun insert(raceResult: RaceResult) {
        raceResultDao.insert(raceResult)
    }

    suspend fun getResultHistory(): List<ResultHistory> {
        return raceResultDao.getResultHistory()
    }

    fun getResultHistory(raceCategory: RaceCategory): Flow<List<ResultHistory>> {
        return raceResultDao.getResultHistory(raceCategory)
    }

    fun getCountTotal(raceCategory: RaceCategory): Flow<Int> {
        return raceResultDao.getCountTotal(raceCategory)
    }

    fun getCountThreelap(raceCategory: RaceCategory): Flow<Int> {
        return raceResultDao.getCountThreelap(raceCategory)
    }

    fun getCountRoute(raceCategory: RaceCategory): Flow<Int> {
        return raceResultDao.getCountRoute(raceCategory)
    }

    fun getCountPerSessionTotal(raceCategory: RaceCategory): Flow<List<Int>> {
        return raceResultDao.getCountPerSessionTotal(raceCategory)
    }

    fun getCountPerSessionThreelap(raceCategory: RaceCategory): Flow<List<Int>> {
        return raceResultDao.getCountPerSessionThreelap(raceCategory)
    }

    fun getCountPerSessionRoute(raceCategory: RaceCategory): Flow<List<Int>> {
        return raceResultDao.getCountPerSessionRoute(raceCategory)
    }

    fun getAveragePositionTotal(raceCategory: RaceCategory): Flow<Int?> {
        return raceResultDao.getAveragePositionTotal(raceCategory)
    }

    fun getAveragePositionThreelap(raceCategory: RaceCategory): Flow<Int?> {
        return raceResultDao.getAveragePositionThreelap(raceCategory)
    }

    fun getAveragePositionRoute(raceCategory: RaceCategory): Flow<Int?> {
        return raceResultDao.getAveragePositionRoute(raceCategory)
    }
}

object MathUtils { // Oder eine andere passende Hilfsklasse/Datei
    fun calculateMedian(numbers: List<Int>): Int {
        if (numbers.isEmpty()) return 0
        val sortedNumbers = numbers.sorted()
        val size = sortedNumbers.size
        return if (size % 2 == 1) {
            // Ungerade Anzahl: Der mittlere Wert
            sortedNumbers[size / 2].toInt()
        } else {
            // Gerade Anzahl: Der Durchschnitt der beiden mittleren Werte
            ((sortedNumbers[size / 2 - 1] + sortedNumbers[size / 2]) / 2.0).toInt()
        }
    }
}