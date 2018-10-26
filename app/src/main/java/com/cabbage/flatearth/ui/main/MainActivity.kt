package com.cabbage.flatearth.ui.main

import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick
import com.cabbage.flatearth.R
import com.cabbage.flatearth.misc.GlideApp
import com.cabbage.flatearth.misc.checkIsSupportedDeviceOrFinish
import com.cabbage.flatearth.ui.gallery.GalleryFragment
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseTransformableNode
import com.google.ar.sceneform.ux.SelectionVisualizer
import com.google.ar.sceneform.ux.TransformableNode
import timber.log.Timber

class MainActivity : AppCompatActivity(),
        GalleryFragment.Callback {

    private val arFragment by lazy { supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment }

    private val posterList: MutableList<ViewRenderable> = mutableListOf()

    private val listener = Scene.OnUpdateListener {
        arFragment.arSceneView.session?.also { session ->
            val config = session.config
            config.planeFindingMode = Config.PlaneFindingMode.VERTICAL
            session.configure(config)
            removeListener()
        }
    }

    private var prototypeRenderable: ViewRenderable? = null

    @OnClick(R.id.fab_test)
    fun fabOnClick() {
        if (posterList.size >= 3) {
            return
        }

        arFragment.arSceneView.arFrame?.also { frame ->
            val point = getScreenCenter()
            val hits = frame.hitTest(point.x.toFloat(), point.y.toFloat())
            Timber.i("${hits.size} hits")
            for (hit in hits) {
                val trackable = hit.trackable
                if (trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)) {
                    placeImageAtAnchor(hit.createAnchor())
                    arFragment.arSceneView.planeRenderer.isEnabled = false
                    break
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkIsSupportedDeviceOrFinish()) return
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        setupPrototype()

        arFragment.arSceneView.scene.addOnUpdateListener(listener)
        arFragment.transformationSystem.selectionVisualizer = object : SelectionVisualizer {
            override fun applySelectionVisual(node: BaseTransformableNode?) {

            }

            override fun removeSelectionVisual(node: BaseTransformableNode?) {

            }
        }

//        arFragment.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane, motionEvent: MotionEvent ->
//            Timber.i("OnTap ${motionEvent.action}")
//            hitResult.also {
//                Timber.d("Distance: ${it.distance}")
//                Timber.d("Pose: ${it.hitPose}")
//                Timber.d("Trackable ${it.trackable.trackingState}, anchor count: ${it.trackable.anchors?.size}")
//            }
//
//            plane.also {
//                Timber.d("Plane type: ${it.type}")
//                Timber.d("Center: ${it.centerPose}")
//            }
//        }
    }

    override fun imageSelected(url: String) {
        if (prototypeRenderable == null) {
            Timber.w("Renderable not ready")
            return
        }

        val iv = prototypeRenderable?.view?.findViewById(R.id.iv_test) as? ImageView ?: return

        GlideApp.with(this)
                .load(url)
                .centerCrop()
                .override(156, 212)
                .into(iv)
    }

    private fun setupPrototype() {
        Timber.d("setupPrototype")
        ViewRenderable.builder()
                .setView(this, R.layout.image_view_test)
                .build()
                .thenAccept {
                    it.verticalAlignment = ViewRenderable.VerticalAlignment.CENTER
                    prototypeRenderable = it
                }
    }

    private fun removeListener() {
        arFragment.arSceneView.scene.removeOnUpdateListener(listener)
    }

    private fun placeImageAtAnchor(anchor: Anchor) {
        if (prototypeRenderable == null) {
            Timber.w("Renderable not ready")
            return
        }

        Timber.i("Placing anchor")

        val anchorNode = AnchorNode(anchor).apply { setParent(arFragment.arSceneView.scene) }

        val tNode = TransformableNode(arFragment.transformationSystem).apply { setParent(anchorNode) }

        val viewNode = Node().apply { setParent(tNode) }

        val q2 = Quaternion(Vector3(1f, 0f, 0f), -90f)
        viewNode.localRotation = Quaternion.multiply(tNode.localRotation, q2)
        viewNode.renderable = prototypeRenderable
        tNode.select()
        posterList.add(prototypeRenderable!!)

        prototypeRenderable = null
        setupPrototype()
    }

    private fun getScreenCenter() =
            findViewById<View>(android.R.id.content)
                    .let { vw ->
                        Point(vw.width / 2, vw.height / 2)
                    }
}