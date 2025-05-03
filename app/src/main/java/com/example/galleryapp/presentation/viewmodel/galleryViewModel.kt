package com.example.galleryapp.presentation.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.galleryapp.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    application: Application,
    private val repository: ImageRepository
) : AndroidViewModel(application) {

    private val _photos = MutableStateFlow<List<Uri>>(emptyList())
    val photos: StateFlow<List<Uri>> = _photos

    init {
        loadPhotos()
    }

    fun loadPhotos() {
        viewModelScope.launch {
            _photos.value = repository.loadImagesFromGallery()
        }
    }

    fun searchPhotos(startDateMillis: Long, endDateMillis: Long) {
        viewModelScope.launch {
            _photos.value = repository.searchImagesByDate(startDateMillis, endDateMillis)
        }
    }

}