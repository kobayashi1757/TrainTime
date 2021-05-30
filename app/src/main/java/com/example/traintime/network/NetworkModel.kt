package com.example.traintime.network

import com.example.traintime.database.DatabaseTrainInfo
import com.squareup.moshi.*

@JsonClass(generateAdapter = true)
data class NetworkResponse(
    @Json(name = "UpdateTime") val updateTime: String,
    @Json(name = "TrainDate") val trainDate: String,
    @Json(name = "TrainTimetables") val trainInfos: List<NetworkTrainInfo>,
)

data class NetworkTrainInfo(
    val trainNo: String,
    val trainTypeName: String,
    val from: String,
    val to: String,
    val startingStationName: String,
    val endingStationName: String,
    val startingTime: String,
    val endingTime: String,
)

private const val TRAIN_INFO            = 0
private const val TRAIN_NO              = 1
private const val TRAIN_TYPE_NAME       = 2
private const val STARTING_STATION_NAME = 3
private const val ENDING_STATION_NAME   = 4
private const val STOP_TIMES            = 5
private const val ZH_TW                 = 6
private const val STATION_ID            = 7
private const val ARRIVAL_TIME          = 8
private const val DEPARTURE_TIME        = 9

private fun JsonReader.skipNameAndValue() {
    skipName()
    skipValue()
}

private inline fun JsonReader.readObject(body: () -> Unit) {
    beginObject()
    while (hasNext()) {
        body()
    }
    endObject()
}

private inline fun JsonReader.readArray(body: (Int) -> Unit) {
    beginArray()
    var index = 0
    while (hasNext()) {
        body(index)
        index++
    }
    endArray()
}

class NetworkTrainInfoAdapter : JsonAdapter<NetworkTrainInfo>() {
    private val names = JsonReader.Options.of(
        "TrainInfo",
        "TrainNo",
        "TrainTypeName",
        "StartingStationName",
        "EndingStationName",
        "StopTimes",
        "Zh_tw",
        "StationID",
        "ArrivalTime",
        "DepartureTime",
    )

    override fun fromJson(reader: JsonReader): NetworkTrainInfo {
        var trainNo = ""
        var trainTypeName = ""
        var from = ""
        var to = ""
        var startingStationName = ""
        var endingStationName = ""
        var startingTime = ""
        var endingTime = ""

        reader.readObject {
            when (reader.selectName(names)) {
                TRAIN_INFO -> reader.readObject {
                    when (reader.selectName(names)) {
                        TRAIN_NO -> trainNo = reader.nextString()
                        TRAIN_TYPE_NAME -> reader.readObject {
                            when (reader.selectName(names)) {
                                ZH_TW -> trainTypeName = reader.nextString()
                                else -> reader.skipNameAndValue()
                            }
                        }
                        STARTING_STATION_NAME -> reader.readObject {
                            when (reader.selectName(names)) {
                                ZH_TW -> startingStationName = reader.nextString()
                                else -> reader.skipNameAndValue()
                            }
                        }
                        ENDING_STATION_NAME -> reader.readObject {
                            when (reader.selectName(names)) {
                                ZH_TW -> endingStationName = reader.nextString()
                                else -> reader.skipNameAndValue()
                            }
                        }
                        else -> reader.skipNameAndValue()
                    }
                }
                STOP_TIMES -> reader.readArray { index ->
                    when (index) {
                        0 -> reader.readObject {
                            when (reader.selectName(names)) {
                                STATION_ID -> from = reader.nextString()
                                ARRIVAL_TIME -> reader.skipValue()
                                DEPARTURE_TIME -> startingTime = reader.nextString()
                                else -> reader.skipNameAndValue()
                            }
                        }
                        1 -> reader.readObject {
                            when (reader.selectName(names)) {
                                STATION_ID -> to = reader.nextString()
                                ARRIVAL_TIME -> endingTime = reader.nextString()
                                DEPARTURE_TIME -> reader.skipValue()
                                else -> reader.skipNameAndValue()
                            }
                        }
                        else -> reader.skipValue()
                    }
                }
                else -> reader.skipNameAndValue()
            }
        }

        return NetworkTrainInfo(
            trainNo,
            trainTypeName,
            from,
            to,
            startingStationName,
            endingStationName,
            startingTime,
            endingTime,
        )
    }

    override fun toJson(writer: JsonWriter, value: NetworkTrainInfo?) {}
}

private fun String.toTimeInt(): Int {
    val (h, m) = split(":")
    return h.toInt() * 60 + m.toInt()
}

fun NetworkResponse.asDatabaseModel(): List<DatabaseTrainInfo> {
    return trainInfos.map {
        DatabaseTrainInfo(
            it.trainNo,
            it.trainTypeName.let { name ->
                val i = name.indexOf('(')
                if (i != -1) name.substring(0, i) else name
            },
            it.startingStationName,
            it.endingStationName,
            it.startingTime.toTimeInt(),
            it.endingTime.toTimeInt(),
            "${it.from}-${it.to}",
            trainDate,
        )
    }
}