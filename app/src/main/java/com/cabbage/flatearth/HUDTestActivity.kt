package com.cabbage.flatearth

import android.os.Bundle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.appcompat.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_hud_test.*
import timber.log.Timber

class HUDTestActivity : AppCompatActivity() {

//    private var _xDelta: Int = 0
    private var _yDelta: Int = 0

    private var mTopMargin: Int = 360

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hud_test)
        // disable hardware acceleration
        findViewById<View>(android.R.id.content).setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        val lp = theLine.layoutParams as CoordinatorLayout.LayoutParams
        lp.topMargin = mTopMargin

        theLine.setOnTouchListener { view, event ->
//            val rawX = event.rawX.toInt()
            val rawY = event.rawY.toInt()
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    val lParams = view.layoutParams as CoordinatorLayout.LayoutParams
//                    _xDelta = rawX - lParams.leftMargin
                    _yDelta = rawY - lParams.topMargin
                }
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                }
                MotionEvent.ACTION_POINTER_UP -> {
                }
                MotionEvent.ACTION_MOVE -> {
                    val layoutParams = view.layoutParams as CoordinatorLayout.LayoutParams
//                    layoutParams.leftMargin = rawX - _xDelta
                    mTopMargin = rawY - _yDelta
                    layoutParams.topMargin = mTopMargin
                    view.requestLayout()
//                    layoutParams.rightMargin = -250
//                    layoutParams.bottomMargin = -250
//                    view.layoutParams = layoutParams
                }
            }
//            findViewById<View>(android.R.id.content).invalidate()
            return@setOnTouchListener true

        }
    }
}
