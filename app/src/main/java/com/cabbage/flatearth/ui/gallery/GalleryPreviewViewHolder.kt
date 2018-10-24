package com.cabbage.flatearth.ui.gallery

import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cabbage.flatearth.R
import com.cabbage.flatearth.misc.GlideApp

class GalleryPreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun render(downloadUrl: String) {

        itemView.setOnClickListener {
            Toast.makeText(itemView.context, "${adapterPosition}", Toast.LENGTH_SHORT)
                    .show()
        }

        val ivPreview = itemView.findViewById<ImageView>(R.id.iv_preview)

        GlideApp.with(itemView.context)
                .load(downloadUrl)
                .fitCenter()
                .thumbnail(0.1f)
                .into(ivPreview)
    }
}