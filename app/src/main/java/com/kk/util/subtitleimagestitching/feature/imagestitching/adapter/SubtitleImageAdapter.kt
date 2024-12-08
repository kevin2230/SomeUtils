package com.kk.util.subtitleimagestitching.feature.imagestitching.adapter

import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Size
import coil.transform.Transformation
import com.kk.util.subtitleimagestitching.databinding.ItemSubtitleImageBinding

class SubtitleImageAdapter : ListAdapter<Uri, SubtitleImageAdapter.ImageViewHolder>(ImageDiffCallback()) {
    
    inner class ImageViewHolder(private val binding: ItemSubtitleImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(uri: Uri, position: Int) {
            binding.imageView.load(uri) {
                crossfade(true)
                if (position > 0) {
                    transformations(SubtitleOnlyTransformation())
                }
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemSubtitleImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}

class ImageDiffCallback : DiffUtil.ItemCallback<Uri>() {
    override fun areItemsTheSame(oldItem: Uri, newItem: Uri) = oldItem == newItem
    override fun areContentsTheSame(oldItem: Uri, newItem: Uri) = oldItem == newItem
}

// 字幕区域变换
class SubtitleOnlyTransformation() : Transformation {
    override val cacheKey = "subtitle_only"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val subtitleHeight = (input.height * 0.2).toInt()
        return Bitmap.createBitmap(
            input,
            0,
            input.height - subtitleHeight,
            input.width,
            subtitleHeight
        )
    }
}