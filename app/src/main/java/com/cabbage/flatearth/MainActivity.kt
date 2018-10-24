package com.cabbage.flatearth

import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.cabbage.flatearth.misc.GlideApp
import com.cabbage.flatearth.misc.checkIsSupportedDeviceOrFinish
import com.cabbage.flatearth.mock.testUrl
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val arFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment
    }

    //    private var andyRenderable: ModelRenderable? = null
    private var cardRenderable: ViewRenderable? = null

    private var placed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkIsSupportedDeviceOrFinish()) return
        setContentView(R.layout.activity_main)

        val lightEnabled = arFragment.arSceneView.isLightEstimationEnabled
        Timber.i("$lightEnabled")

        ViewRenderable.builder()
                .setView(this, R.layout.image_view_test)
                .build()
                .thenAccept {
                    it.verticalAlignment = ViewRenderable.VerticalAlignment.CENTER

                    GlideApp.with(this)
                            .load(testUrl)
                            .centerCrop()
                            .override(400, 300)
                            .into(it.view as ImageView)

                    cardRenderable = it
                }

        arFragment.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane, motionEvent: MotionEvent ->
            Timber.i("OnTap ${motionEvent.action}")
            hitResult.also {
                Timber.d("Distance: ${it.distance}")
                Timber.d("Pose: ${it.hitPose}")
                Timber.d("Trackable ${it.trackable.trackingState}, anchor count: ${it.trackable.anchors?.size}")
            }

            plane.also {
                Timber.d("Plane type: ${it.type}")
                Timber.d("Center: ${it.centerPose}")
            }

            if (cardRenderable == null) {
                Timber.w("Renderable not ready")
                return@setOnTapArPlaneListener
            } else if (placed) {
                return@setOnTapArPlaneListener
            }

            placeImageAtAnchor(hitResult.createAnchor())
        }

        fab_test.setOnClickListener {

        }
    }

    private fun placeImageAtAnchor(anchor: Anchor) {
        Timber.i("Placing anchor")
        val anchorNode = AnchorNode(anchor)

        anchorNode.setParent(arFragment.arSceneView.scene)

//        Timber.d("${cardRenderable?.sizer}")

        val tNode = TransformableNode(arFragment.transformationSystem)
        tNode.setParent(anchorNode)

        val viewNode = Node()
        viewNode.setParent(tNode)

        val q2 = Quaternion(Vector3(1f, 0f, 0f), -90f)
        viewNode.localPosition = Vector3(0f, 0.01f, 0f)
        viewNode.localRotation = Quaternion.multiply(tNode.localRotation, q2)
        viewNode.renderable = cardRenderable
        tNode.select()

        placed = true
    }
}