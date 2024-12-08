package com.kk.util.subtitleimagestitching.feature.imagestitching

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.kk.util.subtitleimagestitching.base.BaseToolFragment
import com.kk.util.subtitleimagestitching.databinding.FragmentSubtitleImageStitchingBinding
import com.kk.util.subtitleimagestitching.feature.imagestitching.adapter.SubtitleImageAdapter
import com.kk.util.subtitleimagestitching.feature.imagestitching.helper.SwipeToDeleteCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class SubtitleImageStitchingFragment : BaseToolFragment<FragmentSubtitleImageStitchingBinding>() {
    private val imageAdapter = SubtitleImageAdapter()
    private val selectedImages = mutableListOf<Uri>()
    
    private val pickImages = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        uris?.let {
            selectedImages.addAll(it)
            imageAdapter.submitList(selectedImages.toList())
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSubtitleImageStitchingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupButtons()
    }

    private fun setupRecyclerView() {
        binding.rvImages.apply {
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(requireContext())
            
            // 添加滑动删除功能
            val swipeHandler = SwipeToDeleteCallback { position ->
                selectedImages.removeAt(position)
                imageAdapter.submitList(selectedImages.toList()) {
                    if (position == 0) {
                        imageAdapter.notifyItemChanged(0)
                    }
                }
            }
            ItemTouchHelper(swipeHandler).attachToRecyclerView(this)
        }
    }

    private fun setupButtons() {
        binding.btnSelectImages.setOnClickListener {
            pickImages.launch("image/*")
        }
        
        binding.btnExport.setOnClickListener {
            if (selectedImages.isEmpty()) {
                Toast.makeText(requireContext(), "请先选择图片", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            exportStitchedImage()
        }
    }

    private fun exportStitchedImage() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val stitchedBitmap = ImageStitcher.stitchImages(requireContext(), selectedImages)
                saveBitmapToGallery(stitchedBitmap)
                
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "保存成功", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "保存失败: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveBitmapToGallery(bitmap: Bitmap) {
        val filename = "StitchedImage_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                
                requireContext().contentResolver.let { resolver ->
                    val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    fos = imageUri?.let { resolver.openOutputStream(it) }
                }
            } else {
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDir, filename)
                fos = FileOutputStream(image)
            }
            
            fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
        } finally {
            fos?.close()
        }
    }
} 