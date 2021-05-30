package com.example.traintime.network

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL =
        "https://ptx.transportdata.tw/MOTC/v3/Rail/TRA/"

interface RailwayService {
    @Headers("User-Agent:Mozilla/5.0")
    @GET("DailyTrainTimetable/OD/{from}/to/{to}/{date}")
    suspend fun getResponse(
            @Path("from") from: String,
            @Path("to") to: String,
            @Path("date") date: String,
            @Query("format") format: String = "JSON",
    ): NetworkResponse
}

object RailwayNetwork {
    private val moshi = Moshi.Builder()
            .add(NetworkTrainInfo::class.java, NetworkTrainInfoAdapter())
            .build()

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    val railway: RailwayService = retrofit.create(RailwayService::class.java)
}