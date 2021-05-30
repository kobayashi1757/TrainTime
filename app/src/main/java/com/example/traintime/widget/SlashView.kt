package com.example.traintime.widget

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import com.example.traintime.R

private fun Float.clamp(lower: Float, upper: Float): Float {
    return when {
        this > upper -> upper
        this < lower -> lower
        else -> this
    }
}

class SlashView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val mPaint = Paint(ANTI_ALIAS_FLAG)
    private val leftPath = Path()
    private val rightPath = Path()
    private var ratio = 0f
    private var cornerRadius = 0f
    private var leftColor = Color.BLUE
    private var rightColor = Color.RED

    init {
        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.SlashView,
                0, 0).apply {
            try {
                ratio = getFloat(R.styleable.SlashView_ratio, ratio).clamp(0f, 1f)
                cornerRadius = getDimensionPixelSize(R.styleable.SlashView_cornerRadius, 0).toFloat()
                leftColor = getColor(R.styleable.SlashView_leftColor, leftColor)
                rightColor = getColor(R.styleable.SlashView_rightColor, rightColor)
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val wf = w.toFloat()
        val hf = h.toFloat()
        val basePath = Path().apply {
            addRoundRect(RectF(0f, 0f, wf, hf), cornerRadius, cornerRadius, Path.Direction.CW)
        }
        val slashPath = Path().apply {
            moveTo(wf * ratio, 0f)
            lineTo(wf, 0f)
            lineTo(wf, hf)
            lineTo(wf * (1 - ratio), hf)
            close()
        }
        leftPath.op(basePath, slashPath, Path.Op.DIFFERENCE)
        rightPath.op(basePath, slashPath, Path.Op.INTERSECT)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            mPaint.color = leftColor
            it.drawPath(leftPath, mPaint)
            mPaint.color = rightColor
            it.drawPath(rightPath, mPaint)
        }
    }
}