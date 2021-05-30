package com.example.traintime.util

import android.graphics.Color
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.traintime.domain.TrainInfo

@BindingAdapter("trainTypeColor")
fun setTrainTypeColor(imageView: ImageView, trainInfo: TrainInfo) {
    imageView.setColorFilter(when (trainInfo.trainTypeName) {
        "太魯閣" -> Color.rgb(255, 80, 0)
        "普悠瑪" -> Color.rgb(208, 2, 22)
        "自強　" -> Color.rgb(255, 135, 8)
        "莒光　" -> Color.rgb(255, 210, 0)
        "復興　" -> Color.rgb(0, 172, 232)
        else -> Color.rgb(51, 51, 51)
    })
}