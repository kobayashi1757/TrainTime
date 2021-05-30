package com.example.traintime.domain

data class TrainInfo(
        val trainNo: String,
        val trainTypeName: String,
        val startingInfo: String,
        val endingInfo: String,
        val duration: String,
        val startingTime: Int,
)