package com.cabbage.flatearth.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cabbage.flatearth.R
import com.cabbage.flatearth.ui.gallery.data.PreviewItem
import com.google.firebase.storage.FirebaseStorage
import timber.log.Timber

class GalleryPreviewAdapter(initData: List<PreviewItem> = emptyList(),
                            private val callback: GalleryFragment.Callback) :
        RecyclerView.Adapter<GalleryPreviewViewHolder>(),
        GalleryPreviewViewHolder.Callback {

    override fun itemOnClick(index: Int) {
        val url = items[index].downloadUrl
        if (url == null) {
            Timber.w("No download url")
        } else {
            callback.imageSelected(url)
        }
    }

    private var items: List<PreviewItem> = initData
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryPreviewViewHolder {

        val iv = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gallery_preview, parent, false)

        val vh = GalleryPreviewViewHolder(iv)

        vh.callback = this

        return vh
    }

    override fun onBindViewHolder(holder: GalleryPreviewViewHolder, position: Int) {
        val item = items[position]
        val downloadUrl = item.downloadUrl

        if (downloadUrl == null) {
            FirebaseStorage.getInstance().reference
                    .child(item.filePath).downloadUrl
                    .addOnSuccessListener {
                        val url = it.toString()
                        item.downloadUrl = url
                        holder.render(url)
                    }
        } else {
            holder.render(downloadUrl)
        }
    }
}