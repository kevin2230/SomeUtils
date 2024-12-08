package com.kk.util.subtitleimagestitching.pages

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

class SubtitleImageStitchingViewModel : ViewModel() {
    private val _selectedImageUri = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImageUri: SharedFlow<List<Uri>> = _selectedImageUri

    fun setSelectedImageUris(uris: List<Uri>) {
        _selectedImageUri.value = uris
    }
}