package com.cabbage.flatearth.ui.gallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cabbage.flatearth.R
import timber.log.Timber

class GalleryActivity : AppCompatActivity(),
        GalleryFragment.Callback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
    }

    override fun imageSelected(url: String) {
        Timber.i(url)
    }
}