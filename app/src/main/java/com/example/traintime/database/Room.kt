package com.example.traintime.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TrainTimeDao {
    @Query("SELECT * FROM train_infos WHERE from_to = :fromTo AND train_date = :date ORDER BY starting_time ASC")
    fun getTrainInfos(fromTo: String, date: String): List<DatabaseTrainInfo>

    @Query("DELETE FROM train_infos WHERE from_to = :fromTo")
    fun deleteTrainInfos(fromTo: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrainInfos(trainInfos: List<DatabaseTrainInfo>)

    @Query("SELECT * FROM timetable_requests ORDER BY list_order ASC")
    fun getTimetableRequests(): LiveData<List<TimetableRequest>>

    @Query("DELETE FROM timetable_requests")
    fun deleteTimetableRequests()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTimetableRequest(timetableRequest: TimetableRequest)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTimetableRequests(timetableRequests: List<TimetableRequest>)
}

@Database(entities = arrayOf(DatabaseTrainInfo::class, TimetableRequest::class), version = 1)
abstract class TrainTimeDatabase : RoomDatabase() {
    abstract val trainTimeDao: TrainTimeDao
}

private lateinit var INSTANCE: TrainTimeDatabase

fun getDatabase(context: Context): TrainTimeDatabase {
    synchronized(TrainTimeDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    TrainTimeDatabase::class.java,
                    "train_time"
            ).build()
        }
    }
    return INSTANCE
}