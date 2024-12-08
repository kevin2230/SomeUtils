package com.kk.util.subtitleimagestitching.pages

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kk.util.subtitleimagestitching.R
import kotlinx.coroutines.launch

class SubtitleImageStitchingFragment : Fragment() {
    private val viewModel: SubtitleImageStitchingViewModel by viewModels()

    private lateinit var outputImageBtn: Button
    private lateinit var selectImageView: ImageView
    private lateinit var recyclerView: RecyclerView
    private var mAdapter: SISAdapter? = null

    private val imagePickLauncher =
        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) {
            viewModel.setSelectedImageUris(it)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subtitle_image_stitching, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        outputImageBtn = view.findViewById<Button>(R.id.output_image)
//        selectImageView = view.findViewById<ImageView>(R.id.select_image)
//        recyclerView = view.findViewById<RecyclerView>(R.id.selected_images)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            mAdapter = SISAdapter().also { adapter = it }
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedImageUri
                    .collect {
                        if (it.isNotEmpty()) {
                            handleSelectedImages(it)
                        }
                    }
            }
        }

        outputImageBtn.setOnClickListener { outputImage() }

        selectImageView.setOnClickListener {
            imagePickLauncher.launch(arrayOf("image/*"))
        }
    }

    private val REQUEST_PICK_IMAGES = 1

    private fun outputImage() {

    }

    private fun handleSelectedImages(uriList: List<Uri>) {
        mAdapter?.run {
            if (uriList.isNotEmpty() && recyclerView.visibility != View.VISIBLE) {
                recyclerView.visibility = View.VISIBLE
                selectImageView.visibility = View.INVISIBLE
            }
            recyclerView.viewTreeObserver.addOnGlobalLayoutListener {
                recyclerView.viewTreeObserver.removeOnGlobalLayoutListener { recyclerView }
                updateData(uriList)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubtitleImageStitchingFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
            }
    }
}