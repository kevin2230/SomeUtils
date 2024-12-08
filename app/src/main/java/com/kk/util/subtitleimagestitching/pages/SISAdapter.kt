package com.kk.util.subtitleimagestitching.pages

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.kk.util.subtitleimagestitching.R

class SISAdapter : RecyclerView.Adapter<SISAdapter.ViewHolder>() {

    private val uriData = mutableListOf<Uri>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SISAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.selected_image_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(uriData[position])
    }

    override fun getItemCount(): Int {
        return uriData.size
    }

    fun updateData(uris: List<Uri>) {
        uriData.clear()
        uriData.addAll(uris)
        notifyDataSetChanged()
    }

    fun addData(uris: List<Uri>){
        uriData.addAll(uris)
        notifyDataSetChanged()
    }

    fun clearData() {
        val size = uriData.size
        uriData.clear()
        notifyItemRangeRemoved(0, size)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.selected_image)

        fun bind(uri: Uri) {
//            imageView.context.contentResolver.openInputStream(uri)?.use {
//                val bitmap = BitmapFactory.decodeStream(it)
//                imageView.setImageBitmap(bitmap)
//            }

            Glide.with(imageView)
                .asBitmap()
                .load(uri)
//                .override(500,500)
                .into(imageView/*object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        val width = imageView.width
                        val aspectRatio = resource.height.toFloat() / resource.width
                        val height = (width * aspectRatio).toInt()

                        imageView.layoutParams.height = height
                        imageView.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        imageView.setImageDrawable(placeholder)
                    }

                }*/)
        }
    }

}
