package com.cabbage.flatearth

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast

import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //btn to close the application
        val imgClose = findViewById<View>(R.id.imgClose) as ImageButton
        imgClose.setOnClickListener { System.exit(0) }
    }

    public override fun onStart() {
        super.onStart()
        initCamera()
    }

    public override fun onStop() {
        super.onStop()
        releaseCameraAndPreview()
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initCamera()
                } else {
                    Toast.makeText(this, "Camera permission NOT granted", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    private fun initCamera() {

        val permissionCheck = ContextCompat.checkSelfPermission(this,
                                                                Manifest.permission.CAMERA)

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(this,
                                              arrayOf(Manifest.permission.CAMERA),
                                              PERMISSION_REQUEST_CAMERA)

            return
        }

        try {
            releaseCameraAndPreview()
            mCamera = Camera.open()//you can use open(int) to use different cameras
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.e("Failed to get camera: %s", e.message)
        }

        if (mCamera != null) {
            mCameraView = CameraView(this, mCamera)//create a SurfaceView to show camera data
            val camera_view = findViewById<View>(R.id.camera_view) as FrameLayout
            camera_view.addView(mCameraView)//add the SurfaceView to the layout
        }
    }

    private fun releaseCameraAndPreview() {
        if (mCamera != null) {
            mCamera!!.release()
            mCamera = null
        }
    }

    companion object {

        private val MAIN_ACTIVITY_CODE = 1
        private val PERMISSION_REQUEST_CAMERA = MAIN_ACTIVITY_CODE * 100 + 1
    }
}
