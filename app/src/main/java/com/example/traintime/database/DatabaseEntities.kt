package com.example.traintime.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.traintime.domain.TrainInfo

@Entity(tableName = "train_infos", primaryKeys = arrayOf("train_no", "starting_time", "from_to"))
data class DatabaseTrainInfo(
        @ColumnInfo(name = "train_no") val trainNo: String,
        @ColumnInfo(name = "train_type_name") val trainTypeName: String,
        @ColumnInfo(name = "starting_station_name") val startingStationName: String,
        @ColumnInfo(name = "ending_station_name") val endingStationName: String,
        @ColumnInfo(name = "starting_time") val startingTime: Int,
        @ColumnInfo(name = "ending_time") val endingTime: Int,
        @ColumnInfo(name = "from_to") val fromTo: String,
        @ColumnInfo(name = "train_date") val trainDate: String,
)

private fun toInfo(stationName: String, time: Int): String {
    val h = time / 60
    val m = time % 60
    return "${stationName}\n%02d:%02d".format(h, m)
}

private fun toDuration(startingTime: Int, endingTime: Int): String {
    var duration = endingTime - startingTime
    if (duration < 0)
        duration += 24 * 60
    val h = duration / 60
    val m = duration % 60
    return when {
        duration > 60 -> "$h 小時 $m 分"
        duration == 60 -> "1 小時"
        else -> "$m 分"
    }
}

fun List<DatabaseTrainInfo>.asDomainModel(): List<TrainInfo> {
    return map {
        TrainInfo(
                it.trainNo,
                it.trainTypeName.let { name ->
                    if (name.length == 2) "${name}　" else name
                },
                toInfo(it.startingStationName, it.startingTime),
                toInfo(it.endingStationName, it.endingTime),
                toDuration(it.startingTime, it.endingTime),
                it.startingTime,
        )
    }
}

@Entity(tableName = "timetable_requests", primaryKeys = arrayOf("from_station_id", "to_station_id"))
data class TimetableRequest(
        @ColumnInfo(name = "from_country_or_city") val fromCountyOrCity: String,
        @ColumnInfo(name = "to_country_or_city") val toCountyOrCity: String,
        @ColumnInfo(name = "from_station") val fromStation: String,
        @ColumnInfo(name = "to_station") val toStation: String,
        @ColumnInfo(name = "from_station_id") val fromStationID: String,
        @ColumnInfo(name = "to_station_id") val toStationID: String,
        @ColumnInfo(name = "list_order") var listOrder: Int = 0,
)