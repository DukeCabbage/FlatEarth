package com.cabbage.flatearth.misc

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import timber.log.Timber


/**
 * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
 * on this device.
 * Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
 * Finishes the activity if Sceneform can not run
 */
@SuppressLint("ObsoleteSdkInt")
fun Activity.checkIsSupportedDeviceOrFinish(): Boolean {

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        Timber.e("Sceneform requires Android N or later")
        Toast.makeText(this, "Sceneform requires Android N or later",
                       Toast.LENGTH_LONG).show()
        this.finish()
        return false
    }

    val openGlVersionString = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .deviceConfigurationInfo
            .glEsVersion

    if (openGlVersionString.toDouble() < MIN_OPENGL_VERSION) {
        Timber.e("Sceneform requires OpenGL ES 3.0 later")
        Toast.makeText(this, "Sceneform requires OpenGL ES 3.0 or later",
                       Toast.LENGTH_LONG).show()
        this.finish()
        return false
    }
    return true
}

private const val MIN_OPENGL_VERSION = 3.0