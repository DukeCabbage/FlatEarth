package com.cabbage.flatearth.ui.dashboard

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import com.cabbage.flatearth.R
import timber.log.Timber

class LevelView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    private val mHandler = Handler()
    private val mGreenPaint = Paint()
    private var dimension = 10000.0  // Approximate size of the view

    var delegate = object : Delegate {}

    init {
        @Suppress("DEPRECATION")
        mGreenPaint.color = resources.getColor(R.color.colorGreenA400)
        mGreenPaint.style = Paint.Style.STROKE
        mGreenPaint.strokeWidth = 8.0f
        mGreenPaint.maskFilter = BlurMaskFilter(2f, BlurMaskFilter.Blur.NORMAL)
    }

    private val mTick = Runnable { tick() }
    fun start() {
        Timber.v("start")
        tick()
    }

    fun stop() {
        Timber.v("stop")
        mHandler.removeCallbacks(mTick)
    }

    private fun tick() {
        if (isAttachedToWindow) invalidate()
        mHandler.postDelayed(mTick, 20) // 20ms == 60fps
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        dimension = Math.sqrt((w * w + h * h).toDouble())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val angle = delegate.getAngleToLevel()
        canvas.rotate(angle.toFloat(), (width / 2).toFloat(), (height / 2).toFloat())

        val centerX = height / 2.0f
        val centerY = width / 2.0f
        val startX = centerY - dimension.toFloat()
        val stopX = centerY + dimension.toFloat()
        canvas.drawLine(startX, centerX, stopX, centerX, mGreenPaint)
        canvas.drawLine(centerY, centerX, centerY, (height * 4 / 6).toFloat(), mGreenPaint)
    }

    interface Delegate {
        fun getAngleToLevel(): Double = 0.0
    }
}