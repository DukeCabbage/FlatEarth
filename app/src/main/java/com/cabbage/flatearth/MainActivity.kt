package com.cabbage.flatearth

import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.cabbage.flatearth.misc.checkIsSupportedDeviceOrFinish
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
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

//        // When you build a Renderable, Sceneform loads its resources in the background while returning
//        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
//        ModelRenderable.builder()
//                .setSource(this, R.raw.andy)
//                .build()
//                .thenAccept { andyRenderable = it }
//                .exceptionally { _ ->
//                    val toast = Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG)
//                    toast.setGravity(Gravity.CENTER, 0, 0)
//                    toast.show()
//                    null
//                }

        ViewRenderable.builder()
                .setView(this, R.layout.text_view_test)
                .build()
                .thenAccept {
                    it.verticalAlignment = ViewRenderable.VerticalAlignment.CENTER
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

            Timber.i("Placing anchor")
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            val q1 = anchorNode.localRotation
            Timber.i("$q1")
            val q2 = Quaternion(Vector3(1f, 0f, 0f), -90f)
            Timber.i("$q2")
            val q3 = Quaternion.multiply(q1, q2)
            Timber.i("$q3")
//            anchorNode.localRotation = q3
//            anchorNode.anchor = anchor
//            val q4 = anchorNode.localRotation
//            Timber.i("$q4")
            anchorNode.setParent(arFragment.arSceneView.scene)

            Timber.d("${cardRenderable?.sizer}")

            val tNode = TransformableNode(arFragment.transformationSystem)
            tNode.setParent(anchorNode)

            val viewNode = Node()
            viewNode.setParent(tNode)

            viewNode.localRotation = Quaternion.multiply(tNode.localRotation, q2)
            viewNode.renderable = cardRenderable
            tNode.select()

            placed = true
        }

        fab_test.setOnClickListener {
//            cardRenderable?.also { card ->
//                card.verticalAlignment = when (card.verticalAlignment) {
//                    ViewRenderable.VerticalAlignment.BOTTOM -> ViewRenderable.VerticalAlignment.CENTER
//                    ViewRenderable.VerticalAlignment.CENTER -> ViewRenderable.VerticalAlignment.TOP
//                    ViewRenderable.VerticalAlignment.TOP -> ViewRenderable.VerticalAlignment.BOTTOM
//                    else -> ViewRenderable.VerticalAlignment.CENTER
//                }
//            }
        }
    }
}
