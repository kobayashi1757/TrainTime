package com.example.traintime.repository

import com.example.traintime.database.TrainTimeDatabase
import com.example.traintime.database.asDomainModel
import com.example.traintime.domain.TrainInfo
import com.example.traintime.network.RailwayNetwork
import com.example.traintime.network.asDatabaseModel
import com.example.traintime.util.today
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrainInfoRepository(private val database: TrainTimeDatabase) {

    suspend fun getTrainInfos(fromTo: String): List<TrainInfo> {
        return withContext(Dispatchers.Default) {
            database.trainTimeDao.getTrainInfos(fromTo, today()).asDomainModel()
        }
    }

    suspend fun queryTimetable(from: String, to: String) {
        withContext(Dispatchers.IO) {
            val response = RailwayNetwork.railway.getResponse(from, to, today())
            database.trainTimeDao.deleteTrainInfos("${from}-${to}")
            database.trainTimeDao.insertTrainInfos(response.asDatabaseModel())
        }
    }
}