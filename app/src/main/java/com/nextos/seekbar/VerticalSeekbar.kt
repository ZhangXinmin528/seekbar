package com.nextos.seekbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by zhangxinmin on 2023/11/2.
 * 垂直支持负值的seekbar
 */

class VerticalSeekbar : View {

    private var context: Context? = null
    private var paint: Paint? = null

    private var viewHeight: Int = 100.dpToPx.toInt()
    private var viewWidth: Int = 200.dpToPx.toInt()
    private var centerX: Float = viewWidth / 2.0f
    private var centerY: Float = viewHeight / 2.0f
    private var defaultPadding: Float = 12.dpToPx

    constructor(context: Context?) : this(context, null)

    //track
    private var trackColor: Int = Color.LTGRAY

    private var trackWidth: Float = 8f.dpToPx

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initParams(context, attrs, defStyleAttr)
    }


    private fun initParams(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
        this.context = context
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = trackWidth
            color = trackColor
            strokeCap = Paint.Cap.ROUND
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureHeight: Int = measureHeight(heightMeasureSpec)
        val measureWidth: Int = measureWidth(widthMeasureSpec)
        if (measureHeight != 0) {
            viewHeight = measureHeight
            centerY = viewHeight / 2.0f
        }
        if (measureWidth != 0) {
            viewWidth = measureWidth
            centerX = viewWidth / 2.0f
        }
        Log.d(
            "VerticalSeekbar",
            "onMeasure..measureHeight:$measureHeight..measureWidth:$measureWidth"
        )
        setMeasuredDimension(measureWidth, measureHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            drawBaseLine(it)
            drawTrackBackground(canvas)
            drawThumb(canvas)
        }
    }

    private fun drawThumb(canvas: Canvas) {
        paint?.apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }?.let {
            canvas.drawLine(centerX,0f,centerX,viewHeight.toFloat(), it)
        }
    }

    private fun drawBaseLine(canvas: Canvas) {
        paint?.apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }?.let {
            canvas.drawLine(centerX,0f,centerX,viewHeight.toFloat(), it)
        }
    }

    //绘制轨道背景
    private fun drawTrackBackground(canvas: Canvas) {
        Log.d(
            "VerticalSeekbar",
            "drawTrackBackground()..defaultPadding:$defaultPadding..viewHeight:$viewHeight"
        )

        paint?.apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = trackWidth
            color = trackColor
            strokeCap = Paint.Cap.ROUND
        }?.let {
            canvas.drawLine(centerX, defaultPadding, centerX, viewHeight - defaultPadding, it)
        }
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(widthMeasureSpec)
        return MeasureSpec.getSize(widthMeasureSpec)
    }

    private fun measureHeight(heightMeasureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(heightMeasureSpec)
        return MeasureSpec.getSize(heightMeasureSpec)
    }


}