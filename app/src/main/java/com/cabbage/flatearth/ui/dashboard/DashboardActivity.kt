package com.cabbage.flatearth.ui.dashboard

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Surface
import android.view.WindowManager
import android.widget.Toast
import com.cabbage.flatearth.R
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.include_readings.*

class DashboardActivity : AppCompatActivity(),
                          SensorEventListener,
                          LevelView.Delegate {

    private lateinit var mSensorManager: SensorManager
    private lateinit var mSensor: Sensor

    private var asin: Double = 0.0 // from -90 to 90
    private var acos: Double = 0.0 // from 0 to 180
    private var isLevelViewDrawing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)

        levelView.delegate = this

        fab.setOnClickListener {
            isLevelViewDrawing = !isLevelViewDrawing
            if (isLevelViewDrawing) levelView.start()
            else levelView.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        Toast.makeText(this, "onAccuracyChanged", Toast.LENGTH_SHORT).show()
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_GRAVITY) return

        //        Timber.v("onSensorChanged, %.3f, %.3f, %.3f", event.values[0], event.values[1], event.values[2])

        val readingX = event.values[0]
        val readingY = event.values[1]
        val readingZ = event.values[2]

        tvReadingX.text = String.format("%.3f", readingX)
        tvReadingY.text = String.format("%.3f", readingY)
        tvReadingZ.text = String.format("%.3f", readingZ)

        val gravProjOnScreenSurface = Math.sqrt(Math.pow(readingX.toDouble(), 2.0)
                                                        + Math.pow(readingY.toDouble(), 2.0))

        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val valueDownward = when (windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> readingY
            Surface.ROTATION_90 -> readingX
            Surface.ROTATION_180 -> -readingY
            Surface.ROTATION_270 -> -readingX
            else -> readingY
        }

        val valueLeftward = when (windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> readingX
            Surface.ROTATION_90 -> -readingY
            Surface.ROTATION_180 -> -readingX
            Surface.ROTATION_270 -> readingY
            else -> event.values[0]
        }

        tvRatioCos.text = String.format("%.3f", valueDownward / gravProjOnScreenSurface)
        tvRatioSin.text = String.format("%.3f", valueLeftward / gravProjOnScreenSurface)

        asin = Math.toDegrees(Math.asin(valueLeftward / gravProjOnScreenSurface))
        acos = Math.toDegrees(Math.acos(valueDownward / gravProjOnScreenSurface))

        tvAngleCos.text = getString(R.string.three_decimal, acos)
        tvAngleSin.text = getString(R.string.three_decimal, asin)
    }

    override fun getAngleToLevel(): Double {
        return if (acos <= 90) asin else 180 - asin
    }
}
