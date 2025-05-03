package com.example.galleryapp.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.galleryapp.data.repository.FolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderPhotosViewModel @Inject constructor(
    private val repository: FolderRepository
) : ViewModel() {

    private val _images = MutableStateFlow<List<Uri>>(emptyList())
    val images: StateFlow<List<Uri>> = _images

    fun loadImages(bucketId: String) {
        viewModelScope.launch {
            _images.value = repository.loadImagesFromFolder(bucketId)
        }
    }
}