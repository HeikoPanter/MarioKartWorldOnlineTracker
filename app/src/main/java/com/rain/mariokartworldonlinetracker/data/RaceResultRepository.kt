package com.rain.mariokartworldonlinetracker.data

import com.rain.mariokartworldonlinetracker.KnockoutCupName
import com.rain.mariokartworldonlinetracker.TrackName
import com.rain.mariokartworldonlinetracker.data.pojo.AveragePositionByType
import com.rain.mariokartworldonlinetracker.data.pojo.MedianRaceCountPerSessionByType
import com.rain.mariokartworldonlinetracker.data.pojo.MostPlayedRaceRoute
import com.rain.mariokartworldonlinetracker.data.pojo.RaceCountByType
import com.rain.mariokartworldonlinetracker.data.pojo.ResultHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class RaceResultRepository(private val raceResultDao: RaceResultDao) {

    val knockoutCountTotal: Flow<Int> = raceResultDao.getKnockoutCountTotal()
    val knockoutAveragePosition: Flow<Int?> = raceResultDao.getKnockoutAveragePositionTotal()
    val mostFrequentKnockoutCup: Flow<KnockoutCupName?> = raceResultDao.getMostFrequentKnockoutCupName()

    suspend fun insert(raceResult: RaceResult) {
        raceResultDao.insert(raceResult)
    }

    fun getRaceCountPOJO(): Flow<RaceCountByType> {
        val raceCountTotal: Flow<Int> = raceResultDao.getRaceCountTotal()
        val raceCountThreelap: Flow<Int> = raceResultDao.getRaceCountThreelap()
        val raceCountRoute: Flow<Int> = raceResultDao.getRaceCountRoute()

        return combine(
            raceCountTotal,
            raceCountThreelap,
            raceCountRoute
        ) { total, threelap, route ->
            RaceCountByType(
                raceCountTotal = total,
                raceCountThreelap = threelap,
                raceCountRoute = route)
        }
    }

    fun getRaceVsCountPOJO(): Flow<RaceCountByType> {
        val raceCountTotal: Flow<Int> = raceResultDao.getRaceVsCountTotal()
        val raceCountThreelap: Flow<Int> = raceResultDao.getRaceVsCountThreelap()
        val raceCountRoute: Flow<Int> = raceResultDao.getRaceVsCountRoute()

        return combine(
            raceCountTotal,
            raceCountThreelap,
            raceCountRoute
        ) { total, threelap, route ->
            RaceCountByType(
                raceCountTotal = total,
                raceCountThreelap = threelap,
                raceCountRoute = route)
        }
    }

    fun getMedianKnockoutCountPerSession(): Flow<Int> {
        return raceResultDao.getKnockoutCountPerSessionTotal().map { countsList -> MathUtils.calculateMedian(countsList) }
    }

    fun getMedianRaceCountPerSessionPOJO(): Flow<MedianRaceCountPerSessionByType> {
        val averageTotal: Flow<Int> = raceResultDao.getRaceCountPerSessionTotal().map { countsList -> MathUtils.calculateMedian(countsList) }
        val averageThreelap: Flow<Int> = raceResultDao.getRaceCountPerSessionThreelap().map { countsList -> MathUtils.calculateMedian(countsList) }
        val averageRoute: Flow<Int> = raceResultDao.getRaceCountPerSessionRoute().map { countsList -> MathUtils.calculateMedian(countsList) }

        return combine(
            averageTotal,
            averageThreelap,
            averageRoute
        ) { total, threelap, route ->
            MedianRaceCountPerSessionByType(
                raceCountTotal = total,
                raceCountThreelap = threelap,
                raceCountRoute = route)
        }
    }

    fun getMedianRaceVsCountPerSessionPOJO(): Flow<MedianRaceCountPerSessionByType> {
        val averageTotal: Flow<Int> = raceResultDao.getRaceVsCountPerSessionTotal().map { countsList -> MathUtils.calculateMedian(countsList) }
        val averageThreelap: Flow<Int> = raceResultDao.getRaceVsCountPerSessionThreelap().map { countsList -> MathUtils.calculateMedian(countsList) }
        val averageRoute: Flow<Int> = raceResultDao.getRaceVsCountPerSessionRoute().map { countsList -> MathUtils.calculateMedian(countsList) }

        return combine(
            averageTotal,
            averageThreelap,
            averageRoute
        ) { total, threelap, route ->
            MedianRaceCountPerSessionByType(
                raceCountTotal = total,
                raceCountThreelap = threelap,
                raceCountRoute = route)
        }
    }

    fun getAveragePositionPOJO(): Flow<AveragePositionByType> {
        val averageTotal: Flow<Int?> = raceResultDao.getAveragePositionTotal()
        val averageThreelap: Flow<Int?> = raceResultDao.getAveragePositionThreelap()
        val averageRoute: Flow<Int?> = raceResultDao.getAveragePositionRoute()

        return combine(
            averageTotal,
            averageThreelap,
            averageRoute
        ) { total, threelap, route ->
            AveragePositionByType(
                averagePositionTotal = total ?: 0,
                averagePositionThreelap = threelap ?: 0,
                averagePositionRoute = route ?: 0)
        }
    }

    fun getAverageRaceVsPositionPOJO(): Flow<AveragePositionByType> {
        val averageTotal: Flow<Int?> = raceResultDao.getAverageRaceVsPositionTotal()
        val averageThreelap: Flow<Int?> = raceResultDao.getAverageRaceVsPositionThreelap()
        val averageRoute: Flow<Int?> = raceResultDao.getAverageRaceVsPositionRoute()

        return combine(
            averageTotal,
            averageThreelap,
            averageRoute
        ) { total, threelap, route ->
            AveragePositionByType(
                averagePositionTotal = total ?: 0,
                averagePositionThreelap = threelap ?: 0,
                averagePositionRoute = route ?: 0)
        }
    }

    fun getMostFrequentThreelapTrackName(): Flow<TrackName?> {
        return raceResultDao.getMostFrequentThreelapTrackName()
    }

    fun getMostFrequentThreelapVsTrackName(): Flow<TrackName?> {
        return raceResultDao.getMostFrequentThreelapVsTrackName()
    }

    fun getMostFrequentRouteTrackName(): Flow<MostPlayedRaceRoute?> {
        return raceResultDao.getMostFrequentRouteTrackName()
    }

    fun getMostFrequentRouteVsTrackName(): Flow<MostPlayedRaceRoute?> {
        return raceResultDao.getMostFrequentRouteVsTrackName()
    }

    suspend fun getResultHistory() : List<ResultHistory> {
        return raceResultDao.getResultHistory()
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