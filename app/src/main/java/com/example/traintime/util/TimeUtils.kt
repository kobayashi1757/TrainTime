package com.example.traintime.util

import java.text.SimpleDateFormat
import java.util.*

fun today(): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Calendar.getInstance().time)
}

fun now(): Int {
    val (h, m) = SimpleDateFormat("H:m", Locale.getDefault())
            .format(Calendar.getInstance().time).split(":")
    return h.toInt() * 60 + m.toInt()
}