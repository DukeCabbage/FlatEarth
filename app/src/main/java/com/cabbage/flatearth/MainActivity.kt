package com.cabbage.flatearth

import android.os.Bundle
import android.view.Gravity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.cabbage.flatearth.misc.checkIsSupportedDeviceOrFinish
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment

import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val arFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment
    }

    private var andyRenderable: ModelRenderable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkIsSupportedDeviceOrFinish()) return
        setContentView(R.layout.activity_main)

        val lightEnabled = arFragment.arSceneView.isLightEstimationEnabled
        Timber.i("$lightEnabled")

        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
                .setSource(this, R.raw.andy)
                .build()
                .thenAccept { andyRenderable = it }
                .exceptionally { _ ->
                    val toast = Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                    null
                }
    }
}
