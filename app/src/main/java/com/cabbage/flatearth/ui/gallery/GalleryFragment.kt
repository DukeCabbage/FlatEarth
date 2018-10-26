package com.cabbage.flatearth.ui.gallery

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindDrawable
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.cabbage.flatearth.R
import com.cabbage.flatearth.mock.fileHierarchy
import com.cabbage.flatearth.ui.gallery.data.PreviewItem
import timber.log.Timber
import java.lang.RuntimeException

class GalleryFragment : Fragment() {

    @BindDrawable(R.drawable.divider)
    lateinit var divider: Drawable

    @BindView(R.id.rv_gallery)
    lateinit var rvGallery: RecyclerView

    private var unbinder: Unbinder? = null

    private lateinit var callback: Callback

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Callback) {
            callback = context
        } else {
            throw RuntimeException("Need callback")
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_gallery, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unbinder = ButterKnife.bind(this, view)
        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
    }

    private fun setupRecyclerView() {

        val dirName = "tree"
        val fileNames = fileHierarchy[dirName] ?: return

        val dataSet = List(fileNames.size) { index ->
            PreviewItem(filePath = "$dirName/${fileNames[index]}",
                        fileName = fileNames[index])
        }

        val rvViewManager = LinearLayoutManager(context)
        val rvDividerDeco = SkipLastDividerItemDecoration(context, rvViewManager.orientation, false)
        rvDividerDeco.setDrawable(divider)
        val rvViewAdapter = GalleryPreviewAdapter(dataSet, callback)

        rvGallery.layoutManager = rvViewManager
        rvGallery.addItemDecoration(rvDividerDeco)
        rvGallery.adapter = rvViewAdapter
    }

    interface Callback {
        fun imageSelected(url: String)
    }
}