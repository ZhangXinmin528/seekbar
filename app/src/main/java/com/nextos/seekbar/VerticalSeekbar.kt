package com.nextos.seekbar

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import java.math.BigDecimal

/**
 * Created by zhangxinmin on 2023/11/2.
 * 垂直支持负值的seekbar
 */

class VerticalSeekbar : View {

    private val sTag = "VerticalSeekbar"
    private var context: Context? = null
    private var touchSlop: Int = 0

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
    private var offset: Float = 0f
    private var touchMode: Int = 0

    private var seekbarChangedListener: OnSeekbarChangedListener? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initParams(context, attrs, defStyleAttr)
    }


    private fun initParams(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
        this.context = context
        val typedArray =
            context?.obtainStyledAttributes(attrs, R.styleable.VerticalSeekbar, defStyleAttr, 0)
        typedArray?.let {
            labelColor = typedArray.getColor(
                R.styleable.VerticalSeekbar_labelColor, Color.parseColor("#90000000")
            )
            labelSize =
                typedArray.getDimensionPixelSize(R.styleable.VerticalSeekbar_labelSize, 20)
                    .toFloat()

            trackColor = typedArray.getColor(
                R.styleable.VerticalSeekbar_trackColor, Color.parseColor("#6968779B")
            )

            trackWidth =
                typedArray.getDimensionPixelSize(R.styleable.VerticalSeekbar_trackWidth, 16)
                    .toFloat()

            thumbColor = typedArray.getColor(
                R.styleable.VerticalSeekbar_thumbColor, Color.parseColor("#ffffff")
            )
            thumbRadius =
                typedArray.getDimensionPixelSize(R.styleable.VerticalSeekbar_thumbRadius, 28)
                    .toFloat()

            indicatorColor = typedArray.getColor(
                R.styleable.VerticalSeekbar_indicatorColor, Color.parseColor("#ffffff")
            )

            maxProgress =
                typedArray.getFloat(R.styleable.VerticalSeekbar_maxProgress, 20f)

            minProgress = -maxProgress

            progress =
                typedArray.getFloat(R.styleable.VerticalSeekbar_progress, 0f)
        }

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
        offset = trackLength / 200

        touchSlop = context?.let { ViewConfiguration.get(it).scaledTouchSlop } ?: 0

        typedArray?.recycle()
    }

    fun setOnSeekbarChangedListener(listener: OnSeekbarChangedListener) {
        this.seekbarChangedListener = listener
    }

    fun setProgress(progress: Float) {
        this.progress = progress
        invalidate()
    }

    fun setAnimProgress(progress: Float) {
        val objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress).apply {
            duration = 1000
        }
        objectAnimator.start()
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
        setMeasuredDimension(measureWidth, measureHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            drawTrackBackground(canvas)

            drawProgress(canvas)

            drawThumb(canvas)

            drawProgressLabel(canvas)

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        var x = 0f
        var y = 0f
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchMode = 0
            }

            MotionEvent.ACTION_UP -> {
                y = event.y
                if (touchMode == 0) {
                    val calProgress = calculateProgressByPx(y)
                    progress =
                        if (calProgress > maxProgress) maxProgress else if (calProgress < minProgress) minProgress else calProgress
                    postInvalidate()
                }
                seekbarChangedListener?.onSeekbarChanged(progress)
            }

            MotionEvent.ACTION_MOVE -> {
                val currY = event.y
                if (currY - y >= touchSlop) {//存在滑动
                    val calProgress = calculateProgressByPx(currY)
                    touchMode = 1
                    progress =
                        if (calProgress > maxProgress) maxProgress else if (calProgress < minProgress) minProgress else calProgress
                    postInvalidate()
                    y = currY
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                return super.onTouchEvent(event)
            }
        }
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                if (progress > minProgress) {
                    progress -= offset
                    postInvalidate()
                    seekbarChangedListener?.onSeekbarChanged(progress)
                }
                return true
            }

            KeyEvent.KEYCODE_DPAD_UP -> {
                if (progress < maxProgress) {
                    progress += offset
                    postInvalidate()
                    seekbarChangedListener?.onSeekbarChanged(progress)
                }
                return true
            }
        }
        return super.onKeyDown(keyCode, event)

    }

    private fun drawThumb(canvas: Canvas) {
        paint?.apply {
            color = thumbColor
            style = Paint.Style.FILL
        }?.let {

            val startX = centerX + thumbRadius
            val startY = getProgressY() - thumbLineHeight / 2.0f
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
                    centerX - thumbRadius, startY - thumbRadius, startX, startY + thumbRadius
                )
                arcTo(rectFTop, 180f, 180f)
            }
            canvas.drawPath(thumbPath, it)
        }
    }

    private fun drawProgress(canvas: Canvas) {
        paint?.apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = trackWidth
            color = indicatorColor
            strokeCap = Paint.Cap.BUTT
        }?.let {
            val startX = centerX
            val startY = centerY
            val stopX = centerX
            val stopY = getProgressY()
            canvas.drawLine(
                startX, startY, stopX, stopY, it
            )
        }

    }

    private fun getProgressY(): Float {
        return (maxProgress - progress) * (trackStopY - defaultPadding - thumbRadius) / (maxProgress - minProgress) + defaultPadding + thumbRadius
    }

    private fun calculateProgressByPx(y: Float): Float {
        return maxProgress - (y - defaultPadding - thumbRadius) * (maxProgress - minProgress) / (trackStopY - defaultPadding - thumbRadius)
    }

    private fun drawProgressLabel(canvas: Canvas) {
        val label = if (progress == 0f) "0" else BigDecimal(progress.toString()).setScale(
            1, BigDecimal.ROUND_HALF_DOWN
        ).toString()

        val labelWidth = labelPaint?.measureText(label) ?: 0f
        val baseLine = labelPaint?.fontMetrics?.let {
            (it.bottom - it.top) / 2.0f - it.bottom
        } ?: 0f

        val baseLineY = baseLine + getProgressY()

        labelPaint?.apply {
            textSize = labelSize
            color = labelColor
        }?.let {
            canvas.drawText(label, centerX - labelWidth / 2.0f, baseLineY, it)
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
                centerX, trackStartY, centerX, trackStopY, it
            )
        }
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        return MeasureSpec.getSize(widthMeasureSpec)
    }

    private fun measureHeight(heightMeasureSpec: Int): Int {
        return MeasureSpec.getSize(heightMeasureSpec)
    }


}

interface OnSeekbarChangedListener {

    fun onSeekbarChanged(progress: Float)
}