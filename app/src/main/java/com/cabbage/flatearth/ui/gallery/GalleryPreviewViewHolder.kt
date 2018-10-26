package com.cabbage.flatearth.ui.gallery

import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cabbage.flatearth.R
import com.cabbage.flatearth.misc.GlideApp
import timber.log.Timber

class GalleryPreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    init {
        itemView.findViewById<CardView>(R.id.card_preview).setOnClickListener {
            callback?.itemOnClick(adapterPosition)
        }
    }

    var callback: Callback? = null

    fun render(downloadUrl: String) {

        val ivPreview = itemView.findViewById<ImageView>(R.id.iv_preview)

        GlideApp.with(itemView.context)
                .load(downloadUrl)
                .fitCenter()
                .thumbnail(0.1f)
                .into(ivPreview)
    }

    interface Callback {
        fun itemOnClick(index: Int)
    }
}