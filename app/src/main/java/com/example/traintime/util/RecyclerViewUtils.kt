package com.example.traintime.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PaddingDecoration(private val padding: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
    ) {
        val index = parent.getChildAdapterPosition(view)
        if (index == 0)
            outRect.top = padding
        outRect.bottom = padding
        outRect.left = padding
        outRect.right = padding
    }
}