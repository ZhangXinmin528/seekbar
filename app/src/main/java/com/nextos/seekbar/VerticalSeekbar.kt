package com.nextos.seekbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * Created by zhangxinmin on 2023/11/2.
 * 垂直支持负值的seekbar
 */

class VerticalSeekbar : View {

    private var context: Context? = null

    private var paint: Paint? = null

    private var labelPaint: Paint? = null
    private var labelColor: Int = Color.parseColor("#90000000")
    private var labelSize: Float = 20f.spToPx

    //布局范围
    private var viewHeight: Int = 100.dpToPx.toInt()
    private var viewWidth: Int = 200.dpToPx.toInt()
    private var centerX: Float = viewWidth / 2.0f
    private var centerY: Float = viewHeight / 2.0f
    private var defaultPadding: Float = 12.dpToPx

    //track
    private var trackColor: Int = Color.parseColor("#6968779B")
    private var trackWidth: Float = 16f.dpToPx
    private var trackLength: Float = 0f
    private var trackStartY: Float = 0f
    private var trackStopY: Float = 0f

    //thumb
    private var thumbColor: Int = Color.parseColor("#ffffff")
    private var thumbRadius: Float = 28f.dpToPx
    private var thumbLineHeight: Float = thumbRadius / 2.8f

    //indicator
    private var indicatorColor: Int = Color.parseColor("#ffffff")

    //progress
    private var maxProgress: Float = 10.0f
    private var progress: Float = 0.0f
    private var minProgress: Float = -maxProgress

    constructor(context: Context?) : this(context, null)
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

        labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            textSize = labelSize
            color = labelColor
            strokeWidth = 2f
        }

        trackLength = maxProgress - minProgress

    }

    fun setProgress(progress: Float) {
        this.progress = progress
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureHeight: Int = measureHeight(heightMeasureSpec)
        val measureWidth: Int = measureWidth(widthMeasureSpec)
        if (measureHeight != 0) {
            viewHeight = measureHeight
            centerY = viewHeight / 2.0f

            trackStartY = defaultPadding + thumbRadius
            trackStopY = viewHeight - defaultPadding - thumbRadius
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
//            drawBaseLine(it)
            drawTrackBackground(canvas)
            drawThumb(canvas)
            drawProgress(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {

            }

            MotionEvent.ACTION_UP -> {

            }
        }
        return super.onTouchEvent(event)
    }

    private fun drawThumb(canvas: Canvas) {
        paint?.apply {
            color = thumbColor
            style = Paint.Style.FILL
        }?.let {
            val startX = centerX + thumbRadius
            val startY = centerY - thumbLineHeight / 2.0f
            val thumbPath = Path().apply {
                moveTo(startX, startY)
                lineTo(startX, startY + thumbLineHeight)
                val rectFBootom = RectF(
                    centerX - thumbRadius,
                    startY + thumbLineHeight - thumbRadius,
                    startX,
                    startY + thumbLineHeight + thumbRadius
                )
                arcTo(rectFBootom, 0f, 180f)
                lineTo(centerX - thumbRadius, startY)
                val rectFTop = RectF(
                    centerX - thumbRadius,
                    startY - thumbRadius,
                    startX,
                    startY + thumbRadius
                )
                arcTo(rectFTop, 180f, 180f)
            }
            canvas.drawPath(thumbPath, it)
        }
    }

    private fun drawProgress(canvas: Canvas) {
        val label = if (progress == 0f) "0" else progress.toString()

        val labelWidth = labelPaint?.measureText(label) ?: 0f
        val baseLine = labelPaint?.fontMetrics?.let {
            (it.bottom - it.top) / 2.0f - it.bottom
        } ?: 0f

        val baseLineY = baseLine + centerY

        Log.d("VerticalSeekbar", "drawProgress..baseLineY:$baseLineY..labelWidth:$labelWidth")
        labelPaint?.apply {
            textSize = labelSize
            color = labelColor
        }?.let {
            canvas.drawText(label, centerX - labelWidth / 2.0f, baseLineY, it)
        }

    }

    private fun drawBaseLine(canvas: Canvas) {
        paint?.apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 0.5f
        }?.let {
            canvas.drawLine(centerX, 0f, centerX, viewHeight.toFloat(), it)
        }
    }

    //绘制轨道背景
    private fun drawTrackBackground(canvas: Canvas) {

        paint?.apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = trackWidth
            color = trackColor
            strokeCap = Paint.Cap.ROUND
        }?.let {
            canvas.drawLine(
                centerX,
                trackStartY,
                centerX,
                trackStopY,
                it
            )
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