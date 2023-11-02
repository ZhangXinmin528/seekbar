package com.nextos.seekbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhangxinmin on 2023/11/2.
 * 垂直支持负值的seekbar
 */

class VerticalSeekbar : View {

    private var paint: Paint? = null

    private var viewHeight: Float = 100f.dpToPx
    private var viewWidth: Float = 200f.dpToPx

    constructor(context: Context?) : this(context, null)

    //track
    private var trackColor: Int = Color.WHITE

    private var trackWidth: Float = 5f

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initParams(context, attrs, defStyleAttr)
    }


    private fun initParams(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = trackColor
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureHeight: Int = measureHeight(heightMeasureSpec)
        val measureWidth: Int = measureWidth(widthMeasureSpec)
        super.onMeasure(measureWidth, measureHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            drawTrackBackage(canvas)
        }
    }

    //绘制轨道背景
    private fun drawTrackBackage(canvas: Canvas?) {

    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(widthMeasureSpec)
        return MeasureSpec.getSize(widthMeasureSpec)
    }

    private fun measureHeight(widthMeasureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(widthMeasureSpec)
        return MeasureSpec.getSize(widthMeasureSpec)
    }


}